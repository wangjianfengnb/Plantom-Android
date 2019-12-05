package com.phantom.client.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.model.ChatMessage;
import com.phantom.client.model.Constants;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.message.Message;
import com.phantom.client.model.NetworkMessage;
import com.phantom.client.network.ConnectionManager;
import com.phantom.client.sqlite.ChatHelper;
import com.phantom.client.utils.HttpUtil;
import com.phantom.client.utils.RxHelper;
import com.phantom.common.C2CMessageRequest;
import com.phantom.common.C2CMessageResponse;
import com.phantom.common.C2GMessageResponse;
import com.phantom.common.FetchMessageResponse;
import com.phantom.common.InformFetchMessageResponse;
import com.phantom.common.OfflineMessage;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager implements MessageHandler, IChatManager {

    private static final String TAG = ChatManager.class.getSimpleName();
    private String serverApi;

    /**
     * 本地最大的sequence
     */
    private volatile long sequence;

    /**
     * 本地最大的时间戳
     */
    private volatile long timestamp;

    private List<OnConversationChangeListener> onConversationChangeListeners = new ArrayList<>();

    private List<OnMessageListener> onMessageListeners = new ArrayList<>();

    private ChatHelper chatHelper;

    private String userId;

    private Map<Long, Long> conversationMaxMessageId = new ConcurrentHashMap<>();

    private Map<String, Message> inFlightMessages = new HashMap<>();

    ChatManager(Context context, String serverApi) {
        chatHelper = new ChatHelper(context);
        this.serverApi = serverApi;
        Log.i(TAG, "加载最大的消息时间戳为：" + timestamp + ", 最大序列号为：" + sequence);
    }

    private long loadMaxSequence() {
        return chatHelper.getMaxSequence(userId);
    }

    private long loadMaxMessageTimestamp() {
        return chatHelper.getMaxMessageTimestamp(userId);
    }

    void onAuthenticateSuccess(String uid) {
        this.userId = uid;
        timestamp = loadMaxMessageTimestamp();
        sequence = loadMaxSequence();
        synchronized (this) {
            notifyAll();
        }
        fetchMessage(this.userId);
    }

    /**
     * 抓取消息
     *
     * @param uid 用户ID
     */
    private void fetchMessage(String uid) {
        ConnectionManager.getInstance().fetchMessage(uid, getMaxTimestamp());
    }

    private long getMaxTimestamp() {
        return timestamp;
    }

    @Override
    public void onMessage(NetworkMessage networkMessage) throws InvalidProtocolBufferException {
        int requestType = networkMessage.getRequestType();
        if (Constants.REQUEST_TYPE_C2C_SEND == requestType) {
            processC2cMessageResponse(networkMessage);
        } else if (Constants.REQUEST_TYPE_INFORM_FETCH == requestType) {
            processFetchInform(networkMessage);
        } else if (Constants.REQUEST_TYPE_MESSAGE_FETCH == requestType) {
            processOfflineMessages(networkMessage);
        } else if (Constants.REQUEST_TYPE_C2G_SEND == requestType) {
            processC2gMessageResponse(networkMessage);
        }
    }

    private void processFetchInform(NetworkMessage networkMessage) throws InvalidProtocolBufferException {
        Log.i(TAG, "收到抓取离线消息的通知...");
        InformFetchMessageResponse informFetchMessageResponse =
                InformFetchMessageResponse.parseFrom(networkMessage.getBody());
        fetchMessage(informFetchMessageResponse.getUid());
    }

    private void processC2gMessageResponse(NetworkMessage networkMessage) throws InvalidProtocolBufferException {
        byte[] body = networkMessage.getBody();
        C2GMessageResponse c2GMessageResponse = C2GMessageResponse.parseFrom(body);
        if (c2GMessageResponse.getStatus() == Constants.RESPONSE_STATUS_OK) {
            Log.i(TAG, "发送群聊消息成功...");
        } else {
            Log.i(TAG, "发送群聊消息失败，重新发送...");
        }
    }

    private void processOfflineMessages(NetworkMessage networkMessage) throws InvalidProtocolBufferException {
        FetchMessageResponse response = FetchMessageResponse.parseFrom(networkMessage.getBody());
        Log.i(TAG, "收到抓取离线消息的响应，是否结果为空：" + response.getIsEmpty());
        if (!response.getIsEmpty()) {
            synchronized (ConnectionManager.class) {
                List<OfflineMessage> messagesList = response.getMessagesList();
                for (OfflineMessage msg : messagesList) {
                    // 要求sequence严格递增，但是由于测试阶段没有地方持久化sequence，先注释
//                        if (msg.getSequence() == sequence + 1) {
                    sequence++;
                    timestamp = msg.getTimestamp();
                    handleMessage(msg);
//                        } else {
//                            Log.i(TAG, "发现获取到的消息sequence不连续，丢弃后续的消息");
//                            break;
//                        }
                }
                fetchMessage(response.getUid());
            }
        }
    }

    private void processC2cMessageResponse(NetworkMessage networkMessage) throws InvalidProtocolBufferException {
        byte[] body = networkMessage.getBody();
        C2CMessageResponse c2CMessageResponse = C2CMessageResponse.parseFrom(body);
        int status = Message.STATUS_SENDING;
        if (c2CMessageResponse.getStatus() == Constants.RESPONSE_STATUS_OK) {
            status = Message.STATUS_SEND_SUCCESS;
            Log.i(TAG, "发送单聊消息成功...");
        } else if (c2CMessageResponse.getStatus() == Constants.RESPONSE_STATUS_ERROR) {
            status = Message.STATUS_SEND_FAILURE;
            Log.i(TAG, "发送单聊消息失败...");
        }
        String crc = c2CMessageResponse.getCrc();
        Message message = inFlightMessages.remove(crc);
        if (message != null) {
            message.setStatus(status);
            long messageId = c2CMessageResponse.getMessageId();
            long timestamp = c2CMessageResponse.getTimestamp();
            chatHelper.updateMessage(message.getId(), status, messageId, timestamp);
            RxHelper.runOnUI(message::invokeListener);
        }
    }

    private void handleMessage(OfflineMessage msg) {
        Conversation conversation = getOrCreateConversation(msg);
        ChatMessage chatMessage = ChatMessage.parse(conversation, msg);
        chatMessage.setUserId(userId);
        chatHelper.saveMessage(chatMessage);
        Message message = chatMessage.toMessage(conversation.getConversationId());

        for (OnMessageListener listener : onMessageListeners) {
            if (message.getConversationId().equals(listener.conversationId())) {
                RxHelper.runOnUI(() -> listener.onMessage(message));
            }
        }

    }

    private synchronized Conversation getOrCreateConversation(OfflineMessage message) {
        int conversationType = TextUtils.isEmpty(message.getGroupId()) ? Conversation.TYPE_C2C : Conversation.TYPE_C2G;
        String targetId = TextUtils.isEmpty(message.getGroupId()) ? message.getSenderId() : message.getGroupId();
        Conversation conversation = chatHelper.getConversation(userId, conversationType, targetId);
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setUnread(inConversation(conversation.getConversationId()) ? 0 : 1);
            conversation.setTargetId(targetId);
            conversation.setUserId(userId);
            conversation.setConversationType(conversationType);
            conversation.setLastMessage(ChatMessage.getMessageDescription(message));
            conversation.setLastUpdate(message.getTimestamp());
            appendConversationInfo(conversation);
            chatHelper.saveConversation(conversation);
            informConversationChange(conversation, true);
        } else {
            conversation.setUnread(conversation.getUnread() +
                    (inConversation(conversation.getConversationId()) ? 0 : 1));
            conversation.setLastMessage(ChatMessage.getMessageDescription(message));
            conversation.setLastUpdate(message.getTimestamp());
            chatHelper.updateConversation(conversation);
            informConversationChange(conversation, false);
        }
        return conversation;
    }

    private void appendConversationInfo(Conversation conversation) {
        if (conversation.getConversationType() == Conversation.TYPE_C2C) {
            String s = HttpUtil.get(serverApi + "/user/" + conversation.getTargetId(), null);
            JSONObject obj = JSONObject.parseObject(s);
            String conversationName = obj.getString("userName");
            String conversationAvatar = obj.getString("avatar");
            conversation.setConversationName(conversationName);
            conversation.setConversationAvatar(conversationAvatar);
        } else {
            String s = HttpUtil.get(serverApi + "/group/" + conversation.getTargetId(), null);
            JSONObject obj = JSONObject.parseObject(s);
            String conversationName = obj.getString("groupName");
            String conversationAvatar = obj.getString("groupAvatar");
            conversation.setConversationName(conversationName);
            conversation.setConversationAvatar(conversationAvatar);
        }
    }

    private boolean inConversation(long conversationId) {
        return conversationMaxMessageId.get(conversationId) != null;
    }

    private void informConversationChange(Conversation conversation, boolean isNew) {
        RxHelper.runOnUI(() -> {
            for (OnConversationChangeListener listener : onConversationChangeListeners) {
                listener.onConversationChange(conversation, isNew);
            }
        });
    }

    @Override
    public void loadConversation(int page, int size, LoadConversationListener listener) {
        while (userId == null) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        page--;
        List<Conversation> conversations = chatHelper.loadConversation(userId, page, size);
        if (listener != null) {
            listener.onLoadCompleted(conversations);
        }
    }

    @Override
    public void addOnConversationChangeListener(OnConversationChangeListener onConversationChangeListener) {
        this.onConversationChangeListeners.add(onConversationChangeListener);
    }

    @Override
    public void loadMessage(Conversation conversation, OnLoadMessageListener listener) {
        if (conversation.getConversationType() == Conversation.TYPE_C2C) {
            Long maxId = conversationMaxMessageId.get(conversation.getConversationId());
            if (maxId == null) {
                maxId = 0L;
                conversationMaxMessageId.put(conversation.getConversationId(), maxId);
            }
            List<ChatMessage> chatMessages = chatHelper.loadC2CMessage(userId, conversation.getTargetId(), maxId);
            if (!chatMessages.isEmpty()) {
                maxId = chatMessages.get(0).getId();
                conversationMaxMessageId.put(conversation.getConversationId(), maxId);
                List<Message> messages = new ArrayList<>();
                for (ChatMessage msg : chatMessages) {
                    Message message = msg.toMessage(conversation.getConversationId());
                    messages.add(message);
                }
                if (listener != null) {
                    listener.onMessage(messages);
                }
            }
        } else if (conversation.getConversationType() == Conversation.TYPE_C2G) {
            // TODO 群聊
        }

        conversation.setLastUpdate(conversation.getLastUpdate());
        conversation.setUnread(0);
        conversation.setLastMessage(conversation.getLastMessage());
        informConversationChange(conversation, false);
        chatHelper.updateConversation(conversation);

    }

    @Override
    public void closeConversation(Long conversationId) {
        conversationMaxMessageId.remove(conversationId);
    }

    @Override
    public void sendMessage(Message message) {
        RxHelper.runOnBackground(() -> {
            ChatMessage chatMessage = ChatMessage.parse(message, userId);
            chatHelper.saveMessage(chatMessage);
            message.setId(chatMessage.getId());
            inFlightMessages.put(message.getCrc(), message);
            C2CMessageRequest request = C2CMessageRequest.newBuilder()
                    .setContent(chatMessage.getMessageContent())
                    .setSenderId(chatMessage.getSenderId())
                    .setReceiverId(chatMessage.getReceiverId())
                    .setPlatform(Constants.PLATFORM_ANDROID)
                    .setCrc(message.getCrc())
                    .build();
            Conversation conversation = chatHelper.getConversation(message.getConversationId());
            conversation.setLastUpdate(chatMessage.getTimestamp());
            conversation.setUnread(0);
            conversation.setLastMessage(message.getContent());
            informConversationChange(conversation, false);
            chatHelper.updateConversation(conversation);
            ConnectionManager.getInstance().sendMessage(NetworkMessage.buildC2CMessageRequest(request));
        });
    }

    @Override
    public void addOnMessageListener(OnMessageListener messageListener) {
        onMessageListeners.add(messageListener);
    }

    @Override
    public void removeOnMessageListener(OnMessageListener messageListener) {
        onMessageListeners.remove(messageListener);
    }
}

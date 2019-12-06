package com.phantom.client.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.model.ChatMessage;
import com.phantom.client.model.Constants;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.NetworkMessage;
import com.phantom.client.model.message.Message;
import com.phantom.client.network.ConnectionManager;
import com.phantom.client.sqlite.ChatHelper;
import com.phantom.client.utils.HttpUtil;
import com.phantom.client.utils.RxHelper;
import com.phantom.common.C2CMessageRequest;
import com.phantom.common.C2CMessageResponse;
import com.phantom.common.C2GMessageRequest;
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

    private Map<Long, Long> conversationMinMessageId = new ConcurrentHashMap<>();

    private Map<String, ChatMessage> inFlightMessages = new HashMap<>();

    private Map<Long, Message> sendingMessage = new HashMap<>();

    private volatile boolean inConversation = false;

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
        chatHelper.fireMessageSendingFail(this.userId);
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
        ChatMessage message = inFlightMessages.remove(crc);
        if (message != null) {
            message.setMessageId(c2CMessageResponse.getMessageId());
            message.setTimestamp(c2CMessageResponse.getTimestamp());
            message.setMessageStatus(status);
            message.setCrc(crc);
            updateMessage(message);
        } else {
            Log.e(TAG, "收到消息响应，但是找不到发送中的消息：" + c2CMessageResponse);
        }
    }

    private void processC2gMessageResponse(NetworkMessage networkMessage) throws InvalidProtocolBufferException {
        byte[] body = networkMessage.getBody();
        C2GMessageResponse c2GMessageResponse = C2GMessageResponse.parseFrom(body);
        int status = Message.STATUS_SENDING;
        if (c2GMessageResponse.getStatus() == Constants.RESPONSE_STATUS_OK) {
            Log.i(TAG, "发送群聊消息成功...");
            status = Message.STATUS_SEND_SUCCESS;
        } else if (c2GMessageResponse.getStatus() == Constants.RESPONSE_STATUS_ERROR) {
            Log.i(TAG, "发送群聊消息失败，重新发送...");
            status = Message.STATUS_SEND_FAILURE;
        }
        String crc = c2GMessageResponse.getCrc();
        ChatMessage message = inFlightMessages.remove(crc);
        if (message != null) {
            message.setMessageId(c2GMessageResponse.getMessageId());
            message.setTimestamp(c2GMessageResponse.getTimestamp());
            message.setMessageStatus(status);
            message.setCrc(crc);
            message.setGroupId(c2GMessageResponse.getGroupId());
            updateMessage(message);
        } else {
            Log.e(TAG, "收到消息响应，但是找不到发送中的消息：" + c2GMessageResponse);
        }
    }

    private void updateMessage(ChatMessage message) {
        chatHelper.updateMessage(message);
        Message msg = sendingMessage.remove(message.getId());
        if (msg != null) {
            RxHelper.runOnUI(msg::invokeListener);
        }
    }

    private void handleMessage(OfflineMessage msg) {
        Log.d(TAG, "收到消息：" + msg);
        int conversationType = TextUtils.isEmpty(msg.getGroupId()) ? Conversation.TYPE_C2C : Conversation.TYPE_C2G;
        String targetId = TextUtils.isEmpty(msg.getGroupId()) ? msg.getSenderId() : msg.getGroupId();
        String lastMessage = ChatMessage.getMessageDescription(msg);
        long lastUpdate = msg.getTimestamp();
        int unread = inConversation ? 0 : 1;
        Conversation conversation = createOrUpdateConversation(conversationType, targetId,
                lastMessage, lastUpdate, unread);
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

    private synchronized Conversation createOrUpdateConversation(int conversationType, String targetId,
                                                                 String lastMessage, long lastUpdate,
                                                                 int unread) {
        Conversation conversation = chatHelper.getConversation(userId, conversationType, targetId);
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setUnread(unread);
            conversation.setTargetId(targetId);
            conversation.setUserId(userId);
            conversation.setConversationType(conversationType);
            conversation.setLastMessage(lastMessage);
            conversation.setLastUpdate(lastUpdate);
            appendConversationInfo(conversation);
            chatHelper.saveConversation(conversation);
            informConversationChange(conversation, true);
        } else {
            conversation.setUnread(conversation.getUnread() + unread);
            conversation.setLastMessage(lastMessage);
            conversation.setLastUpdate(lastUpdate > 0 ? lastUpdate : conversation.getLastUpdate());
            chatHelper.updateConversation(conversation);
            informConversationChange(conversation, false);
        }
        return conversation;
    }

    private void appendConversationInfo(Conversation conversation) {
        try {
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
        } catch (Exception e) {
            Log.e(TAG, "获取用户信息失败：", e);
            conversation.setConversationName("");
            conversation.setConversationAvatar("");
        }
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
    public void addOnConversationChangeListener(OnConversationChangeListener listener) {
        this.onConversationChangeListeners.add(listener);
    }

    @Override
    public void removeOnConversationChangeListener(OnConversationChangeListener listener) {
        this.onConversationChangeListeners.remove(listener);
    }

    @Override
    public void loadMessage(Conversation conversation, int page, OnLoadMessageListener listener) {
        inConversation = true;
        Long minId = conversationMinMessageId.get(conversation.getConversationId());
        minId = minId == null ? Long.MAX_VALUE : minId;
        List<ChatMessage> chatMessages;
        if (conversation.getConversationType() == Conversation.TYPE_C2C) {
            chatMessages = chatHelper.loadC2CMessage(userId, conversation.getTargetId(), minId, page);
        } else {
            chatMessages = chatHelper.loadC2GMessage(userId, conversation.getTargetId(), minId, page);
        }
        if (!chatMessages.isEmpty()) {
            minId = chatMessages.get(chatMessages.size() - 1).getId();
            conversationMinMessageId.put(conversation.getConversationId(), minId);
            List<Message> messages = new ArrayList<>();
            for (ChatMessage msg : chatMessages) {
                Message message = msg.toMessage(conversation.getConversationId());
                messages.add(message);
            }
            if (listener != null) {
                listener.onMessage(messages);
            }
        }
        conversation.setLastUpdate(conversation.getLastUpdate());
        conversation.setUnread(0);
        conversation.setLastMessage(conversation.getLastMessage());
        informConversationChange(conversation, false);
        chatHelper.updateConversation(conversation);
    }

    @Override
    public void closeConversation(Long conversationId) {
        conversationMinMessageId.remove(conversationId);
        inConversation = false;
    }

    @Override
    public void sendMessage(Message message) {
        RxHelper.runOnBackground(() -> {
            ChatMessage chatMessage = ChatMessage.parse(message, userId);
            chatHelper.saveMessage(chatMessage);
            message.setId(chatMessage.getId());
            inFlightMessages.put(message.getCrc(), chatMessage);
            sendingMessage.put(message.getId(), message);
            Conversation conversation = chatHelper.getConversation(message.getConversationId());
            conversation.setLastUpdate(chatMessage.getTimestamp());
            conversation.setUnread(0);
            conversation.setLastMessage(message.getContent());
            informConversationChange(conversation, false);
            chatHelper.updateConversation(conversation);
            if (message.getConversationType() == Conversation.TYPE_C2C) {
                C2CMessageRequest request = C2CMessageRequest.newBuilder()
                        .setContent(chatMessage.getMessageContent())
                        .setSenderId(chatMessage.getSenderId())
                        .setReceiverId(chatMessage.getReceiverId())
                        .setPlatform(Constants.PLATFORM_ANDROID)
                        .setCrc(message.getCrc())
                        .build();
                ConnectionManager.getInstance().sendMessage(NetworkMessage.buildC2CMessageRequest(request));
            } else if (message.getConversationType() == Conversation.TYPE_C2G) {
                C2GMessageRequest request = C2GMessageRequest.newBuilder()
                        .setContent(chatMessage.getMessageContent())
                        .setSenderId(chatMessage.getSenderId())
                        .setGroupId(chatMessage.getGroupId())
                        .setPlatform(Constants.PLATFORM_ANDROID)
                        .setCrc(message.getCrc())
                        .build();
                ConnectionManager.getInstance().sendMessage(NetworkMessage.buildC2gMessageRequest(request));
            }
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

    @Override
    public void openConversation(int conversationType, String targetId, OnConversationLoadListener listener) {
        RxHelper.runOnBackground(() -> {
            Conversation conversation = createOrUpdateConversation(conversationType, targetId,
                    "", -1, 0);
            if (listener != null) {
                RxHelper.runOnUI(() -> listener.onConversationLoad(conversation));
            }

        });
    }
}

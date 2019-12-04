package com.phantom.client.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.model.ChatMessage;
import com.phantom.client.model.Constants;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.message.Message;
import com.phantom.client.model.request.C2CMessageResponse;
import com.phantom.client.model.request.C2GMessageResponse;
import com.phantom.client.model.request.FetchMessageResponse;
import com.phantom.client.model.request.InformFetchMessageResponse;
import com.phantom.client.model.request.NetworkMessage;
import com.phantom.client.model.request.OfflineMessage;
import com.phantom.client.network.ConnectionManager;
import com.phantom.client.sqlite.ChatHelper;
import com.phantom.client.utils.RxHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager implements MessageHandler, IChatManager {

    private static final String TAG = ChatManager.class.getSimpleName();

    /**
     * 本地最大的sequence
     */
    private volatile long sequence;

    /**
     * 本地最大的时间戳
     */
    private volatile long timestamp;

    private List<OnConversationChangeListener> onConversationChangeListeners = new ArrayList<>();

    private ChatHelper chatHelper;

    private String userId;

    private Map<Long, Long> conversationMaxMessageId = new ConcurrentHashMap<>();

    ChatManager(Context context) {
        chatHelper = new ChatHelper(context);
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
    void fetchMessage(String uid) {

        ConnectionManager.getInstance().fetchMessage(uid, getMaxTimestamp());
    }

    private long getMaxTimestamp() {
        return timestamp;
    }

    @Override
    public void onMessage(NetworkMessage networkMessage) throws InvalidProtocolBufferException {
        int requestType = networkMessage.getRequestType();
        if (Constants.REQUEST_TYPE_C2C_SEND == requestType) {
            byte[] body = networkMessage.getBody();
            C2CMessageResponse c2CMessageResponse = C2CMessageResponse.parseFrom(body);
            if (c2CMessageResponse.getStatus() == Constants.RESPONSE_STATUS_OK) {
                Log.i(TAG, "发送单聊消息成功...");
            } else {
                Log.i(TAG, "发送单聊消息失败，重新发送...");
            }
        } else if (Constants.REQUEST_TYPE_INFORM_FETCH == requestType) {
            Log.i(TAG, "收到抓取离线消息的通知...");
            InformFetchMessageResponse informFetchMessageResponse =
                    InformFetchMessageResponse.parseFrom(networkMessage.getBody());
            fetchMessage(informFetchMessageResponse.getUid());
        } else if (Constants.REQUEST_TYPE_MESSAGE_FETCH == requestType) {
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
        } else if (Constants.REQUEST_TYPE_C2G_SEND == requestType) {
            byte[] body = networkMessage.getBody();
            C2GMessageResponse c2GMessageResponse = C2GMessageResponse.parseFrom(body);
            if (c2GMessageResponse.getStatus() == Constants.RESPONSE_STATUS_OK) {
                Log.i(TAG, "发送群聊消息成功...");
            } else {
                Log.i(TAG, "发送群聊消息失败，重新发送...");
            }
        }
    }

    private void handleMessage(OfflineMessage msg) {
        Conversation conversation = getOrCreateConversation(msg);
        ChatMessage chatMessage = ChatMessage.parse(conversation, msg);
        chatMessage.setUserId(userId);
        chatHelper.saveMessage(chatMessage);
    }

    private synchronized Conversation getOrCreateConversation(OfflineMessage message) {
        int conversationType = TextUtils.isEmpty(message.getGroupId()) ? Conversation.TYPE_C2C : Conversation.TYPE_C2G;
        String targetId = TextUtils.isEmpty(message.getGroupId()) ? message.getSenderId() : message.getGroupId();
        Conversation conversation = chatHelper.getConversation(userId, conversationType, targetId);
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setUnread(1);
            conversation.setTargetId(targetId);
            conversation.setUserId(userId);
            conversation.setConversationType(conversationType);
            conversation.setConversationName("老子的ID是" + targetId);
            conversation.setLastMessage(message.getContent());
            conversation.setLastUpdate(message.getTimestamp());
            chatHelper.saveConversation(conversation);
            informConversationChange(conversation, true);
        } else {
            conversation.setUnread(conversation.getUnread() + 1);
            conversation.setLastMessage(message.getContent());
            conversation.setLastUpdate(message.getTimestamp());
            chatHelper.updateConversationUnread(conversation);
            informConversationChange(conversation, false);
        }
        return conversation;
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
        // 如果是单聊，则根据目标用户id，消息类型是单聊分页查找。
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
    }

    @Override
    public void closeConversation(Long conversationId) {
        conversationMaxMessageId.remove(conversationId);
    }
}

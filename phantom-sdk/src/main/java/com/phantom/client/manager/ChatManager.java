package com.phantom.client.manager;

import android.content.Context;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.model.ChatMessage;
import com.phantom.client.model.Constants;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.request.C2CMessageResponse;
import com.phantom.client.model.request.C2GMessageResponse;
import com.phantom.client.model.request.FetchMessageResponse;
import com.phantom.client.model.request.InformFetchMessageResponse;
import com.phantom.client.model.request.Message;
import com.phantom.client.model.request.OfflineMessage;
import com.phantom.client.network.ConnectionManager;
import com.phantom.client.sqlite.ChatHelper;

import java.util.List;

public class ChatManager implements MessageHandler, IChatManager {

    private static final String TAG = ChatManager.class.getSimpleName();

    /**
     * 本地最大的sequence
     */
    private volatile long sequence = 0;

    /**
     * 本地最大的时间戳
     */
    private volatile long timestamp = 0;

    private ChatHelper chatHelper;

    public ChatManager(Context context) {
        chatHelper = new ChatHelper(context);
        timestamp = loadMaxMessageTimestamp();
        sequence = loadMaxSequence();
        Log.i(TAG, "加载最大的消息时间戳为：" + timestamp + ", 最大序列号为：" + sequence);
    }

    private long loadMaxSequence() {
        return chatHelper.getMaxSequence();
    }

    private long loadMaxMessageTimestamp() {
        return chatHelper.getMaxMessageTimestamp();
    }

    /**
     * 抓取消息
     *
     * @param uid 用户ID
     */
    public void fetchMessage(String uid) {
        ConnectionManager.getInstance().fetchMessage(uid, getMaxTimestamp());
    }

    private long getMaxTimestamp() {
        return timestamp;
    }

    @Override
    public void onMessage(Message message) throws InvalidProtocolBufferException {
        int requestType = message.getRequestType();
        if (Constants.REQUEST_TYPE_C2C_SEND == requestType) {
            byte[] body = message.getBody();
            C2CMessageResponse c2CMessageResponse = C2CMessageResponse.parseFrom(body);
            if (c2CMessageResponse.getStatus() == Constants.RESPONSE_STATUS_OK) {
                Log.i(TAG, "发送单聊消息成功...");
            } else {
                Log.i(TAG, "发送单聊消息失败，重新发送...");
            }
        } else if (Constants.REQUEST_TYPE_INFORM_FETCH == requestType) {
            Log.i(TAG, "收到抓取离线消息的通知...");
            InformFetchMessageResponse informFetchMessageResponse =
                    InformFetchMessageResponse.parseFrom(message.getBody());
            fetchMessage(informFetchMessageResponse.getUid());
        } else if (Constants.REQUEST_TYPE_MESSAGE_FETCH == requestType) {
            FetchMessageResponse response = FetchMessageResponse.parseFrom(message.getBody());
            Log.i(TAG, "收到抓取离线消息的响应，是否结果为空：" + response.getIsEmpty());
            if (!response.getIsEmpty()) {
                synchronized (ConnectionManager.class) {
                    List<OfflineMessage> messagesList = response.getMessagesList();
                    for (OfflineMessage msg : messagesList) {
                        // 要求sequence严格递增，但是由于测试阶段没有地方持久化sequence，先注释
                        if (msg.getSequence() == sequence + 1) {
                            sequence++;
                            timestamp = msg.getTimestamp();
                            handleMessage(msg);
                        } else {
                            Log.i(TAG, "发现获取到的消息sequence不连续，丢弃后续的消息");
                            break;
                        }
                    }
                    fetchMessage(response.getUid());
                }
            }
        } else if (Constants.REQUEST_TYPE_C2G_SEND == requestType) {
            byte[] body = message.getBody();
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
        chatHelper.saveMessage(chatMessage);
    }

    private synchronized Conversation getOrCreateConversation(OfflineMessage message) {
        int conversationType = message.getGroupId() == null ? Conversation.TYPE_C2G : Conversation.TYPE_C2C;
        String targetId = message.getGroupId() == null ? message.getSenderId() : message.getGroupId();
        Conversation conversation = chatHelper.getConversation(conversationType, targetId);
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setUnread(1);
            conversation.setTargetId(message.getGroupId());
            conversation.setConversationType(Conversation.TYPE_C2G);
            conversation.setConversationName("群聊（应该从接口获取）");
            chatHelper.saveConversation(conversation);
        } else {
            conversation.setUnread(conversation.getUnread() + 1);
            chatHelper.updateConversationUnread(conversation);
        }
        return conversation;
    }

    @Override
    public void loadConversation(int page, int size,LoadConversationListener listener) {
        List<Conversation> conversations = chatHelper.loadConversation(page, size);
        if(listener != null){
            listener.onLoadCompleted(conversations);
        }
    }

    @Override
    public void addOnConversationChangeListener(OnConversationChangeListener onConversationChangeListener) {

    }
}

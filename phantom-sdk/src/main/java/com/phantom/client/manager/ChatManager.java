package com.phantom.client.manager;

import android.content.Context;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.ConnectionManager;
import com.phantom.client.model.Constants;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.request.C2CMessageResponse;
import com.phantom.client.model.request.C2GMessageResponse;
import com.phantom.client.model.request.FetchMessageResponse;
import com.phantom.client.model.request.InformFetchMessageResponse;
import com.phantom.client.model.request.Message;
import com.phantom.client.model.request.OfflineMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatManager implements MessageHandler {

    private static final String TAG = ChatManager.class.getSimpleName();

    /**
     * 本地最大的sequence
     */
    private volatile long sequence = 0;

    /**
     * 本地最大的时间戳
     */
    private volatile long timestamp = 0;


    public ChatManager(Context context) {

    }

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
            C2CMessageResponse c2CMessageResponse =
                    C2CMessageResponse.parseFrom(body);
            if (c2CMessageResponse.getStatus() == Constants.RESPONSE_STATUS_OK) {
                Log.i(TAG, "发送单聊消息成功...");
            } else {
                Log.i(TAG, "发送单聊消息失败，重新发送...");
            }
        } else if (Constants.REQUEST_TYPE_INFORM_FETCH == requestType) {
            Log.i(TAG, "收到抓取离线消息的通知...");
            InformFetchMessageResponse informFetchMessageResponse =
                    InformFetchMessageResponse.parseFrom(message.getBody());
            String uid = informFetchMessageResponse.getUid();
            fetchMessage(uid);
        } else if (Constants.REQUEST_TYPE_MESSAGE_FETCH == requestType) {
            FetchMessageResponse response = FetchMessageResponse.parseFrom(message.getBody());
            boolean isEmpty = response.getIsEmpty();
            Log.i(TAG, "收到抓取离线消息的响应，是否结果为空：" + isEmpty);
            if (!isEmpty) {
                synchronized (ConnectionManager.class) {
                    List<OfflineMessage> messagesList = response.getMessagesList();
                    for (OfflineMessage msg : messagesList) {
                        // 要求sequence严格递增，但是由于测试阶段没有地方持久化sequence，先注释
                        //if (msg.getSequence() == sequence + 1) {
                        sequence++;
                        timestamp = msg.getTimestamp();
                        handleMessage(msg);
                        // } else {
                        //    log.info("发现获取到的消息sequence不连续，丢弃后续的消息");
                        //    break;
                        //}
                    }
                    fetchMessage(response.getUid());
                }
            }
        } else if (Constants.REQUEST_TYPE_C2G_SEND == requestType) {
            byte[] body = message.getBody();
            C2GMessageResponse c2GMessageResponse =
                    C2GMessageResponse.parseFrom(body);
            if (c2GMessageResponse.getStatus() == Constants.RESPONSE_STATUS_OK) {
                Log.i(TAG, "发送群聊消息成功...");
            } else {
                Log.i(TAG, "发送群聊消息失败，重新发送...");
            }
        }
    }

    private void handleMessage(OfflineMessage msg) {
        Conversation conversation = new Conversation();
        if (msg.getGroupId() != null) {

        }


    }

    @Override
    public Set<Integer> supportMessageTypes() {
        HashSet<Integer> types = new HashSet<>();
        types.add(Constants.REQUEST_TYPE_INFORM_FETCH);
        types.add(Constants.REQUEST_TYPE_MESSAGE_FETCH);
        types.add(Constants.REQUEST_TYPE_C2G_SEND);
        types.add(Constants.REQUEST_TYPE_C2C_SEND);
        return types;
    }
}

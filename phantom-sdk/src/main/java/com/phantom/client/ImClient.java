package com.phantom.client;

import android.content.Context;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.manager.AuthenticateManager;
import com.phantom.client.manager.ChatManager;
import com.phantom.client.model.ChatMessage;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.request.AuthenticateRequest;
import com.phantom.client.model.request.C2CMessageRequest;
import com.phantom.client.model.request.C2GMessageRequest;
import com.phantom.client.model.request.Message;

/**
 * Im客户端启动类
 *
 * @author Jianfeng Wang
 * @since 2019/11/1 12:56
 */
public class ImClient {

    private static ImClient client = new ImClient();

    private ImClient() {
    }

    public static ImClient getInstance() {
        return client;
    }

    private AuthenticateManager authenticateManager;
    private ChatManager chatManager;

    /**
     * 初始化
     */
    public void initialize(Context context, String serverApi) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        connectionManager.initialize(serverApi);
        chatManager = new ChatManager(context);
        authenticateManager = new AuthenticateManager(chatManager);
        connectionManager.setMessageListener(message -> {
            try {
                int requestType = message.getRequestType();
                if (authenticateManager.supportMessageTypes().contains(requestType)) {
                    authenticateManager.onMessage(message);
                } else if (chatManager.supportMessageTypes().contains(requestType)) {
                    chatManager.onMessage(message);
                }
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 发起认证请求
     *
     * @param uid   用户ID
     * @param token 用户Token
     */
    public void authenticate(String uid, String token) {
        ConnectionManager connectManager = ConnectionManager.getInstance();
        AuthenticateRequest authenticateRequest =
                AuthenticateRequest.newBuilder()
                        .setToken(token)
                        .setUid(uid)
                        .build();
        Message message = Message.buildAuthenticateRequest(authenticateRequest);
        connectManager.authenticate(message);
    }

    /**
     * 发送一条消息
     *
     * @param chatMessage 内容
     */
    public void sendMessage(ChatMessage chatMessage) {
        ConnectionManager connectManager = ConnectionManager.getInstance();
        Message message = null;
        if (chatMessage.getType() == Conversation.TYPE_C2C) {
            C2CMessageRequest request = C2CMessageRequest.newBuilder()
                    .setContent(chatMessage.getMessageContent())
                    .setSenderId(chatMessage.getSenderId())
                    .setReceiverId(chatMessage.getReceiverId())
                    .build();
            message = Message.buildC2CMessageRequest(request);
        } else if (chatMessage.getType() == Conversation.TYPE_C2G) {
            C2GMessageRequest request = C2GMessageRequest.newBuilder()
                    .setContent(chatMessage.getMessageContent())
                    .setSenderId(chatMessage.getSenderId())
                    .setGroupId(chatMessage.getReceiverId())
                    .build();
            message = Message.buildC2gMessageRequest(request);
        }
        if (message != null) {
            connectManager.sendMessage(message);
        }
    }

}

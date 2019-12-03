package com.phantom.client.manager;

import android.content.Context;

import com.phantom.client.model.Constants;

import java.util.HashMap;
import java.util.Map;

public class ManagerFactoryBean {

    private static Map<Integer, MessageHandler> messageHandlers = new HashMap<>();

    public static void init(Context context) {
        ChatManager chatManager = new ChatManager(context);
        AuthenticateManager authenticateManager = new AuthenticateManager(chatManager);
        messageHandlers.put(Constants.REQUEST_TYPE_AUTHENTICATE, authenticateManager);
        messageHandlers.put(Constants.REQUEST_TYPE_INFORM_FETCH, chatManager);
        messageHandlers.put(Constants.REQUEST_TYPE_MESSAGE_FETCH, chatManager);
        messageHandlers.put(Constants.REQUEST_TYPE_C2G_SEND, chatManager);
        messageHandlers.put(Constants.REQUEST_TYPE_C2C_SEND, chatManager);
    }

    public static MessageHandler getManager(int requestType) {
        MessageHandler messageHandler = messageHandlers.get(requestType);
        return messageHandler;
    }
}

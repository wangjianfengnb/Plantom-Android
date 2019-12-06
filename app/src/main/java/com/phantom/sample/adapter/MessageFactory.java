package com.phantom.sample.adapter;

import com.phantom.client.model.Conversation;
import com.phantom.client.model.message.Message;

public class MessageFactory {

    public static TextMessage getMessage(Message message, Conversation conversation) {
        if (message.getType() == Message.TEXT) {
            return new TextMessage(message, conversation);
        }
        return null;
    }


}

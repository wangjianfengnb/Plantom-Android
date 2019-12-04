package com.phantom.sample.adapter;

import com.phantom.client.model.message.Message;

public class MessageFactory {

    public static BaseMessage getMessage(Message message) {
        if (message.getType() == Message.TEXT) {
            return new TextMessage(message);
        }
        return null;
    }


}

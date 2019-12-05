package com.phantom.client.manager;

import com.phantom.client.model.message.Message;

public interface OnMessageListener {

    void onMessage(Message message);

    Long conversationId();
}

package com.phantom.sample.adapter;

import android.widget.FrameLayout;

import com.phantom.client.model.Conversation;
import com.phantom.client.model.message.Message;

public abstract class BaseMessage {

    protected Message message;

    protected Conversation conversation;

    public BaseMessage(Message message, Conversation conversation) {
        this.message = message;
        this.conversation = conversation;
    }

    protected abstract void bindView(FrameLayout root);

}

package com.phantom.sample.adapter;

import android.widget.FrameLayout;

import com.phantom.client.model.message.Message;

public abstract class BaseMessage {

    protected Message message;

    public BaseMessage(Message message) {
        this.message = message;
    }

    protected abstract void bindView(FrameLayout root);

}

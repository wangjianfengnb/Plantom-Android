package com.phantom.client.manager;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.model.request.Message;

import java.util.Set;

public interface MessageHandler {
    void onMessage(Message message) throws InvalidProtocolBufferException;
}

package com.phantom.client.manager;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.model.NetworkMessage;

public interface MessageHandler {
    void onMessage(NetworkMessage networkMessage) throws InvalidProtocolBufferException;
}

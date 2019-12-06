package com.phantom.client.manager;

import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.model.Constants;
import com.phantom.client.model.NetworkMessage;
import com.phantom.client.network.ConnectionManager;
import com.phantom.common.AuthenticateResponse;

public class AuthenticateManager implements MessageHandler {

    public static final String TAG = AuthenticateManager.class.getSimpleName();
    private ChatManager chatManager;

    public AuthenticateManager(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @Override
    public void onMessage(NetworkMessage networkMessage) throws InvalidProtocolBufferException {
        byte[] body = networkMessage.getBody();
        AuthenticateResponse authenticateResponse = AuthenticateResponse.parseFrom(body);
        if (authenticateResponse.getStatus() == Constants.RESPONSE_STATUS_OK) {
            ConnectionManager.getInstance().setAuthenticate(true);
            Log.i(TAG, "认证请求成功...开始拉取离线消息");
            chatManager.onAuthenticateSuccess(authenticateResponse.getUid());
        } else {
            Log.i(TAG, "认证请求失败...");
            ConnectionManager.getInstance().shutdown();
        }
    }
}

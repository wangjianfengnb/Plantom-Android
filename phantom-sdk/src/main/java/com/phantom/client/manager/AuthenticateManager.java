package com.phantom.client.manager;

import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.model.Constants;
import com.phantom.client.model.request.AuthenticateResponse;
import com.phantom.client.model.request.Message;
import com.phantom.client.network.ConnectionManager;

public class AuthenticateManager implements MessageHandler {

    public static final String TAG = AuthenticateManager.class.getSimpleName();
    private ChatManager chatManager;

    public AuthenticateManager(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @Override
    public void onMessage(Message message) throws InvalidProtocolBufferException {
        byte[] body = message.getBody();
        AuthenticateResponse authenticateResponse = AuthenticateResponse.parseFrom(body);
        if (authenticateResponse.getStatus() == Constants.RESPONSE_STATUS_OK) {
            ConnectionManager.getInstance().setAuthenticate(true);
            Log.i(TAG, "认证请求成功...开始拉取离线消息");
            chatManager.onAuthenticateSuccess(authenticateResponse.getUid());
        } else {
            Log.i(TAG, "认证请求失败，休眠后再次发送认证请求...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ConnectionManager.getInstance().reauthenticate();
        }
    }
}

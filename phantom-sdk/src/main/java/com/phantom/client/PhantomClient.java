package com.phantom.client;

import android.content.Context;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.manager.IChatManager;
import com.phantom.client.manager.ManagerFactoryBean;
import com.phantom.client.manager.MessageHandler;
import com.phantom.client.model.Constants;
import com.phantom.client.model.NetworkMessage;
import com.phantom.client.network.ConnectionManager;
import com.phantom.common.AuthenticateRequest;

/**
 * Im客户端启动类
 *
 * @author Jianfeng Wang
 * @since 2019/11/1 12:56
 */
public class PhantomClient {

    private static final String TAG = PhantomClient.class.getSimpleName();

    private static PhantomClient client = new PhantomClient();

    private PhantomClient() {
    }

    public static PhantomClient getInstance() {
        return client;
    }

    public IChatManager chatManager() {
        return (IChatManager) ManagerFactoryBean.getManager(Constants.REQUEST_TYPE_C2C_SEND);
    }

    /**
     * 初始化
     */
    public void initialize(Context context, String serverApi) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        connectionManager.initialize(context, serverApi);
        ManagerFactoryBean.init(context, serverApi);
        connectionManager.setMessageListener(message -> {
            try {
                int requestType = message.getRequestType();
                MessageHandler handler = ManagerFactoryBean.getManager(requestType);
                if (handler == null) {
                    Log.i(TAG, "无法找到处理请求的组件：requestType = " + requestType);
                } else {
                    handler.onMessage(message);
                }
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 发起认证请求
     *
     * @param uid   用户ID
     * @param token 用户Token
     */
    public void login(String uid, String token) {
        ConnectionManager connectManager = ConnectionManager.getInstance();
        AuthenticateRequest authenticateRequest =
                AuthenticateRequest.newBuilder()
                        .setToken(token)
                        .setUid(uid)
                        .build();
        NetworkMessage networkMessage = NetworkMessage.buildAuthenticateRequest(authenticateRequest);
        connectManager.authenticate(networkMessage);
    }

    public void logout() {
        ConnectionManager connectManager = ConnectionManager.getInstance();
        connectManager.logout();
    }

}

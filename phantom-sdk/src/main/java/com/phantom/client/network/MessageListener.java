package com.phantom.client.network;

import com.phantom.client.model.NetworkMessage;

/**
 * 消息监听器
 *
 * @author Jianfeng Wang
 * @since 2019/11/18 19:05
 */
public interface MessageListener {

    /**
     * 收到消息
     *
     * @param networkMessage 消息
     */
    void onMessage(NetworkMessage networkMessage);

}

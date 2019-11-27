package com.phantom.client;

import com.phantom.client.model.request.Message;

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
     * @param message 消息
     */
    void onMessage(Message message);

}

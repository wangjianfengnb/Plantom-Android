package com.phantom.client.manager;

import com.phantom.client.model.message.Message;

import java.util.List;

public interface OnLoadMessageListener {

    /**
     * 加载消息
     *
     * @param messages 消息列表
     */
    void onMessage(List<Message> messages);


}

package com.phantom.client.manager;

import com.phantom.client.model.Conversation;

public interface IChatManager {


    /**
     * 加载会话
     *
     * @param page 页码 从1开始
     * @param size 页数
     * @return 会话列表
     */
    void loadConversation(int page, int size, LoadConversationListener listener);

    /**
     * 设置会话变更状态监听器
     *
     * @param listener 会话状态变更
     */
    void addOnConversationChangeListener(OnConversationChangeListener listener);

    /**
     * 加载消息
     *
     * @param conversation 会话列表
     * @param listener     加载消息回调
     */
    void loadMessage(Conversation conversation, OnLoadMessageListener listener);

    /**
     * 关闭会话
     *
     * @param conversationId 会话ID
     */
    void closeConversation(Long conversationId);
}

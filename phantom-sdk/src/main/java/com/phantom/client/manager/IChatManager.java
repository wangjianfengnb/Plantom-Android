package com.phantom.client.manager;

public interface IChatManager {


    /**
     * 加载会话
     *
     * @param page 页码
     * @param size 页数
     * @return 会话列表
     */
    void loadConversation(int page, int size, LoadConversationListener listener);


    void addOnConversationChangeListener(OnConversationChangeListener onConversationChangeListener);
}

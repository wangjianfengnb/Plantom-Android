package com.phantom.client.manager;

import com.phantom.client.model.Conversation;

import java.util.List;

public interface LoadConversationListener {

    void onLoadCompleted(List<Conversation> conversationList);

}

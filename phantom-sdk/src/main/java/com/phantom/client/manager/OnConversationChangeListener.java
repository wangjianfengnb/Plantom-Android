package com.phantom.client.manager;

import com.phantom.client.model.Conversation;

public interface OnConversationChangeListener {

    void onConversationChange(Conversation conversation, boolean isNew);

}

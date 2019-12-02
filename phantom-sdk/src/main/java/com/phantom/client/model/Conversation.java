package com.phantom.client.model;

public class Conversation {

    public static final int TYPE_C2C = 1;

    public static final int TYPE_C2G = 2;

    private long conversationId;

    private int type;

    private String targetId;

    private int unread;

    private String conversationName;

    private ChatMessage lastMessage;

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public TextMessage createTextMessage(String content) {
        TextMessage message = new TextMessage();
        message.setType(type);
        message.setReceiverId(targetId);
        message.setContent(content);
        return message;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }
}

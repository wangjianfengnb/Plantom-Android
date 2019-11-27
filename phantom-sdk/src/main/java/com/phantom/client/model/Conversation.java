package com.phantom.client.model;

public class Conversation {

    public static final int TYPE_C2C = 1;

    public static final int TYPE_C2G = 2;

    private long conversationId;

    private int type;

    private String senderId;

    private String receiverId;

    private int unread;

    private String conversationName;

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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        return message;
    }

}

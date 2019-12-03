package com.phantom.client.model;

import com.phantom.client.model.request.OfflineMessage;

public class ChatMessage {

    public static final int STATUS_WAIT_SEND = 1;
    public static final int STATUS_SENDING = 2;
    public static final int STATUS_SEND_SUCCESS = 3;
    public static final int STATUS_SEND_FAILURE = 4;


    private Long id;

    private Long userId;

    private Long messageId;

    private Integer messageType;

    private String senderId;

    private String receiverId;

    private Integer messageStatus;

    private Long sequence;

    private Long timestamp;

    private String crc;

    private String messageContent;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
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

    public Integer getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Integer messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static ChatMessage parse(Conversation conversation, OfflineMessage msg) {
        ChatMessage message = new ChatMessage();
        message.setSenderId(msg.getSenderId());
        message.setReceiverId(msg.getReceiverId());
        message.setMessageId(msg.getMessageId());
        message.setMessageType(conversation.getConversationType());
        message.setSequence(message.getSequence());
        message.setMessageContent(msg.getContent());
        message.setTimestamp(msg.getTimestamp());
        message.setMessageStatus(STATUS_SEND_SUCCESS);
        message.setCrc("testCrc");
        return message;
    }

}

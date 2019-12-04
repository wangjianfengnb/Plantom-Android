package com.phantom.client.model.message;

public class Message {

    public static final int TEXT = 1;

    public static final int IMAGE = 2;

    public static final int VOICE = 3;

    /**
     * 会话ID
     */
    private Long conversationId;
    /**
     * 消息ID
     */
    private Long messageId;
    /**
     * 发送者ID
     */
    private String senderId;
    /**
     * 接受者ID
     */
    private String receiverId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 唯一标识
     */
    private String crc;

    /**
     * 消息内容
     */
    private String content;

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }
}

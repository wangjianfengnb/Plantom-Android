package com.phantom.client.model.message;

import com.phantom.client.manager.OnStatusChangeListener;

public class Message {

    public static final int TEXT = 1;

    public static final int IMAGE = 2;

    public static final int VOICE = 3;

    public static final int STATUS_WAIT_SEND = 1;
    public static final int STATUS_SENDING = 2;
    public static final int STATUS_SEND_SUCCESS = 3;
    public static final int STATUS_SEND_FAILURE = 4;

    /**
     * 消息ID
     */
    private Long id;

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

    /**
     * 消息类型
     *
     * @see Message#TEXT
     */
    private Integer type;

    /**
     * 会话类型
     */
    private Integer conversationType;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 群组ID
     */
    private String groupId;

    private OnStatusChangeListener listener;

    public void setOnStatusChangeListener(OnStatusChangeListener listener) {
        this.listener = listener;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getConversationType() {
        return conversationType;
    }

    public void setConversationType(Integer conversationType) {
        this.conversationType = conversationType;
    }

    public void invokeListener() {
        if (listener != null) {
            listener.onStatusChange();
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

package com.phantom.client.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.phantom.client.model.message.Message;
import com.phantom.client.utils.StringUtils;

import java.util.Locale;


public class Conversation implements Parcelable {

    public static final int TYPE_C2C = 1;

    public static final int TYPE_C2G = 2;

    private long conversationId;

    private int conversationType;

    /**
     * 单聊情况下:是对方的Id，群聊情况下是群ID
     */
    private String targetId;

    private int unread;

    private String conversationName;

    private long lastUpdate;

    private String lastMessage;

    private String userId;

    private String conversationAvatar;

    protected Conversation(Parcel in) {
        conversationId = in.readLong();
        conversationType = in.readInt();
        targetId = in.readString();
        unread = in.readInt();
        conversationName = in.readString();
        lastUpdate = in.readLong();
        lastMessage = in.readString();
        userId = in.readString();
        conversationAvatar = in.readString();
    }

    public static final Creator<Conversation> CREATOR = new Creator<Conversation>() {
        @Override
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        @Override
        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

    public String getConversationAvatar() {
        return conversationAvatar;
    }

    public void setConversationAvatar(String conversationAvatar) {
        this.conversationAvatar = conversationAvatar;
    }

    public Conversation() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    public Message createTextMessage(String content) {
        Message message = new Message();
        message.setSenderId(userId);
        message.setReceiverId(targetId);
        message.setContent(content);
        message.setStatus(Message.STATUS_SENDING);
        message.setCrc(generateCrc(userId));
        message.setType(Message.TEXT);
        message.setConversationId(conversationId);
        message.setConversationType(conversationType);
        message.setGroupId(conversationType == Conversation.TYPE_C2G ? targetId : "");
        return message;
    }

    private String generateCrc(String uid) {
        String params = String.format(Locale.getDefault(), "%s%s", uid, StringUtils.getTime());
        return StringUtils.MD5(params).toUpperCase().substring(8, 24);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(conversationId);
        dest.writeInt(conversationType);
        dest.writeString(targetId);
        dest.writeInt(unread);
        dest.writeString(conversationName);
        dest.writeLong(lastUpdate);
        dest.writeString(lastMessage);
        dest.writeString(userId);
        dest.writeString(conversationAvatar);
    }
}

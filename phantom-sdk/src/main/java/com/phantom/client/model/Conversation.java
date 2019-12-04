package com.phantom.client.model;

import android.os.Parcel;
import android.os.Parcelable;

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

    public Conversation() {
    }

    protected Conversation(Parcel in) {
        conversationId = in.readLong();
        conversationType = in.readInt();
        targetId = in.readString();
        unread = in.readInt();
        conversationName = in.readString();
        lastUpdate = in.readLong();
        lastMessage = in.readString();
        userId = in.readString();
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
    }
}

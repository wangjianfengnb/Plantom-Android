package com.phantom.client.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phantom.client.model.ChatMessage;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.message.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "chat.db";

    private SQLiteDatabase db;

    public ChatHelper(Context context) {
        super(context, DB_NAME, null, 2);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE conversation(" +
                "conversation_id integer primary key autoincrement," +
                "conversation_name varchar(20)," +
                "user_id integer," +
                "target_id varchar(20)," +
                "conversation_type integer," +
                "unread integer," +
                "last_update integer," +
                "last_message varchar(20)," +
                "conversation_avatar varchar(256)" +
                ");");
        db.execSQL("CREATE TABLE message(" +
                "id integer primary key autoincrement," +
                "user_id varchar(20)," +
                "message_id integer," +
                "sender_id varchar(20)," +
                "receiver_id varchar(20)," +
                "conversation_type integer," +
                "message_content text," +
                "message_status integer," +
                "timestamp integer," +
                "sequence integer," +
                "crc varchar(32)," +
                "group_id varchar(20));");
        db.execSQL("CREATE INDEX idx_target_id ON conversation(target_id);");
        db.execSQL("CREATE INDEX idx_sender_id ON message(sender_id);");
        db.execSQL("CREATE INDEX idx_receiver_id ON message(receiver_id);");
        db.execSQL("CREATE INDEX idx_timestamp ON message(timestamp);");
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * 保存回话
     *
     * @param conversation 回话
     */
    public void saveConversation(Conversation conversation) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("conversation_name", conversation.getConversationName());
        contentValues.put("user_id", conversation.getUserId());
        contentValues.put("target_id", conversation.getTargetId());
        contentValues.put("conversation_type", conversation.getConversationType());
        contentValues.put("unread", conversation.getUnread());
        contentValues.put("last_update", conversation.getLastUpdate());
        contentValues.put("last_message", conversation.getLastMessage());
        contentValues.put("conversation_avatar", conversation.getConversationAvatar());
        long id = this.db.insert("conversation", "conversation_id", contentValues);
        conversation.setConversationId(id);
    }

    /**
     * 根据会话类型和ID找到会话
     *
     * @param conversationType 会话类型
     * @param targetId         目标ID
     * @return 会话
     */
    public Conversation getConversation(String userId, int conversationType, String targetId) {
        Cursor cursor = db.query("conversation", null, "conversation_type=? AND target_id = ? AND user_id = ?",
                new String[]{String.valueOf(conversationType), targetId, userId}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            long conversationId = cursor.getLong(cursor.getColumnIndex("conversation_id"));
            String conversationName = cursor.getString(cursor.getColumnIndex("conversation_name"));
            int unread = cursor.getInt(cursor.getColumnIndex("unread"));
            long lastUpdate = cursor.getLong(cursor.getColumnIndex("last_update"));
            String lastMessage = cursor.getString(cursor.getColumnIndex("last_message"));
            String conversationAvatar = cursor.getString(cursor.getColumnIndex("conversation_avatar"));
            cursor.close();
            Conversation conversation = new Conversation();
            conversation.setTargetId(targetId);
            conversation.setConversationName(conversationName);
            conversation.setConversationId(conversationId);
            conversation.setConversationType(conversationType);
            conversation.setUnread(unread);
            conversation.setLastUpdate(lastUpdate);
            conversation.setLastMessage(lastMessage);
            conversation.setUserId(userId);
            conversation.setConversationAvatar(conversationAvatar);
            return conversation;
        }
        return null;
    }

    public Conversation getConversation(Long conversationId) {
        Cursor cursor = db.query("conversation", null, "conversation_id = ?",
                new String[]{String.valueOf(conversationId)}, null, null, null);
        cursor.moveToFirst();
        String conversationName = cursor.getString(cursor.getColumnIndex("conversation_name"));
        int unread = cursor.getInt(cursor.getColumnIndex("unread"));
        long lastUpdate = cursor.getLong(cursor.getColumnIndex("last_update"));
        String lastMessage = cursor.getString(cursor.getColumnIndex("last_message"));
        String targetId = cursor.getString(cursor.getColumnIndex("target_id"));
        int conversationType = cursor.getInt(cursor.getColumnIndex("conversation_type"));
        String uid = cursor.getString(cursor.getColumnIndex("user_id"));
        String conversationAvatar = cursor.getString(cursor.getColumnIndex("conversation_avatar"));
        cursor.close();
        Conversation conversation = new Conversation();
        conversation.setTargetId(targetId);
        conversation.setConversationName(conversationName);
        conversation.setConversationId(conversationId);
        conversation.setConversationType(conversationType);
        conversation.setUnread(unread);
        conversation.setLastUpdate(lastUpdate);
        conversation.setLastMessage(lastMessage);
        conversation.setUserId(uid);
        conversation.setConversationAvatar(conversationAvatar);
        return conversation;
    }


    /**
     * 更新会话的未读消息数量
     *
     * @param conversation 会话
     */
    public void updateConversation(Conversation conversation) {
        ContentValues values = new ContentValues();
        values.put("unread", conversation.getUnread());
        values.put("last_message", conversation.getLastMessage());
        values.put("last_update", conversation.getLastUpdate());
        db.update("conversation", values, "conversation_id = ?",
                new String[]{String.valueOf(conversation.getConversationId())});
    }

    /**
     * 保存聊天消息
     *
     * @param chatMessage 聊天消息
     */
    public void saveMessage(ChatMessage chatMessage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", chatMessage.getUserId());
        contentValues.put("message_id", chatMessage.getMessageId());
        contentValues.put("sender_id", chatMessage.getSenderId());
        contentValues.put("receiver_id", chatMessage.getReceiverId());
        contentValues.put("conversation_type", chatMessage.getConversationType());
        contentValues.put("message_content", chatMessage.getMessageContent());
        contentValues.put("message_status", chatMessage.getMessageStatus());
        contentValues.put("timestamp", chatMessage.getTimestamp());
        contentValues.put("sequence", chatMessage.getSequence());
        contentValues.put("crc", chatMessage.getCrc());
        contentValues.put("group_id", chatMessage.getGroupId());
        long id = this.db.insert("message", "id", contentValues);
        chatMessage.setId(id);
    }

    /**
     * 获取消息最大时间戳
     *
     * @return 消息最大时间戳
     */
    public long getMaxMessageTimestamp(String userId) {
        Cursor cursor = db.query("message", new String[]{"MAX(timestamp) as timestamp"},
                "user_id=?", new String[]{userId}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
            cursor.close();
            return timestamp;
        }
        return 0;
    }

    /**
     * 获取消息最大序列号
     *
     * @return 消息最大序列号
     */
    public long getMaxSequence(String userId) {
        Cursor cursor = db.query("message ", new String[]{"MAX(sequence) as sequence"},
                "user_id=?", new String[]{userId}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            long sequence = cursor.getLong(cursor.getColumnIndex("sequence"));
            cursor.close();
            return sequence;
        }
        return 0;
    }

    public List<Conversation> loadConversation(String userId, int page, int size) {
        Cursor cursor = db.query("conversation", null, "user_id = ?",
                new String[]{userId}, null, null,
                "last_update DESC limit " + size + " offset " + page * size);
        List<Conversation> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long conversationId = cursor.getLong(cursor.getColumnIndex("conversation_id"));
                String conversationName = cursor.getString(cursor.getColumnIndex("conversation_name"));
                int unread = cursor.getInt(cursor.getColumnIndex("unread"));
                long lastUpdate = cursor.getLong(cursor.getColumnIndex("last_update"));
                String targetId = cursor.getString(cursor.getColumnIndex("target_id"));
                int conversationType = cursor.getInt(cursor.getColumnIndex("conversation_type"));
                String lastMessage = cursor.getString(cursor.getColumnIndex("last_message"));
                String conversationAvatar = cursor.getString(cursor.getColumnIndex("conversation_avatar"));
                Conversation conversation = new Conversation();
                conversation.setTargetId(targetId);
                conversation.setConversationName(conversationName);
                conversation.setConversationId(conversationId);
                conversation.setConversationType(conversationType);
                conversation.setUnread(unread);
                conversation.setLastUpdate(lastUpdate);
                conversation.setLastMessage(lastMessage);
                conversation.setUserId(userId);
                conversation.setConversationAvatar(conversationAvatar);
                result.add(conversation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    /**
     * 加载单聊消息
     *
     * @param userId   用户ID
     * @param targetId 目标ID
     * @param minId    最小消息ID
     * @return 消息列表
     */
    public List<ChatMessage> loadC2CMessage(String userId, String targetId, Long minId) {
        Cursor cursor = db.query("message", null,
                "user_id = ? AND conversation_type = ? AND ((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) AND id < ?",
                new String[]{userId, String.valueOf(Conversation.TYPE_C2C), userId, targetId, targetId, userId, String.valueOf(minId)},
                null, null,
                "id DESC limit 30");
        List<ChatMessage> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                long messageId = cursor.getLong(cursor.getColumnIndex("message_id"));
                String senderId = cursor.getString(cursor.getColumnIndex("sender_id"));
                String receiverId = cursor.getString(cursor.getColumnIndex("receiver_id"));
                int conversationType = cursor.getInt(cursor.getColumnIndex("conversation_type"));
                String messageContent = cursor.getString(cursor.getColumnIndex("message_content"));
                int messageStatus = cursor.getInt(cursor.getColumnIndex("message_status"));
                long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
                long sequence = cursor.getLong(cursor.getColumnIndex("sequence"));
                String crc = cursor.getString(cursor.getColumnIndex("crc"));
                String groupId = cursor.getString(cursor.getColumnIndex("group_id"));
                ChatMessage message = new ChatMessage();
                message.setId(id);
                message.setMessageId(messageId);
                message.setSenderId(senderId);
                message.setReceiverId(receiverId);
                message.setConversationType(conversationType);
                message.setMessageContent(messageContent);
                message.setMessageStatus(messageStatus);
                message.setTimestamp(timestamp);
                message.setCrc(crc);
                message.setGroupId(groupId);
                message.setSequence(sequence);
                result.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    /**
     * 加载单聊消息
     *
     * @param userId   用户ID
     * @param targetId 目标ID
     * @param minId    最小消息ID
     * @return 消息列表
     */
    public List<ChatMessage> loadC2GMessage(String userId, String targetId, Long minId) {
        Cursor cursor = db.query("message", null,
                "user_id = ? AND conversation_type = ? AND group_id = ? AND id < ?",
                new String[]{userId, String.valueOf(Conversation.TYPE_C2G), targetId, String.valueOf(minId)},
                null, null,
                "id DESC limit 30");
        List<ChatMessage> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                long messageId = cursor.getLong(cursor.getColumnIndex("message_id"));
                String senderId = cursor.getString(cursor.getColumnIndex("sender_id"));
                String receiverId = cursor.getString(cursor.getColumnIndex("receiver_id"));
                int conversationType = cursor.getInt(cursor.getColumnIndex("conversation_type"));
                String messageContent = cursor.getString(cursor.getColumnIndex("message_content"));
                int messageStatus = cursor.getInt(cursor.getColumnIndex("message_status"));
                long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
                long sequence = cursor.getLong(cursor.getColumnIndex("sequence"));
                String crc = cursor.getString(cursor.getColumnIndex("crc"));
                String groupId = cursor.getString(cursor.getColumnIndex("group_id"));
                ChatMessage message = new ChatMessage();
                message.setId(id);
                message.setMessageId(messageId);
                message.setSenderId(senderId);
                message.setReceiverId(receiverId);
                message.setConversationType(conversationType);
                message.setMessageContent(messageContent);
                message.setMessageStatus(messageStatus);
                message.setTimestamp(timestamp);
                message.setCrc(crc);
                message.setGroupId(groupId);
                message.setSequence(sequence);
                result.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }


    /**
     * 更新消息
     *
     * @param chatMessage 消息
     */
    public void updateMessage(ChatMessage chatMessage) {
        ContentValues values = new ContentValues();
        values.put("message_status", chatMessage.getMessageStatus());
        values.put("message_id", chatMessage.getMessageId());
        values.put("timestamp", chatMessage.getTimestamp());
        values.put("group_id", chatMessage.getGroupId());
        db.update("message", values, "id = ?",
                new String[]{String.valueOf(chatMessage.getId())});
    }

    public void fireMessageSendingFail(String userId) {
        ContentValues values = new ContentValues();
        values.put("message_status", Message.STATUS_SEND_FAILURE);
        db.update("message", values, "user_id = ? AND message_status = 2", new String[]{String.valueOf(userId)});
    }

}

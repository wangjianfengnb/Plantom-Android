package com.phantom.client.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phantom.client.model.ChatMessage;
import com.phantom.client.model.Conversation;

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
                "last_message varchar(20)" +
                ");");
        db.execSQL("CREATE TABLE message(" +
                "id integer primary key autoincrement," +
                "user_id integer," +
                "message_id integer," +
                "sender_id varchar(20)," +
                "receiver_id varchar(20)," +
                "message_type integer," +
                "message_content text," +
                "message_status integer," +
                "timestamp integer," +
                "sequence integer," +
                "crc varchar(32));");
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
            return conversation;
        }
        return null;
    }

    /**
     * 更新会话的未读消息数量
     *
     * @param conversation 会话
     */
    public void updateConversationUnread(Conversation conversation) {
        ContentValues values = new ContentValues();
        values.put("unread", conversation.getUnread());
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
        contentValues.put("message_type", chatMessage.getMessageType());
        contentValues.put("message_content", chatMessage.getMessageContent());
        contentValues.put("message_status", chatMessage.getMessageStatus());
        contentValues.put("timestamp", chatMessage.getTimestamp());
        contentValues.put("sequence", chatMessage.getSequence());
        contentValues.put("crc", chatMessage.getCrc());
        this.db.insert("message", "id", contentValues);
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
                Conversation conversation = new Conversation();
                conversation.setTargetId(targetId);
                conversation.setConversationName(conversationName);
                conversation.setConversationId(conversationId);
                conversation.setConversationType(conversationType);
                conversation.setUnread(unread);
                conversation.setLastUpdate(lastUpdate);
                conversation.setLastMessage(lastMessage);
                conversation.setUserId(userId);
                result.add(conversation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
}

package com.phantom.client.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phantom.client.model.Conversation;

public class ChatHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "chat.db";
    private SQLiteDatabase db;

    public ChatHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE conversation(" +
                "conversation_id integer primary key autoincrement," +
                "conversation_name varchar(20)," +
                "sender_id varchar(20)," +
                "receiver_id varchar(20)," +
                "type integer," +
                "unread integer" +
                ")");
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveConversation(Conversation conversation){
        ContentValues contentValues = new ContentValues();
        contentValues.put("conversation_name",conversation.getConversationName());
        contentValues.put("sender_id",conversation.getSenderId());
        contentValues.put("receiver_id",conversation.getReceiverId());
        contentValues.put("type",conversation.getType());
        contentValues.put("unread",conversation.getUnread());
        long id = this.db.insert("conversation", "conversatoin_id", contentValues);
        conversation.setConversationId(id);
    }
}

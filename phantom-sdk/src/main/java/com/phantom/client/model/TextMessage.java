package com.phantom.client.model;


import com.alibaba.fastjson.JSONObject;

public class TextMessage extends ChatMessage {

    private String content;

    @Override
    public String getMessageContent() {
        JSONObject obj = new JSONObject();
        obj.put("type", 1);
        obj.put("content", content);
        return obj.toJSONString();
    }

    @Override
    public String getSummary() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

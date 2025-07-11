package com.khanghoang.server.model;

import java.sql.Timestamp;

public class Message {
    private long id;
    private String senderId;
    private String conversationId;
    private String content;
    private Timestamp timestamp;

    public Message() {}

    public Message(long id, String senderId,
                   String conversationId, String content, Timestamp timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.conversationId = conversationId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters
    public long getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getConversationId() { return conversationId; }
    public String getContent() { return content; }
    public Timestamp getTimestamp() { return timestamp; }



}

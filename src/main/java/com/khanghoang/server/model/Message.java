package com.khanghoang.server.model;

import java.sql.Timestamp;

public class Message {
    private long id;
    private int senderId;
    private String senderUsername;
    private int conversationId;
    private String content;
    private Timestamp sentAt;

    public Message() {}

    public Message(long id, int senderId,
                   int conversationId, String content, Timestamp sentAt) {
        this.id = id;
        this.senderId = senderId;
        this.conversationId = conversationId;
        this.content = content;
        this.sentAt = sentAt;
    }

    public long getId() { return id; }
    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }
    public int getConversationId() { return conversationId; }
    public void setConversationId(int conversationId) { this.conversationId = conversationId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Timestamp getSentAt() { return sentAt; }



}

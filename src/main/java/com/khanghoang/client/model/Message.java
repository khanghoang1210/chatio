package com.khanghoang.client.model;


import java.sql.Timestamp;

public class Message {
    private long id;
    private int conversationId;
    private int senderId;
    private String senderUsername;
    private String content;
    private Timestamp sentAt;

    public Message() {}
    public Message(String senderUserName, String content) {
        this.senderUsername = senderUsername;
        this.content = content;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getSenderUsername() { return senderUsername; }
    public String getContent() { return content; }
    public int getConversationId() { return conversationId; }
    public void setConversationId(int conversationId) { this.conversationId = conversationId; }
    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }
    public Timestamp getSentAt() { return sentAt; }
    public void setSentAt(Timestamp sentAt) { this.sentAt = sentAt; }

    public String renderContent(int currentUserId) {
        if (this.senderId == currentUserId) {
            return "You: " + content;
        } else {
            return senderUsername + ": " + content;
        }
    }

}

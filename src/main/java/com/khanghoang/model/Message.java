package com.khanghoang.model;

import java.sql.Timestamp;

public class Message {
    private long id;
    private String type;
    private String senderId;
    private String receiverId;
    private String groupId;
    private String content;
    private Timestamp timestamp;

    public Message() {}

    public Message(long id, String type, String senderId, String receiverId,
                   String groupId, String content, Timestamp timestamp) {
        this.id = id;
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.groupId = groupId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters
    public long getId() { return id; }
    public String getType() { return type; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getGroupId() { return groupId; }
    public String getContent() { return content; }
    public Timestamp getTimestamp() { return timestamp; }

    // Optional: toString() for easy logging
    @Override
    public String toString() {
        return "[" + type + "] " + senderId + " → " +
                (type.equals("PRIVATE") ? receiverId : groupId) +
                ": " + content + " @ " + timestamp;
    }
}

package com.khanghoang.server.dto;

import java.sql.Timestamp;

public class MessageRes {
    public long id;
    public int senderId;
    public String senderUsername;
    public int conversationId;
    public String content;
    public Timestamp sentAt;

    public MessageRes() {}
    public MessageRes(long id, int senderId, String senderUsername, int conversationId, String content, Timestamp sentAt) {
        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.conversationId = conversationId;
        this.content = content;
        this.sentAt = sentAt;
    }
}

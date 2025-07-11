package com.khanghoang.client.model;


public class Message {
    private final String sender;
    private final String content;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() { return sender; }
    public String getContent() { return content; }

    @Override
    public String toString() {
        return sender + ": " + content;
    }
}

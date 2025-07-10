package com.khanghoang.server.model;

import java.util.Date;

public class Conversation {
    private int id;
    private String name;         // nullable for private chat
    private boolean isGroup;
    private int createdBy;
    private Date createdAt;

    public Conversation() {}

    public Conversation(int id, String name, boolean isGroup, int createdBy, Date createdAt) {
        this.id = id;
        this.name = name;
        this.isGroup = isGroup;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Conversation(String name, boolean isGroup, int createdBy, Date createdAt) {
        this.name = name;
        this.isGroup = isGroup;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isGroup() { return isGroup; }
    public void setGroup(boolean group) { isGroup = group; }
    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
}

package com.khanghoang.client.model;

public class Conversation {
    private int id;
    private String name;
    private boolean isGroup;
    private int createdBy;

    public Conversation() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}

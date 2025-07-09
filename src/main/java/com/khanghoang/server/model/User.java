package com.khanghoang.server.model;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private Date createdAt;

    public User() {}
    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

}

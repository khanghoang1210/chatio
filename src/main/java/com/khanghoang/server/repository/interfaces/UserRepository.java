package com.khanghoang.server.repository.interfaces;

import com.khanghoang.server.model.User;

public interface UserRepository {
    void save(User user);
    User getByUsername(String username);
    User getByUserId(int userId);
}

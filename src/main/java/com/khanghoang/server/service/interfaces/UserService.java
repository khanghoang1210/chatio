package com.khanghoang.server.service.interfaces;

import com.khanghoang.server.model.User;

public interface UserService {
    void register(String username);
    User getByUsername(String username);
    User getByUserId(int userId);
}

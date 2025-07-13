package com.khanghoang.server.service;

import com.khanghoang.server.model.User;
import com.khanghoang.server.repository.interfaces.UserRepository;
import com.khanghoang.server.service.interfaces.UserService;

public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void register(String username) {
        User user = new User();
        user.setUsername(username);
        repository.save(user);
    }

    @Override
    public User getByUsername(String username) {
        User user = repository.getByUsername(username);
        return user;
    }

    @Override
    public User getByUserId(int userId) {
        User user = repository.getByUserId(userId);
        return user;
    }
}

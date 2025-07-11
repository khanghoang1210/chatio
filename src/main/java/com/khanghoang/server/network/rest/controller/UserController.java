package com.khanghoang.server.network.rest.controller;

import com.khanghoang.server.model.User;
import com.khanghoang.server.service.UserService;
import io.javalin.http.Context;

public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    public void handleLogin(Context ctx) {
        try {
            String username = ctx.pathParam("username");
            User user = service.getByUsername(username);
            if (user != null) {
                ctx.json(user);
            } else {
                service.register(username);
                User newUser = service.getByUsername(username);
                ctx.json(newUser);
            }
        } catch (Exception e) {
            ctx.status(400).result("Invalid request: " + e.getMessage());
        }
    }
}

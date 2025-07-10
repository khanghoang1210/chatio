package com.khanghoang.server.network.rest.controller;

import com.khanghoang.server.model.User;
import com.khanghoang.server.service.UserService;
import io.javalin.http.Context;

public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    public void handleUserRegistration(Context ctx) {
        try {
            User request = ctx.bodyAsClass(User.class);

            service.register(request.getUsername());

            ctx.status(201).result("User registered: " + request.getUsername());
        } catch (Exception e) {
            ctx.status(400).result("Invalid request: " + e.getMessage());
        }
    }
    public void handleLogin(Context ctx) {
        try {
            String username = ctx.pathParam("username");
            User user = service.getByUsername(username);
            if (user != null) {
                ctx.json(user);
            } else {
                ctx.status(404).result("User not found");
            }
        } catch (Exception e) {
            ctx.status(400).result("Invalid request: " + e.getMessage());
        }
    }
}

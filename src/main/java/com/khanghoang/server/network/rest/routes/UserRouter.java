package com.khanghoang.server.network.rest.routes;

import com.khanghoang.server.network.rest.controller.UserController;
import io.javalin.Javalin;

public class UserRouter {
    public static void register(Javalin app, UserController controller) {
        app.get("/users/{username}", controller::handleLogin);
    }
}

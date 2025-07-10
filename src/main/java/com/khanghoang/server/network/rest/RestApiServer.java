package com.khanghoang.server.network.rest;

import com.khanghoang.server.network.rest.controller.ConversationController;
import com.khanghoang.server.network.rest.controller.UserController;
import io.javalin.Javalin;

public class RestApiServer implements Runnable{
    private final Javalin app;
    private final int port;

    public RestApiServer(int port, UserController userController, ConversationController conversationController) {
        this.port = port;

        app = Javalin.create();

        app.get("/health", ctx -> ctx.result("OK"));

        app.get("/messages", ctx -> {

        });

        app.post("/users", userController::handleUserRegistration);
        app.get("/users/{username}", userController::handleLogin);

        app.post("/conversations", conversationController::handleCreateConversation);
        app.get("/conversations/user/{userId}", conversationController::handleGetConversationsForUser);
        app.get("/conversations/{conversationId}", conversationController::handleGetConversationById);
    }

    @Override
    public void run() {
        System.out.println("REST server started on port: " + port);
        app.start(port);
    }
}

package com.khanghoang.server.network.rest;

import com.khanghoang.server.network.rest.controller.ConversationController;
import com.khanghoang.server.network.rest.controller.MessageController;
import com.khanghoang.server.network.rest.controller.UserController;
import io.javalin.Javalin;

public class RestApiServer implements Runnable{
    private final Javalin app;
    private final int port;

    public RestApiServer(int port, UserController userController, ConversationController conversationController, MessageController messageController) {
        this.port = port;

        app = Javalin.create();

        app.get("/users/{username}", userController::handleLogin);

        app.post("/conversations", conversationController::handleCreateConversation);
        app.get("/conversations/user/{userId}", conversationController::handleGetConversationsForUser);
        app.get("/conversations/{conversationId}", conversationController::handleGetConversationById);

        app.get("/messages/{conversationId}", messageController::handleGetMessagesByConversation);
        app.post("/messages", messageController::handlePostMessage);
    }

    @Override
    public void run() {
        System.out.println("REST server started on port: " + port);
        app.start(port);
    }
}

package com.khanghoang.server.network.rest.routes;

import com.khanghoang.server.network.rest.controller.MessageController;
import io.javalin.Javalin;

public class MessageRouter {
    public static void register(Javalin app, MessageController controller) {
        app.get("/messages/{conversationId}", controller::handleGetMessagesByConversation);
        app.post("/messages", controller::handlePostMessage);
    }
}

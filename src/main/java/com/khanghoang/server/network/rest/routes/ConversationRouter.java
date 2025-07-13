package com.khanghoang.server.network.rest.routes;

import com.khanghoang.server.network.rest.controller.ConversationController;
import io.javalin.Javalin;

public class ConversationRouter {
    public static void register(Javalin app, ConversationController controller) {
        app.post("/conversations", controller::handleCreateConversation);
        app.get("/conversations/user/{userId}", controller::handleGetConversationsForUser);
        app.get("/conversations/{conversationId}", controller::handleGetConversationById);
    }
}

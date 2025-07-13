package com.khanghoang.server.network.rest;

import com.khanghoang.server.network.rest.controller.ConversationController;
import com.khanghoang.server.network.rest.controller.MessageController;
import com.khanghoang.server.network.rest.controller.UserController;
import com.khanghoang.server.network.rest.routes.ConversationRouter;
import com.khanghoang.server.network.rest.routes.MessageRouter;
import com.khanghoang.server.network.rest.routes.UserRouter;
import io.javalin.Javalin;

public class RestApiServer implements Runnable{
    private final Javalin app;
    private final int port;

    public RestApiServer(int port, UserController userController, ConversationController conversationController, MessageController messageController) {
        this.port = port;

        app = Javalin.create();

        UserRouter.register(app, userController);
        ConversationRouter.register(app, conversationController);
        MessageRouter.register(app, messageController);
    }

    @Override
    public void run() {
        System.out.println("REST server started on port: " + port);
        app.start(port);
    }
}

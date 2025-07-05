package com.khanghoang.network.rest;

import com.khanghoang.repository.MessageRepository;
import io.javalin.Javalin;

public class RestApiServer implements Runnable{
    private final Javalin app;
    private final int port;

    public RestApiServer(int port) {
        this.port = port;

        app = Javalin.create();

        app.get("/health", ctx -> ctx.result("OK"));

        app.get("/messages", ctx -> {

        });
    }

    public void start() {
        System.out.println("REST server started on port: " + port);
        app.start(port);
    }
    @Override
    public void run() {
        start();
    }
}

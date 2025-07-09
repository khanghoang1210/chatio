package com.khanghoang.server.network.rest;

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

    @Override
    public void run() {
        System.out.println("REST server started on port: " + port);
        app.start(port);
    }
}

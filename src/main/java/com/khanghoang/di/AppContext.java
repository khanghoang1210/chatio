package com.khanghoang.di;

import com.khanghoang.network.rest.RestApiServer;
import com.khanghoang.network.socket.SocketServer;

public class AppContext {
    private final RestApiServer restServer;
    private final SocketServer socketServer;

    public AppContext() {
        this.restServer = new RestApiServer(8080);
        this.socketServer = new SocketServer(9000);
    }

    public void startAll() {
        new Thread(socketServer).start();
        new Thread(restServer).start();
    }
}

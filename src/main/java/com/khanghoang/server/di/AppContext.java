package com.khanghoang.server.di;

import com.khanghoang.server.database.DatabaseProvider;
import com.khanghoang.server.network.rest.RestApiServer;
import com.khanghoang.server.network.socket.SocketServer;
import com.khanghoang.server.repository.MessageRepository;
import com.khanghoang.server.repository.MessageRepositoryImpl;

import javax.sql.DataSource;

public class AppContext {
    private final RestApiServer restServer;
    private final SocketServer socketServer;
    private final MessageRepository messageRepo;

    public AppContext(){
        DataSource dataSource = DatabaseProvider.getDataSource();
        System.out.println("Connected to database: " + dataSource.toString());
        this.messageRepo = new MessageRepositoryImpl(dataSource);
        this.restServer = new RestApiServer(8080);
        this.socketServer = new SocketServer(9000);
    }

    public void startAll() {
        new Thread(socketServer).start();
        new Thread(restServer).start();
    }
}

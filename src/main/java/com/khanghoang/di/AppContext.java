package com.khanghoang.di;

import com.khanghoang.database.DatabaseProvider;
import com.khanghoang.network.rest.RestApiServer;
import com.khanghoang.network.socket.SocketServer;
import com.khanghoang.repository.MessageRepository;
import com.khanghoang.repository.MessageRepositoryImpl;

import javax.sql.DataSource;
import java.sql.SQLException;

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

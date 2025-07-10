package com.khanghoang.server.di;

import com.khanghoang.server.database.DatabaseProvider;
import com.khanghoang.server.network.rest.RestApiServer;
import com.khanghoang.server.network.rest.controller.UserController;
import com.khanghoang.server.network.socket.SocketServer;
import com.khanghoang.server.repository.MessageRepository;
import com.khanghoang.server.repository.MessageRepositoryImpl;
import com.khanghoang.server.repository.UserRepository;
import com.khanghoang.server.repository.UserRepositoryImpl;
import com.khanghoang.server.service.UserServiceImpl;

import javax.sql.DataSource;

public class AppContext {
    private final RestApiServer restServer;
    private final SocketServer socketServer;

    public AppContext(){
        DataSource dataSource = DatabaseProvider.getDataSource();
        System.out.println("Connected to database: " + dataSource.toString());
        MessageRepository messageRepo = new MessageRepositoryImpl(dataSource);
        UserRepository userRepo = new UserRepositoryImpl(dataSource);
        UserServiceImpl userService = new UserServiceImpl(userRepo);
        UserController userController = new UserController(userService);
        this.restServer = new RestApiServer(8080, userController);
        this.socketServer = new SocketServer(9000);
    }

    public void startAll() {
        new Thread(socketServer).start();
        new Thread(restServer).start();
    }
}

package com.khanghoang.server.di;

import com.khanghoang.server.database.DatabaseProvider;
import com.khanghoang.server.network.rest.RestApiServer;
import com.khanghoang.server.network.rest.controller.ConversationController;
import com.khanghoang.server.network.rest.controller.MessageController;
import com.khanghoang.server.network.rest.controller.UserController;
import com.khanghoang.server.network.socket.SocketServer;
import com.khanghoang.server.repository.*;
import com.khanghoang.server.service.*;

import javax.sql.DataSource;

public class AppContext {
    private final RestApiServer restServer;
    private final SocketServer socketServer;

    public AppContext(){
        DataSource dataSource = DatabaseProvider.getDataSource();
        System.out.println("Connected to database: " + dataSource.toString());
        MessageRepository messageRepo = new MessageRepositoryImpl(dataSource);

        // User DI
        UserRepository userRepo = new UserRepositoryImpl(dataSource);
        UserServiceImpl userService = new UserServiceImpl(userRepo);
        UserController userController = new UserController(userService);

        // Conversation DI
        ConversationRepository conversationRepository = new ConversationRepostoryImpl(dataSource);
        ParticipantRepository participantRepository = new ParticipantRepositoryImpl(dataSource);
        ConversationService conversationService = new ConversationServiceImpl(conversationRepository, participantRepository, userRepo);
        ConversationController conversationController = new ConversationController(conversationService);

        MessageRepository messageRepository = new MessageRepositoryImpl(dataSource);
        MessageService messageService = new MessageServiceImpl(messageRepository);
        MessageController messageController = new MessageController(messageService);

        this.restServer = new RestApiServer(8080, userController, conversationController, messageController);
        this.socketServer = new SocketServer(9000);
    }

    public void startAll() {
        new Thread(socketServer).start();
        new Thread(restServer).start();
    }
}

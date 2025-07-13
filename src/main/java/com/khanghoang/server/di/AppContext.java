package com.khanghoang.server.di;

import com.khanghoang.server.database.DatabaseProvider;
import com.khanghoang.server.network.rest.RestApiServer;
import com.khanghoang.server.network.rest.controller.ConversationController;
import com.khanghoang.server.network.rest.controller.MessageController;
import com.khanghoang.server.network.rest.controller.UserController;
import com.khanghoang.server.network.socket.SocketServer;
import com.khanghoang.server.processor.DefaultMessageProcessor;
import com.khanghoang.server.processor.MessageProcessor;
import com.khanghoang.server.repository.ConversationRepostoryImpl;
import com.khanghoang.server.repository.MessageRepositoryImpl;
import com.khanghoang.server.repository.ParticipantRepositoryImpl;
import com.khanghoang.server.repository.UserRepositoryImpl;
import com.khanghoang.server.repository.interfaces.ConversationRepository;
import com.khanghoang.server.repository.interfaces.MessageRepository;
import com.khanghoang.server.repository.interfaces.ParticipantRepository;
import com.khanghoang.server.repository.interfaces.UserRepository;
import com.khanghoang.server.service.ConversationServiceImpl;
import com.khanghoang.server.service.MessageServiceImpl;
import com.khanghoang.server.service.UserServiceImpl;
import com.khanghoang.server.service.interfaces.ConversationService;
import com.khanghoang.server.service.interfaces.MessageService;
import com.khanghoang.server.service.interfaces.UserService;

import javax.sql.DataSource;

public class AppContext {
    private final RestApiServer restServer;
    private final SocketServer socketServer;

    public AppContext(){
        DataSource dataSource = DatabaseProvider.getDataSource();
        System.out.println("Connected to database: " + dataSource.toString());

        // User DI
        UserRepository userRepo = new UserRepositoryImpl(dataSource);
        UserService userService = new UserServiceImpl(userRepo);
        UserController userController = new UserController(userService);

        // Conversation DI
        ConversationRepository conversationRepository = new ConversationRepostoryImpl(dataSource);
        ParticipantRepository participantRepository = new ParticipantRepositoryImpl(dataSource);
        ConversationService conversationService = new ConversationServiceImpl(conversationRepository, participantRepository, userRepo);
        ConversationController conversationController = new ConversationController(conversationService);

        MessageRepository messageRepository = new MessageRepositoryImpl(dataSource);
        MessageService messageService = new MessageServiceImpl(messageRepository);
        MessageController messageController = new MessageController(messageService);

        MessageProcessor processor = new DefaultMessageProcessor(messageService);

        this.restServer = new RestApiServer(8080, userController, conversationController, messageController);
        this.socketServer = new SocketServer(9000, processor);
    }

    public void startAll() {
        new Thread(socketServer).start();
        new Thread(restServer).start();
    }
}

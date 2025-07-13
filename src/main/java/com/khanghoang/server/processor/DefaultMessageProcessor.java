package com.khanghoang.server.processor;

import com.khanghoang.server.model.Message;
import com.khanghoang.server.service.interfaces.MessageService;

public class DefaultMessageProcessor implements MessageProcessor{
    private final MessageService messageService;

    public DefaultMessageProcessor(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void handleIncomingMessage(Message message){
        messageService.saveMessage(message);
    }

}

package com.khanghoang.server.service;

import com.khanghoang.server.dto.MessageRes;
import com.khanghoang.server.model.Message;
import com.khanghoang.server.repository.interfaces.MessageRepository;
import com.khanghoang.server.service.interfaces.MessageService;

import java.util.List;

public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void saveMessage(Message msg) {
        messageRepository.save(msg);
    }

    @Override
    public List<MessageRes> getMessagesByConversationId(String conversationId) {
        return messageRepository.getMessagesInConversation(conversationId);
    }
}

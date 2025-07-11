package com.khanghoang.server.service;

import com.khanghoang.protocol.MessageFrame;
import com.khanghoang.server.model.Message;
import com.khanghoang.server.repository.MessageRepository;

import java.util.List;

public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void saveMessage(MessageFrame frame) {
        messageRepository.save(frame);
    }

    @Override
    public List<Message> getMessagesByConversationId(String conversationId) {
        return messageRepository.getMessagesInConversation(conversationId);
    }
}

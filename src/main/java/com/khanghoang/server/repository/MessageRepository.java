package com.khanghoang.server.repository;

import com.khanghoang.server.model.Message;
import com.khanghoang.protocol.MessageFrame;

import java.util.List;

public interface MessageRepository {
    void save(MessageFrame message);
    List<Message> getMessagesInConversation(String conversationId);
}

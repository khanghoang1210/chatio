package com.khanghoang.server.service;

import com.khanghoang.protocol.MessageFrame;
import com.khanghoang.server.model.Message;

import java.util.List;

public interface MessageService {
    void saveMessage(MessageFrame frame);

    List<Message> getMessagesByConversationId(String conversationId);
}

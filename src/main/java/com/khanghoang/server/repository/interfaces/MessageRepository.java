package com.khanghoang.server.repository.interfaces;

import com.khanghoang.server.dto.MessageRes;
import com.khanghoang.server.model.Message;

import java.util.List;

public interface MessageRepository {
    void save(Message message);
    List<MessageRes> getMessagesInConversation(String conversationId);
}

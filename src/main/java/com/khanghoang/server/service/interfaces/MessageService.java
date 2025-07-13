package com.khanghoang.server.service.interfaces;

import com.khanghoang.server.dto.MessageRes;
import com.khanghoang.server.model.Message;

import java.util.List;

public interface MessageService {
    void saveMessage(Message frame);

    List<MessageRes> getMessagesByConversationId(String conversationId);
}

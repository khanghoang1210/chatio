package com.khanghoang.server.repository;

import com.khanghoang.server.model.Message;
import com.khanghoang.protocol.MessageFrame;

import java.util.List;

public interface MessageRepository {
    void save(MessageFrame message);
    List<Message> getMessagesForUser(String userId);
    List<Message> getMessagesInGroup(String groupId);
}

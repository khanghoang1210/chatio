package com.khanghoang.server.service;

import com.khanghoang.server.model.Conversation;

import java.util.List;

public interface ConversationService {

    Conversation createConversation(String name, boolean isGroup, int createdBy, String username);

    List<Conversation> getConversationsForUser(int userId);

    Conversation getConversationById(int conversationId);

    List<Conversation> getGroupConversationsForUser(int userId);

    // Thêm người dùng vào cuộc trò chuyện
    void addParticipant(int conversationId, int userId);
}

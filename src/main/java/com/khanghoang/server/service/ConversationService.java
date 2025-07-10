package com.khanghoang.server.service;

import com.khanghoang.server.model.Conversation;

import java.util.List;

public interface ConversationService {

    // Tạo cuộc trò chuyện mới (có thể là group hoặc one-on-one)
    Conversation createConversation(String name, boolean isGroup, int createdBy, List<Integer> participantIds);

    // Lấy danh sách cuộc trò chuyện mà user đang tham gia
    List<Conversation> getConversationsForUser(int userId);

    // Lấy chi tiết 1 conversation theo ID
    Conversation getConversationById(int conversationId);

    // Lấy danh sách các conversation là group chat mà user thuộc về
    List<Conversation> getGroupConversationsForUser(int userId);

    // Thêm người dùng vào cuộc trò chuyện
    void addParticipant(int conversationId, int userId);
}

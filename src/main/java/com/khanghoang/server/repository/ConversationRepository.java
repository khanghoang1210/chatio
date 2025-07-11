package com.khanghoang.server.repository;

import com.khanghoang.server.model.Conversation;

import java.util.List;

public interface ConversationRepository {
    int save(Conversation conversation);
    List<Conversation> getConversationsForUser(String userId);
    List<Conversation> getConversationsInGroup(String groupId);
    Conversation getConversationById(String conversationId);
    int getLatestInsertedId();
}

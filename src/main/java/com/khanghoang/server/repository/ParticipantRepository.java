package com.khanghoang.server.repository;

public interface ParticipantRepository {
    void addParticipant(int conversationId, int userId);
}

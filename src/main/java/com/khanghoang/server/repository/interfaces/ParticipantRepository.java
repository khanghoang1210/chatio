package com.khanghoang.server.repository.interfaces;

import java.util.List;

public interface ParticipantRepository {
    void addParticipant(int conversationId, int userId);
    List<Integer> getParticipantIds(int conversationId);

}

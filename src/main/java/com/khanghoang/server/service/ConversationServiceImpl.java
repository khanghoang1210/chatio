package com.khanghoang.server.service;

import com.khanghoang.server.model.Conversation;
import com.khanghoang.server.repository.ConversationRepository;
import com.khanghoang.server.repository.ParticipantRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ParticipantRepository participantRepository;

    public ConversationServiceImpl(ConversationRepository conversationRepository, ParticipantRepository participantRepository) {
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
    }

    @Override
    public Conversation createConversation(String name, boolean isGroup, int createdBy, List<Integer> participantIds) {
        Conversation conversation = new Conversation();
        conversation.setName(name);
        conversation.setGroup(isGroup);
        conversation.setCreatedBy(createdBy);

        conversationRepository.save(conversation);
        int conversationId = conversationRepository.getLatestInsertedId(); // bạn cần implement thêm hàm này nếu chưa có

        // Thêm người tạo + các participants khác
        List<Integer> allParticipants = new ArrayList<>(participantIds);
        if (!allParticipants.contains(createdBy)) {
            allParticipants.add(createdBy);
        }

        for (int userId : allParticipants) {
            if (userId == createdBy) continue;
            participantRepository.addParticipant(conversationId, userId);
        }

        conversation.setId(conversationId);
        return conversation;
    }

    @Override
    public List<Conversation> getConversationsForUser(int userId) {
        return conversationRepository.getConversationsForUser(String.valueOf(userId));
    }

    @Override
    public Conversation getConversationById(int conversationId) {
        return conversationRepository.getConversationById(String.valueOf(conversationId));
    }

    @Override
    public List<Conversation> getGroupConversationsForUser(int userId) {
        List<Conversation> all = getConversationsForUser(userId);
        List<Conversation> groups = new ArrayList<>();
        for (Conversation c : all) {
            if (c.isGroup()) groups.add(c);
        }
        return groups;
    }

    @Override
    public void addParticipant(int conversationId, int userId) {
        participantRepository.addParticipant(conversationId, userId);
    }


}

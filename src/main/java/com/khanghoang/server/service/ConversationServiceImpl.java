package com.khanghoang.server.service;

import com.khanghoang.server.model.Conversation;
import com.khanghoang.server.model.User;
import com.khanghoang.server.repository.ConversationRepository;
import com.khanghoang.server.repository.ParticipantRepository;
import com.khanghoang.server.repository.UserRepository;
import com.khanghoang.server.repository.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public ConversationServiceImpl(ConversationRepository conversationRepository, ParticipantRepository participantRepository, UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Conversation createConversation(String name, boolean isGroup, int createdBy, String username) {
        // Tạo conversation mới
        Conversation conversation = new Conversation();
        conversation.setName(name);
        conversation.setGroup(isGroup);
        conversation.setCreatedBy(createdBy);

        User otherUser = userRepository.getByUsername(username);
        if(otherUser == null) {
            return null;
        }

        int id = conversationRepository.save(conversation);

        participantRepository.addParticipant(id, createdBy);


        if (otherUser != null && otherUser.getId() != createdBy) {
            participantRepository.addParticipant(id, otherUser.getId());
        }

        conversation.setId(id);
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

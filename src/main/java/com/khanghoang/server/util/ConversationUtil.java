package com.khanghoang.server.util;

import com.khanghoang.server.database.DatabaseProvider;
import com.khanghoang.server.repository.interfaces.ParticipantRepository;
import com.khanghoang.server.repository.ParticipantRepositoryImpl;

import javax.sql.DataSource;
import java.util.List;

public class ConversationUtil {
    private static final DataSource dataSource = DatabaseProvider.getDataSource();
    private static final ParticipantRepository participantRepository = new ParticipantRepositoryImpl(dataSource);

    public static List<String> getParticipantUserIds(String roomId) {
        int conversationId = Integer.parseInt(roomId);
        List<Integer> userIds = participantRepository.getParticipantIds(conversationId);
        return userIds.stream().map(String::valueOf).toList();
    }
}


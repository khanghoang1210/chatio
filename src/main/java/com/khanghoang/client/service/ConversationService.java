package com.khanghoang.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khanghoang.client.constants.Constants;
import com.khanghoang.client.model.Conversation;
import com.khanghoang.client.network.HttpClient;

import java.io.InputStream;
import java.util.List;


public class ConversationService {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String conversationEndpoint = "/conversations";

    public void createGroupConversation(String groupName, int creatorId, String participantUsername) throws Exception {
        String body = String.format("""
            {
                "name": "%s",
                "createdBy": %d,
                "isGroup": true,
                "participant": "%s"
            }
            """, groupName, creatorId, participantUsername);

        HttpClient.post(Constants.BASE_URL + conversationEndpoint, body);
    }

    public List<Conversation> getConversationsForUser(int userId) throws Exception {
        String url = Constants.BASE_URL + conversationEndpoint + "/user/" + userId;
        try (InputStream input = HttpClient.get(url)) {
            return mapper.readValue(input, new TypeReference<>() {});
        }
    }
}

package com.khanghoang.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khanghoang.client.constants.Constants;
import com.khanghoang.client.model.Message;
import com.khanghoang.client.network.HttpClient;

import java.io.InputStream;
import java.util.List;

public class MessageService {
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String endpoint = "/messages";

    public List<Message> getMessagesByConversationId(int conversationId) throws Exception {
        String url = Constants.BASE_URL + endpoint + "/" + conversationId;
        try (InputStream input = HttpClient.get(url)) {
            return mapper.readValue(input, new TypeReference<>() {});
        }
    }
}

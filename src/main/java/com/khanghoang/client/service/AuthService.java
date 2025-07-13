package com.khanghoang.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khanghoang.client.constants.Constants;
import com.khanghoang.client.model.User;
import com.khanghoang.client.network.HttpClient;

public class AuthService {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String userEndpoint = "/users";

    public User login(String username) throws Exception {
        String url = Constants.BASE_URL + userEndpoint + "/" + username;
        return mapper.readValue(HttpClient.get(url), User.class);
    }
}

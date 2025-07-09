package com.khanghoang.client.repository;


import com.khanghoang.client.network.ChatClient;
import com.khanghoang.protocol.MessageFrame;

public class ChatRepository {
    private final ChatClient client;

    public ChatRepository(ChatClient client) {
        this.client = client;
    }

    public void sendChat(String from, String to, String roomId, String content) throws Exception {
        MessageFrame frame = new MessageFrame("private", from, to, roomId, System.currentTimeMillis(), content);
        client.sendMessage(frame);
    }
}



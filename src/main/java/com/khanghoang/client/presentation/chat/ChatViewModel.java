package com.khanghoang.client.presentation.chat;


import com.khanghoang.client.repository.ChatRepository;

public class ChatViewModel {
    private final ChatRepository repository;

    public ChatViewModel(ChatRepository repository) {
        this.repository = repository;
    }

    public void sendMessage(String from,String senderName, String to, String roomId, String content) {
        try {
            repository.sendChat(from, senderName, to, roomId, content);
        } catch (Exception e) {
            e.printStackTrace(); // hoặc báo lỗi lên UI
        }
    }
}


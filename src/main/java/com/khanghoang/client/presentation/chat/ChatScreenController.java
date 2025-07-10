package com.khanghoang.client.presentation.chat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatScreenController {

    @FXML private TextField messageInput;
    @FXML private Button sendButton;
    @FXML private ListView<String> chatList;

    private ChatViewModel viewModel;
    private String currentUsername;

    public void setUsername(String username) {
        this.currentUsername = username;
    }
    @FXML
    public void initialize() {
        var client = new com.khanghoang.client.network.ChatClient();
        try {
            client.connect("localhost", 9000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        var repo = new com.khanghoang.client.repository.ChatRepository(client);
        this.viewModel = new ChatViewModel(repo);

        // Gửi message
        sendButton.setOnAction(event -> {
            String message = messageInput.getText();
            if (!message.isEmpty()) {
                viewModel.sendMessage(currentUsername, "Bob", "room-1", message);
                chatList.getItems().add("Me: " + message);
                messageInput.clear();
            }
        });

        // Lắng nghe message từ server
        client.startReceiving(frame -> {
            String from = frame.from;
            String content = frame.content;

            Platform.runLater(() -> {
                chatList.getItems().add(from + ": " + content);
            });
        });
    }

    @FXML
    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            chatList.getItems().add("You: " + message);
            messageInput.clear();
        }
    }
}

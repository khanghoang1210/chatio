package com.khanghoang.client.presentation.chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khanghoang.client.model.Conversation;
import com.khanghoang.client.model.Message;
import com.khanghoang.client.model.User;
import com.khanghoang.client.network.ChatClient;
import com.khanghoang.protocol.MessageFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ChatScreenController {

    @FXML private TextField messageInput;
    @FXML private Button sendButton;
    @FXML private ListView<String> chatList;
    @FXML private ListView<String> conversationList;
    @FXML private TextField groupNameInput;
    @FXML private TextField userToAddInput;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Integer> conversationMap = new HashMap<>();
    private int currentConversationId = -1;

    private User currentUser;
    private ChatClient client;

    public void setUser(User user) {
        this.currentUser = user;
        this.client = new ChatClient();

        connectSocketAndRegister(user.getId());
        loadConversations();
    }

    private void connectSocketAndRegister(int userId) {
        try {
            client.connect("localhost", 9000); // socket server port
            MessageFrame registerFrame = new MessageFrame();
            registerFrame.setFrom(String.valueOf(userId));
            registerFrame.setSenderName(currentUser.getUsername());
            client.sendMessage(registerFrame);
            System.out.println("[CLIENT] Registered with server as: " + userId);

            client.startReceiving(this::handleIncomingMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleIncomingMessage(MessageFrame frame) {
        System.out.println("[CLIENT RECEIVED] " + frame.getContent() + " from " + frame.getFrom());

        if (frame.getRoomId() != null && frame.getRoomId().equals(String.valueOf(currentConversationId))) {
            Platform.runLater(() -> {
                chatList.getItems().add(frame.getSenderName() + ": " + frame.getContent());
            });
        }
    }

    @FXML
    public void initialize() {
        conversationList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleConversationClick();
            }
        });
    }

    @FXML
    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (message.isEmpty()) return;

        if (currentConversationId == -1) {
            System.out.println("No conversation selected.");
            return;
        }

        MessageFrame frame = new MessageFrame();
        frame.setFrom(String.valueOf(currentUser.getId()));
        frame.setSenderName(currentUser.getUsername());
        frame.setRoomId(String.valueOf(currentConversationId));
        frame.setContent(message);

        try {
            client.sendMessage(frame);
            chatList.getItems().add("You: " + message);
            messageInput.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreateConversation() {
        String groupName = groupNameInput.getText().trim();
        String usernameToAdd = userToAddInput.getText().trim();

        if (groupName.isEmpty() || usernameToAdd.isEmpty()) {
            System.out.println("Group name and user are required");
            return;
        }

        try {
            URL url = new URL("http://localhost:8080/conversations");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String body = String.format("""
            {
                "name": "%s",
                "createdBy": %d,
                "isGroup": true,
                "participant": "%s"
            }
            """, groupName, currentUser.getId(), usernameToAdd);

            conn.getOutputStream().write(body.getBytes());

            int code = conn.getResponseCode();
            if (code == 200 || code == 201) {
                System.out.println("Conversation created");
                conversationList.getItems().clear();
                conversationMap.clear();
                loadConversations();
            } else {
                System.out.println("Failed to create conversation");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadConversations() {
        try {
            URL url = new URL("http://localhost:8080/conversations/user/" + currentUser.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            InputStream is = conn.getInputStream();
            List<Conversation> conversations = objectMapper.readValue(is, new TypeReference<>() {});
            for (Conversation conv : conversations) {
                conversationList.getItems().add(conv.getName());
                conversationMap.put(conv.getName(), conv.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConversationClick() {
        String selected = conversationList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Integer conversationId = conversationMap.get(selected);
            if (conversationId != null) {
                currentConversationId = conversationId;
                loadMessages(conversationId);
            }
        }
    }

    private void loadMessages(int conversationId) {

    }
}

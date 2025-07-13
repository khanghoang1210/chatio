package com.khanghoang.client.presentation.chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khanghoang.client.model.Conversation;
import com.khanghoang.client.model.Message;
import com.khanghoang.client.model.User;
import com.khanghoang.client.network.SocketClient;
import com.khanghoang.client.service.ConversationService;
import com.khanghoang.client.service.MessageService;
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

    private final Map<Integer, List<String>> messageMap = new HashMap<>();
    private final Map<String, Integer> conversationMap = new HashMap<>(); // name -> id
    private final Map<Integer, String> conversationIdToName = new HashMap<>(); // id -> name
    private final Map<Integer, Integer> unreadCountMap = new HashMap<>();
    private final ConversationService conversationService = new ConversationService();
    private final MessageService messageService = new MessageService();

    private int currentConversationId = -1;
    private User currentUser;
    private SocketClient client;

    public void setUser(User user) {
        this.currentUser = user;
        this.client = new SocketClient();

        connectSocketAndRegister(user.getId());
        loadConversations();
    }

    private void connectSocketAndRegister(int userId) {
        try {
            client.connect("localhost", 9000);

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
        int conversationId = Integer.parseInt(frame.getRoomId());
        String formatted = frame.getSenderName() + ": " + frame.getContent();
        System.out.println("Received: [" + frame.getContent() + "] from " + frame.getSenderName());


        // Lưu vào messageMap
        messageMap.computeIfAbsent(conversationId, k -> new ArrayList<>()).add(formatted);

        if (conversationId == currentConversationId) {
            Platform.runLater(() -> chatList.getItems().add(frame.getSenderName() + ": " + frame.getContent()));
        } else {
            unreadCountMap.put(conversationId, unreadCountMap.getOrDefault(conversationId, 0) + 1);
            Platform.runLater(this::updateConversationListUI);
        }
    }

    private void updateConversationListUI() {
        conversationList.getItems().clear();
        for (Map.Entry<String, Integer> entry : conversationMap.entrySet()) {
            String name = entry.getKey();
            int id = entry.getValue();
            int unread = unreadCountMap.getOrDefault(id, 0);
            String display = unread > 0 ? name + " (" + unread + " new)" : name;
            conversationList.getItems().add(display);
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
        if (message.isEmpty() || currentConversationId == -1) {
            System.out.println("No message or no conversation selected.");
            return;
        }

        MessageFrame frame = new MessageFrame();
        frame.setFrom(String.valueOf(currentUser.getId()));
        frame.setSenderName(currentUser.getUsername());
        frame.setRoomId(String.valueOf(currentConversationId));
        frame.setContent(message);

        try {
            client.sendMessage(frame);
            String formatted = "You: " + message;
            messageMap.computeIfAbsent(currentConversationId, k -> new ArrayList<>()).add(formatted);
            chatList.getItems().add(formatted);
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
            conversationService.createGroupConversation(groupName, currentUser.getId(), usernameToAdd);
            System.out.println("Conversation created");
            loadConversations();
        } catch (Exception e) {
            System.out.println("Failed to create conversation");
            e.printStackTrace();
        }
    }

    private void loadConversations() {
        try {
            List<Conversation> conversations = conversationService.getConversationsForUser(currentUser.getId());

            conversationMap.clear();
            conversationIdToName.clear();

            for (Conversation conv : conversations) {
                conversationMap.put(conv.getName(), conv.getId());
                conversationIdToName.put(conv.getId(), conv.getName());
            }

            updateConversationListUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConversationClick() {
        String selected = conversationList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String baseName = selected.replaceAll(" \\(\\d+ new\\)", "");
            Integer conversationId = conversationMap.get(baseName);

            if (conversationId != null) {
                currentConversationId = conversationId;
                unreadCountMap.remove(conversationId);
                updateConversationListUI();
                loadMessages(conversationId);
            }
        }
    }

    private void loadMessages(int conversationId) {
        new Thread(() -> {
            try {
                List<Message> messages = messageService.getMessagesByConversationId(conversationId);
                Platform.runLater(() -> {
                    chatList.getItems().clear();
                    for (Message msg : messages) {
                        chatList.getItems().add(msg.renderContent(currentUser.getId()));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

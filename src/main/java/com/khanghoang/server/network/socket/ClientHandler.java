package com.khanghoang.server.network.socket;

import com.khanghoang.protocol.MessageFrame;
import com.khanghoang.server.util.ConversationUtil;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private String userId;
    private OutputStream output;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream()
        ) {
            this.output = socket.getOutputStream();

            while (true) {
                byte[] lengthBytes = input.readNBytes(4);
                if (lengthBytes.length < 4) break;

                int messageLength = byteArrayToInt(lengthBytes);
                byte[] rawMessage = input.readNBytes(messageLength);
                if (rawMessage.length < messageLength) break;

                MessageFrame message = MessageFrame.decode(rawMessage);

                if (message.getRoomId() == null || message.getRoomId().isEmpty()) {
                    System.out.println("[WARNING] Received message without roomId, skipping...");
                    continue;
                }

                // Nếu chưa đăng ký userId, thì đây là message đầu tiên
                if (userId == null) {
                    userId = message.getFrom();
                    if (userId == null || userId.isEmpty()) {
                        System.out.println("Missing userId in first message. Ignoring...");
                        continue;
                    }
                    ClientManager.registerClient(userId, output);
                    System.out.println("User " + userId + " connected.");
                }

                System.out.println("[RECEIVED] " + message.getContent() + " from " + message.getFrom() + " in room " + message.getRoomId());

                List<String> participants = ConversationUtil.getParticipantUserIds(message.getRoomId());
                ClientManager.broadcastToRoom(message.getRoomId(), message.getFrom(), rawMessage, participants);
            }

        } catch (IOException e) {
            System.out.println("Client " + userId + " disconnected: " + e.getMessage());
        } finally {
            if (userId != null) {
                ClientManager.removeClient(userId);
            }
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private int byteArrayToInt(byte[] bytes) {
        return (bytes[0] & 0xFF) << 24 |
                (bytes[1] & 0xFF) << 16 |
                (bytes[2] & 0xFF) << 8 |
                (bytes[3] & 0xFF);
    }
}

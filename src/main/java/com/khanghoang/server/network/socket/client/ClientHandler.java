package com.khanghoang.server.network.socket.client;

import com.khanghoang.protocol.MessageFrame;
import com.khanghoang.server.model.Message;
import com.khanghoang.server.processor.MessageProcessor;
import com.khanghoang.server.util.ConversationUtil;
import com.khanghoang.server.util.Converter;

import java.io.*;
import java.net.Socket;
import java.util.List;


public class ClientHandler implements Runnable {
    private final Socket socket;
    private String userId;
    private OutputStream output;
    private MessageProcessor processor;

    public ClientHandler(Socket socket, MessageProcessor processor) {
        this.socket = socket;
        this.processor = processor;
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

                int messageLength = Converter.byteArrayToInt(lengthBytes);
                byte[] rawMessage = input.readNBytes(messageLength);
                if (rawMessage.length < messageLength) break;

                MessageFrame message = MessageFrame.decode(rawMessage);


                if (userId == null) {
                    userId = message.getFrom();
                    if (userId == null || userId.isEmpty()) {
                        System.out.println("Missing userId in first message. Ignoring...");
                        continue;
                    }
                    ClientManager.registerClient(userId, output);
                    System.out.println("User " + userId + " connected.");
                }

                if (message.getRoomId() == null || message.getRoomId().isEmpty()) {
                    System.out.println("[WARNING] Received message without roomId, skipping...");
                    continue;
                }
                Message newMsg = new Message();
                newMsg.setSenderId(Integer.parseInt(message.getFrom()));
                newMsg.setConversationId(Integer.parseInt(message.getRoomId()));
                newMsg.setContent(message.getContent());

                System.out.println("[RECEIVED] " + message.getContent() + " from " + message.getFrom() + " in room " + message.getRoomId());
                processor.handleIncomingMessage(newMsg);

                List<String> participants = ConversationUtil.getParticipantUserIds(message.getRoomId());
                ClientManager.broadcastToRoom(message.getFrom(), rawMessage, participants);
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


}

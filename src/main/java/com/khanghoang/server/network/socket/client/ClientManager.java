package com.khanghoang.server.network.socket.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.khanghoang.server.util.Converter.intToByteArray;

public class ClientManager {
    private static final Map<String, OutputStream> clientMap = new ConcurrentHashMap<>();

    public static void registerClient(String userId, OutputStream out) {
        clientMap.put(userId, out);
    }

    public static void removeClient(String userId) {
        clientMap.remove(userId);
    }

    public static void broadcastToRoom(String senderId, byte[] message, List<String> participants) {
        System.out.println("Broadcasting to participants: " + participants);

        for (String userId : participants) {
            if (!userId.equals(senderId)) {
                OutputStream out = clientMap.get(userId);
                if (out != null) {
                    try {
                        byte[] lengthBytes = intToByteArray(message.length);
                        out.write(lengthBytes);
                        out.write(message);
                        out.flush();
                    } catch (IOException e) {
                        System.out.println("Failed to send to userId " + userId + ": " + e.getMessage());
                    }
                }
            }
        }
    }


}


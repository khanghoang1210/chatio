package com.khanghoang.server.network.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
    private static final Map<String, OutputStream> clientMap = new ConcurrentHashMap<>();

    public static void registerClient(String userId, OutputStream out) {
        clientMap.put(userId, out);
    }

    public static void removeClient(String userId) {
        clientMap.remove(userId);
    }

    public static void broadcastToRoom(String roomId, String senderId, byte[] message, List<String> participants) {
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

    private static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }
}


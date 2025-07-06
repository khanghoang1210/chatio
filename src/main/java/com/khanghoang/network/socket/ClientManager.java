package com.khanghoang.network.socket;

import java.io.OutputStream;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
    private static final Set<OutputStream> clientOutputs = ConcurrentHashMap.newKeySet();

    public static void addClient(OutputStream out) {
        clientOutputs.add(out);
    }

    public static void removeClient(OutputStream out) {
        clientOutputs.remove(out);
    }

    public static void broadcast(byte[] message, OutputStream exclude) {
        for (OutputStream out : clientOutputs) {
            if (out != exclude) {
                try {
                    byte[] lengthBytes = intToByteArray(message.length);
                    out.write(lengthBytes);
                    out.write(message);
                    out.flush();
                } catch (Exception e) {
                    System.out.println("Failed to send to one client: " + e.getMessage());
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

package com.khanghoang.network.socket;

import com.khanghoang.protocol.MessageFrame;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream()
        ) {
            ClientManager.addClient(output);
            System.out.println("Client connected: " + socket.getRemoteSocketAddress());

            while (true) {
                byte[] lengthBytes = input.readNBytes(4);
                if (lengthBytes.length < 4) break;

                int length = byteArrayToInt(lengthBytes);
                byte[] data = input.readNBytes(length);

                MessageFrame message = MessageFrame.decode(data);
                System.out.println("[RECEIVED] From " + message.getFrom() + ": " + message.getContent());

                // Broadcast
                ClientManager.broadcast(data, output);
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } finally {
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

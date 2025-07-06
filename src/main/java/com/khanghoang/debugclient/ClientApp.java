package com.khanghoang.debugclient;

import com.khanghoang.protocol.MessageFrame;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String sender = scanner.nextLine();

        try (Socket socket = new Socket("localhost", 9000)) {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        byte[] lenBytes = input.readNBytes(4);
                        if (lenBytes.length < 4) break;

                        int length = byteArrayToInt(lenBytes);
                        byte[] data = input.readNBytes(length);

                        MessageFrame msg = MessageFrame.decode(data);
                        System.out.println("\n[From " + msg.getFrom() + "] " + msg.getContent());
                        System.out.print("> ");
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            receiveThread.start();

            while (true) {
                System.out.print("> ");
                String content = scanner.nextLine();
                if (content.equalsIgnoreCase("/quit")) break;

                MessageFrame message = new MessageFrame(
                        "chat",
                        sender,
                        "all",
                        null,
                        System.currentTimeMillis(),
                        content
                );

                byte[] encoded = message.encode();
                output.write(intToByteArray(encoded.length));
                output.write(encoded);
                output.flush();
            }

        } catch (IOException e) {
            System.out.println("Cannot connect to server: " + e.getMessage());
        }
    }

    private static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte)(value >> 24),
                (byte)(value >> 16),
                (byte)(value >> 8),
                (byte)value
        };
    }

    private static int byteArrayToInt(byte[] bytes) {
        return (bytes[0] & 0xFF) << 24 |
                (bytes[1] & 0xFF) << 16 |
                (bytes[2] & 0xFF) << 8 |
                (bytes[3] & 0xFF);
    }
}

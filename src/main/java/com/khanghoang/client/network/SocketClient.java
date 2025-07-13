package com.khanghoang.client.network;

import com.khanghoang.protocol.MessageFrame;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class SocketClient {
    private Socket socket;
    private DataOutputStream output;

    public void connect(String host, int port) throws Exception {
        socket = new Socket(host, port);
        output = new DataOutputStream(socket.getOutputStream());
    }

    public void sendMessage(MessageFrame frame) throws Exception {
        byte[] data = frame.encode();
        output.writeInt(data.length); // gửi độ dài trước để framing
        output.write(data);
        output.flush();
    }

    // Thêm vào ChatClient.java
    public void startReceiving(Consumer<MessageFrame> onMessageReceived) {
        new Thread(() -> {
            try {
                while (true) {
                    InputStream in = socket.getInputStream();

                    // 1. Đọc độ dài
                    byte[] lengthBytes = in.readNBytes(4);
                    if (lengthBytes.length < 4) break;

                    int length = byteArrayToInt(lengthBytes);

                    // 2. Đọc nội dung message
                    byte[] messageBytes = in.readNBytes(length);
                    if (messageBytes.length < length) break;

                    // 3. Decode message
                    MessageFrame frame = MessageFrame.decode(messageBytes);

                    // 4. Gửi về UI qua callback
                    onMessageReceived.accept(frame);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private int byteArrayToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8)  |
                (bytes[3] & 0xFF);
    }


    public void close() throws Exception {
        if (output != null) output.close();
        if (socket != null) socket.close();
    }
}

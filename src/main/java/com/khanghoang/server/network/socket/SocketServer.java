package com.khanghoang.server.network.socket;

import com.khanghoang.server.network.socket.client.ClientHandler;
import com.khanghoang.server.processor.MessageProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable{
    private final int port;
    private final MessageProcessor processor;

    public SocketServer(int port, MessageProcessor processor) {
        this.port = port;
        this.processor = processor;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Socket server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, processor)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.khanghoang.network.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable{
    private final int port;

    public SocketServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Socket server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

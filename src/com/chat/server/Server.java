package com.chat.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ArrayList<ProcessingClient> clients = new ArrayList<>();

    private Server() {
        final int SERVER_PORT = 4000;

        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Waiting for a client...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ProcessingClient newClient = new ProcessingClient(clientSocket, this, clients.size() + 1);
                clients.add(newClient);
                System.out.println("Client " + clients.size() + " added");
                new Thread(newClient).start();
            }
        } catch(Exception x) { x.printStackTrace(); }
    }

    void sendingMessageForClients(String message) {
        for(ProcessingClient client : clients)
        {
            client.sendingMessageForClient(message);
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}

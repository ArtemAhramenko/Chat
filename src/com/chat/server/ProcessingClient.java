package com.chat.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ProcessingClient implements Runnable {

    private PrintWriter outData;
    private Scanner inData;
    private Server server;
    private int clientNumber;

    ProcessingClient(Socket clientSocket, Server server, int clientNumber) throws IOException {
        this.server = server;
        this.clientNumber = clientNumber;
        outData = new PrintWriter(clientSocket.getOutputStream());
        inData = new Scanner(clientSocket.getInputStream());
    }

    @Override
    public void run() {
        while (true) {
            if (inData.hasNext()) {
                server.sendingMessageForClients("Client " + clientNumber + ": " + inData.nextLine());
            }
        }
    }

    void sendingMessageForClient(String message) {
        outData.println(message);
        outData.flush();
    }
}
package com.chat.server;

import com.chat.protocols.WhoIs;
import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class ProcessingClient implements Runnable {

    private final String DEFAULT_HOST = "whois.internic.net";
    private final int LEFT_BORDER = 6;
    private PrintWriter outData;
    private int clientNumber;
    private Scanner inData;
    private Server server;
    private Date date = new Date();

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
                String inString = inData.nextLine();
                if (inString.equalsIgnoreCase("get time")) {
                    server.sendingMessageForClients("Server : " + date.toString(), clientNumber - 1);
                } else if (inString.contains("whois ")) {
                    server.sendingMessageForClients(getWhoisProtocol(inString.substring(LEFT_BORDER, inString.length())), clientNumber - 1);
                } else {
                    server.sendingMessageForClients("Client " + clientNumber + ": " + inString);
                }
            }
        }
    }

    private String getWhoisProtocol(String domainName) {
        StringBuilder result = new StringBuilder("");
        WhoIs whois = new WhoIs();
        try {
            whois.connect(DEFAULT_HOST);
            String whoisData = whois.query("=" + domainName);
            result.append(whoisData);
            whois.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    void sendingMessageForClient(String message) {
        outData.println(message);
        outData.flush();
    }
}
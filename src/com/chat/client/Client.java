package com.chat.client;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Client() {
        final int SERVER_PORT = 50;
        final String SERVER_HOST = "localhost";

        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            Scanner inData = new Scanner(socket.getInputStream());
            PrintWriter outData = new PrintWriter(socket.getOutputStream());
            threadInMessages(inData);
            outMessage(outData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void outMessage(PrintWriter outData) {
        Scanner inputMessage = new Scanner(new InputStreamReader(System.in));
        System.out.println("Typing something..");
        while (true) {
            if (inputMessage.hasNext()) {
                outData.println(inputMessage.nextLine());
                outData.flush();
            }
        }
    }

    private void threadInMessages(Scanner inData) {
        new Thread(() -> {
            try {
                while (true) {
                    if (inData.hasNext()) {
                        System.out.println(inData.nextLine());
                    }
                }
            }
            catch (Exception ignored){}
        }).start();
    }

    public static void main(String[] args) {
        new Client();
    }
}
package com.ryanslee;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Closeable {
    public static final String SENTINEL = "exit";

    ServerSocket serverEnd;

    public Server(int port) throws IOException {
        // Start recieving messages
        serverEnd = new ServerSocket(port);
    }

    public void runServer() {
        System.out.println("Server started. Waiting for client...");
        try (Socket client = serverEnd.accept()) {
            System.out.printf("Client has connected.\n");
            InputStreamReader isr = new InputStreamReader(client.getInputStream());
            BufferedReader reader = new BufferedReader(isr);

            while (!client.isClosed()) { // server closes upon client msg
                String line = reader.readLine();
                System.out.println("Server read: " + line);
                // System.out.println("The line is a string that reads null: " + (line.equals("null")));
                if (line == null) {
                    System.out.println("Client unexpectedly disconnected.");
                    break;
                }
                if (line.equals(SENTINEL)) {
                    System.out.println("Client closed the service.");
                    client.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Client unexpectedly disconnected maybe?");
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        serverEnd.close();
    }
}

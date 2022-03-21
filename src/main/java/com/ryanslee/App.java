package com.ryanslee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
public class App {
    // to be used for testing
    static final int TESTPORT = 8080;
    static final int MIN_PORT_NUMBER = 0;
    static final int MAX_PORT_NUMBER = 65535;

    // Special values
    static final String SERVER_OPT = "S";
    static final String CLIENT_OPT = "C";
    static final String CLOSE_OPT = "X";

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(System.console().reader());
        System.out.printf("Open server (%s), open Client (%s), or close (%s): ", SERVER_OPT, CLIENT_OPT, CLOSE_OPT);
        String input = "X";
        try {
            input = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        switch (input) {
            case SERVER_OPT:
                serverProcess();
                break;

            case CLIENT_OPT:
                clientProcess();
                break;

            case CLOSE_OPT:
                break;

            default:
                break;
        }
    }

    /**
     * Subprocess that opens a server
     */
    static void serverProcess() {
        int port = -1;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter the port to listen on: ");
            port = Integer.parseInt(reader.readLine());
            while (!portAvailable(port)) {
                System.out.println("Port not available. Try again.");
                port = Integer.parseInt(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        try (Server server = new Server(port)) {
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean portAvailable(int port) {
        boolean portFree;
        try (ServerSocket ignored = new ServerSocket(port)) {
            // If nothing happens, the server is available.
            portFree = true;
        } catch (IOException e) {
            portFree = false;
        }
        return portFree;
    }

    /**
     * Subprocess that opens a client, allows for interaction, and closes it.
     */
    static void clientProcess() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter server address: ");
            String host = reader.readLine();
            System.out.print("Enter server port: ");
            Integer port = Integer.parseInt(reader.readLine());
            reader.close();
            Client client = new Client(host, port);
            client.runClient();
            client.close();
        } catch (UnknownHostException e) {
            System.out.println("Could not identify host.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not connect");
            e.printStackTrace();
        }
    }
}

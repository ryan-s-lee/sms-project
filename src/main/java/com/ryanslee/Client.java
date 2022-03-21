package com.ryanslee;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Closeable {
    // members
    Socket serverEnd;
    BufferedReader userIn;
    BufferedReader serverIn;
    PrintWriter serverOut;

    // special values
    static final String LOCALHOST = "127.0.0.1";
    static final String SENTINEL = "exit";

    // Runtime messages
    static final String INITIATE_MSG = "Attempting to initiate client...";

    // Error messages
    static final String UNCLOSABLE_MSG = "sussy baka";
    static final String NOHOST_MSG = "no host";
    static final String IOEXCEPTION_MSG = "ioexception";
    static final String UNKNOWN_EXCEPTION_MSG = "dunno what happened";

    /**
     * Constructor. Instatiates a socket connecting at the host's port, connects a
     * Byte Stream to the port, and links it with the console that the user runs
     * the main method program from.
     * 
     * @param host the ip address of the host
     * @param port the port of the host to send data to
     */

    public Client(String host, int port) throws UnknownHostException, IOException {
        System.out.println(INITIATE_MSG);
        // Server
        serverEnd = new Socket(host, port);
        System.out.println("Connection established with server");

        // Input stream uses terminal
        userIn = new BufferedReader(new InputStreamReader(System.in, System.console().charset()));
        serverIn = new BufferedReader(new InputStreamReader(serverEnd.getInputStream()));
        serverOut = new PrintWriter(serverEnd.getOutputStream());
    }

    /**
     * Default constructor for client, uses 127.0.0.1:8080
     * 
     */
    public Client() throws UnknownHostException, IOException{
        this(LOCALHOST, 8080);
    }

    /**
     * Runs the client, causing it to read lines from the terminal and send them
     * to the server.
     * 
     * @return
     */
    public int runClient() {
        try {
            while (true) {
                System.out.print("Write a message: ");
                String line = userIn.readLine();
                System.out.println("Message sent to server.");
                serverOut.println(line);
                serverOut.flush();
                if (line.equals(SENTINEL)) {
                    System.out.println("Ending client process");
                    break;
                }
            }
        } catch (IOException e) {
            return 1;
        }
        return 0;
    }

    @Override
    public void close() throws IOException {
        // closing procedures
        serverOut.flush();
        serverEnd.close();
    }
}

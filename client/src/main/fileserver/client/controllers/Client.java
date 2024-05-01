package fileserver.client.controllers;

import fileserver.client.models.connection.ServerSocketConnection;

import java.io.*;
import java.net.*;

public final class Client {
    final InetAddress serverAddress;
    final int serverPort;

    public Client(final InetAddress serverAddress, final int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void work() throws IOException {
        System.out.println("Client started!");
        new Session(
            new ServerSocketConnection(serverAddress, serverPort)
        ).start();
    }
}

final class Session extends Thread {
    private final ServerSocketConnection connection;

    Session(final ServerSocketConnection connection) {
        this.connection = connection;
    }

    public void run() {
        try {
            new ConnectedServerRequester(new ClienCLI(System.in), connection).work();
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
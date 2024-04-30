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
        try (
            ServerSocketConnection connection = new ServerSocketConnection(
                serverAddress,
                serverPort
            )
        ) {
            Session session = new Session(connection);
            session.start();
        }
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
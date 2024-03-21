package fileserver.client.controllers;

import fileserver.client.models.ServerConnection;

import java.io.*;
import java.net.*;

public final class Client {
    private final Socket socket;

    public Client(final String serverAddress, final int serverPort) throws IOException {
        try {
            this.socket = new Socket(InetAddress.getByName(serverAddress), serverPort);
        } catch (IOException e) {
            throw new IOException("Can't create Client. " + e.getMessage(), e);
        }
    }

    public void work() throws IOException {
        System.out.println("Client started!");
        try (
            ServerConnection connection = new ServerConnection(
                socket.getOutputStream(),
                socket.getInputStream())
        ) {
            Session session = new Session(connection);
            session.start();
        }
    }
}

final class Session extends Thread {
    private final ServerConnection connection;

    Session(final ServerConnection connection) {
        this.connection = connection;
    }

    public void run() {
        try {
            new ClientCLI(System.in, connection).work();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
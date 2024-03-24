package fileserver.server.controllers;

import fileserver.server.models.connection.ClientSocketConnection;

import java.net.*;
import java.nio.file.Path;

public final class Server {
    private final Path dataFolder;
    private final InetAddress address;
    private final int port;

    public Server(final Path dataFolder, final InetAddress address, final int port) {
        this.dataFolder = dataFolder;
        this.address = address;
        this.port = port;
    }

    public void work() {
        System.out.println("Server started!");
        try(final ServerSocket serverSocket = new ServerSocket(port, 50, address)) {
            while (true) {
                try (
                    final ClientSocketConnection connection = new ClientSocketConnection(
                        serverSocket.accept()
                    )
                ) {
                    Session session = new Session(dataFolder, connection);
                    session.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

final class Session extends Thread {
    private final Path dataFolder;
    private final ClientSocketConnection connection;

    Session(final Path dataFolder, final ClientSocketConnection connection) {
        this.dataFolder = dataFolder;
        this.connection = connection;
    }

    public void run() {
        try {
            new ConnectedClientHandler(dataFolder, connection).work();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

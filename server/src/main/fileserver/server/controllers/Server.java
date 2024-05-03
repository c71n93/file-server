package fileserver.server.controllers;

import fileserver.server.models.connection.ClientSocketConnection;
import java.io.IOException;
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

    public void work() throws IOException {
        System.out.println("Server started!");
        try(final ServerSocket serverSocket = new ServerSocket(port, 50, address)) {
            while (true) {
                try {
                    Session session = new Session(
                        dataFolder,
                        new ClientSocketConnection(serverSocket.accept())
                    );
                    session.start();
                } catch (IOException e) {
                    throw new ClientConnectionException(
                        "Unable to set up connection with client.",
                        e
                    );
                }
            }
        } catch (IOException e) {
            throw new ServerSocketException("Unable to create server socket.", e);
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

    @Override
    public void run() {
        try {
            new ConnectedClientHandler(dataFolder, connection).work();
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

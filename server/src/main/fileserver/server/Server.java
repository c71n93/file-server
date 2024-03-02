package fileserver.server;

import java.io.*;
import java.net.*;
import java.nio.file.Path;

public class Server {
    private final Path dataFolder;
    private final String address;
    private final int port;

    Server(Path dataFolder, String address, int port) {
        this.dataFolder = dataFolder;
        this.address = address;
        this.port = port;
    }

    public void work() {
        System.out.println("Server started!");
        try(ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            while (true) {
                try {
                    Session session = new Session(dataFolder, serverSocket.accept());
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

class Session extends Thread {
    private final Path dataFolder;
    private final Socket socket;

    Session(Path dataFolder, Socket socket) {
        this.dataFolder = dataFolder;
        this.socket = socket;
    }

    public void run() {
        try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            ServerRequestHandler requestHandler = new ServerRequestHandler(dataFolder, inputStream, outputStream);
            requestHandler.work();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

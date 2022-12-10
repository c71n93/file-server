package server;

import java.io.*;
import java.net.*;

public class Server {
    private final String address;
    private final int port;

    Server(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void work() {
        System.out.println("Server started!");
        try(ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            try {
                Session session = new Session(serverSocket.accept());
                session.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Session extends Thread {
    private final Socket socket;

    Session(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ) {
            ServerRequestHeandler requestHeandler = new ServerRequestHeandler(inputStream, outputStream);
            requestHeandler.work();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

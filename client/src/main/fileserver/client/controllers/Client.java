package fileserver.client.controllers;

import java.io.*;
import java.net.*;

public final class Client {
    private final Socket socket;

    public Client(String serverAddress, int serverPort) throws IOException {
        try {
            this.socket = new Socket(InetAddress.getByName(serverAddress), serverPort);
        } catch (IOException e) {
            throw new IOException("Can't create Client. " + e.getMessage());
        }
    }

    public void work() {
        System.out.println("Client started!");
        Session session = new Session(this.socket);
        session.start();
    }
}

final class Session extends Thread {
    private final Socket socket;

    Session(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ) {
            ClientCLI clientCLI = new ClientCLI(System.in, inputStream, outputStream);
            clientCLI.work();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
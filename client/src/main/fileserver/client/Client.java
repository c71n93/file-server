package fileserver.client;

import java.io.*;
import java.net.*;

public class Client {
    private final String serverAddress;
    private final int serverPort;

    Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void work() {
        System.out.println("Client started!");
        try {
            Socket socket = new Socket(InetAddress.getByName(serverAddress), serverPort);
            Session session = new Session(socket);
            session.start();
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
            ClientCLI clientCLI = new ClientCLI(System.in, inputStream, outputStream);
            clientCLI.work();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
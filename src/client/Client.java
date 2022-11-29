package client;

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
            try {
                Socket socket = new Socket(InetAddress.getByName(serverAddress), serverPort);
                Session session = new Session(socket);
                session.start();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class Session extends Thread {
    private final Socket socket;
    final String msgToServer = "Give me everything you have!";

    Session(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ) {
            outputStream.writeUTF(msgToServer);
            System.out.printf("Sent: %s\n", msgToServer);

            String msg = inputStream.readUTF();
            System.out.printf("Received: %s\n", msg);
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

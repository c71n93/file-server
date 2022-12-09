package server;

import java.io.*;
import java.net.*;

public class Server {
    private final Database serverDatabase = new Database();
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
                System.out.println("error: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
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
            String msg = inputStream.readUTF();
            System.out.printf("Received: %s\n", msg);

            outputStream.writeUTF("Responce");
            System.out.printf("Sent: %s\n", "Responce");
            socket.close();
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }
}

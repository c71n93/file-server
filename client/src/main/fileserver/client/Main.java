package fileserver.client;

//TODO: refactor all try/catch blocks

import fileserver.client.controllers.Client;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Client("127.0.0.1", 23456).work();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

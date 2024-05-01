package fileserver.client;

//TODO: refactor all try/catch blocks

import fileserver.client.controllers.Client;
import java.io.IOException;
import java.net.InetAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        new Client(InetAddress.getByName("127.0.0.1"), 23456).work();
    }
}

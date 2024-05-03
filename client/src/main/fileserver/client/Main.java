package fileserver.client;

import fileserver.client.controllers.Client;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws IOException {
        new Client(InetAddress.getByName("127.0.0.1"), 23456).work();
    }
}

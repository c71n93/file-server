package fileserver.server;

//TODO: refactor all try/catch blocks

import fileserver.server.controllers.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Error: the path to the data folder must be passed as a program argument");
            return;
        }
        new Server(Path.of(args[0]), InetAddress.getByName("127.0.0.1"), 23456).work();
    }
}
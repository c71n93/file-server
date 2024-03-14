package fileserver.server;

//TODO: refactor all try/catch blocks

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: the path to the data folder must be passed as a program argument");
            return;
        }
        Server server = new Server(Path.of(args[0]), "127.0.0.1", 23456);
        server.work();
    }
}
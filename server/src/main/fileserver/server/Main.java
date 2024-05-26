package fileserver.server;

import fileserver.server.controllers.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println(
                """
                Error: wrong number of arguments
                    usage: <data folder> <server address> <server port>
                """
            );
            return;
        }
        new Server(
            Path.of(args[0]),
            InetAddress.getByName(args[1]),
            Integer.parseInt(args[2])
        ).work();
    }
}
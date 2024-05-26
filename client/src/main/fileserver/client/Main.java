package fileserver.client;

import fileserver.client.controllers.Client;
import java.io.IOException;
import java.net.InetAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println(
                """
                Error: wrong number of arguments
                    usage: <server address> <server port>
                """
            );
            return;
        }
        new Client(InetAddress.getByName(args[0]), Integer.parseInt(args[1])).work();
    }
}

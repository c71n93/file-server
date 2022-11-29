package server;

public class Main {

    public static void main(String[] args) {
        Server server = new Server("127.0.0.1", 23456);
        server.work();
//        Database serverDatabase = new Database();
//        ServerCLI serverCLI = new ServerCLI(serverDatabase);
//        serverCLI.work();
    }
}
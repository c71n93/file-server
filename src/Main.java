package server;

public class Main {

    public static void main(String[] args) {
        Database serverDatabase = new Database();
        ServerCLI serverCLI = new ServerCLI(serverDatabase);
        serverCLI.work();
    }
}
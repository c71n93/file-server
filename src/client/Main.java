package client;

//TODO: refactor all try/catch blocks

public class Main {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 23456);
        client.work();
    }
}

package client;

import java.io.*;
import java.util.Scanner;

public class ClientCLI {
    private Scanner scanner;
    private DataInputStream messageInputStream;
    private DataOutputStream messageOutputStream;
    
    // TODO: find out how to make enum with HTTP request, that common for Client and Server 

    public ClientCLI(InputStream comandInputStream, DataInputStream messageInputStream, DataOutputStream messageOutputStream) {
        scanner = new Scanner(comandInputStream);
        this.messageInputStream = messageInputStream;
        this.messageOutputStream = messageOutputStream;
    }

    public void work() {
        String command;
        boolean isNextAction;
        do {
            System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
            command = scanner.next();
            isNextAction = chooseAction(command);
            try {
                System.out.println(messageInputStream.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (isNextAction);
        scanner.close();
    }

    private boolean chooseAction(String command) {
        boolean isNextAction = true;
        switch (command) {
            case "1" -> getAction();
            case "2" -> putAction();
            case "3" -> deleteAction();
            case "exit" -> isNextAction =  false;
            default -> System.out.println("error: no such command");
        }
        System.out.println("The request was sent.");
        return isNextAction;
    }

    private void getAction() {
        System.out.println("Enter filename: ");
        String fileName = scanner.next();
        String request = "GET " + fileName;
        try {
            messageOutputStream.writeUTF(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putAction() {
        System.out.println("Enter filename: ");
        String fileName = scanner.next();
        System.out.println("Enter file content: ");
        String fileContent = scanner.nextLine();
        String request = "PUT " + fileName + " " + fileContent;
        try {
            messageOutputStream.writeUTF(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteAction() {
        System.out.println("Enter filename: ");
        String fileName = scanner.next();
        String request = "DELETE " + fileName;
        try {
            messageOutputStream.writeUTF(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

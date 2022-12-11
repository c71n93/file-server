package client;

import java.io.*;
import java.util.Scanner;
import http.*;

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
        } while (isNextAction);
        scanner.close();
    }

    private boolean chooseAction(String command) {
        //TODO: make an endless work (untill exit comand occurs)
        boolean isNextAction = false;
        //TODO: replace comand string by Enum (mb request)
        switch (command) {
            case "1" -> {
                getActionRequest();
                System.out.println("The request was sent.");
                getActionResponce();
            }
            case "2" -> {
                putActionRequest();
                System.out.println("The request was sent.");
                putActionResponce();
            }
            case "3" -> {
                deleteActionRequest();
                System.out.println("The request was sent.");
                deleteActionResponce();
            }
            case "exit" -> {
                isNextAction =  false;
                exitActionRequest();
                System.out.println("The request was sent.");
            }
            default -> System.out.println("error: no such command");
        }
        return isNextAction;
    }

    private void getActionRequest() {
        System.out.print("Enter filename: ");
        String fileName = scanner.next();
        String request = "GET " + fileName;
        try {
            messageOutputStream.writeUTF(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putActionRequest() {
        System.out.print("Enter filename: ");
        String fileName = scanner.next();
        scanner.nextLine(); //Consumes extra '\n' symbol
        System.out.print("Enter file content: ");
        String fileContent = scanner.nextLine();
        String request = "PUT " + fileName + " " + fileContent;
        try {
            messageOutputStream.writeUTF(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteActionRequest() {
        System.out.print("Enter filename: ");
        String fileName = scanner.next();
        String request = "DELETE " + fileName;
        try {
            messageOutputStream.writeUTF(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: delete this request after pass project on JB
    private void exitActionRequest() {
        String request = "EXIT";
        try {
            messageOutputStream.writeUTF(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: refactor responce funtion (now they are copypast)
    private void getActionResponce() {
        try {
            Response response = Response.valueOfCode(messageInputStream.readInt());
            switch (response) {
                case OK -> {
                    String content = messageInputStream.readUTF();
                    System.out.printf("The content of the file is: %s\n", content);
                }
                case NOT_FOUND -> {
                    System.out.printf("The response says that the file was not found!\n");
                }
                case FORBIDDEN -> {
                    System.out.printf("The response says that creating the file was forbidden!\n");
                }
                case BAD_REQUEST -> {
                    System.out.printf("The response says that the request was incorrect!\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putActionResponce() {
        try {
            Response response = Response.valueOfCode(messageInputStream.readInt());
            switch (response) {
                case OK -> {
                    System.out.printf("The response says that the file was created!\n");
                }
                case NOT_FOUND -> {
                    System.out.printf("The response says that the file was not found!\n");
                }
                case FORBIDDEN -> {
                    System.out.printf("The response says that creating the file was forbidden!\n");
                }
                case BAD_REQUEST -> {
                    System.out.printf("The response says that the request was incorrect!\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteActionResponce() {
        try {
            Response response = Response.valueOfCode(messageInputStream.readInt());
            switch (response) {
                case OK -> {
                    System.out.printf("The response says that the file was successfully deleted!\n");
                }
                case NOT_FOUND -> {
                    System.out.printf("The response says that the file was not found!\n");
                }
                case FORBIDDEN -> {
                    System.out.printf("The response says that creating the file was forbidden!\n");
                }
                case BAD_REQUEST -> {
                    System.out.printf("The response says that the request was incorrect!\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

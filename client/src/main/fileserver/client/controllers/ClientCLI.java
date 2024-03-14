package fileserver.client.controllers;

import java.io.*;
import java.util.Scanner;
import fileserver.client.models.ParsedCommand;
import fileserver.common.http.DeleteRequest;
import fileserver.common.http.GetRequest;
import fileserver.common.http.PutRequest;
import fileserver.common.http.Response;

public final class ClientCLI {
    private final Scanner scanner;
    private final DataInputStream messageIS;
    private final DataOutputStream messageOS;
    
    // TODO: find out how to make enum with HTTP request, that common for Client and Server

    public ClientCLI(InputStream commandIS, InputStream messageIS,
                     OutputStream messageOS) {
        scanner = new Scanner(commandIS);
        this.messageIS = new DataInputStream(messageIS);
        this.messageOS = new DataOutputStream(messageOS);
    }

    public void work() {
        ParsedCommand.Command command;
        boolean isNextAction;
        do {
            System.out.println("Enter action (enter help for help): ");
            command = new ParsedCommand(scanner.nextLine()).takeCommand();
            isNextAction = chooseAction(command);
        } while (isNextAction);
        scanner.close();
    }

    private boolean chooseAction(ParsedCommand.Command command) {
        //TODO: make an endless work (untill exit comand occurs)
        boolean isNextAction = false;
        switch (command.type) {
            case GET -> {
                getActionRequest(command);
                System.out.println("The request was sent.");
                getActionResponce();
            }
            case PUT -> {
                putActionRequest(command);
                System.out.println("The request was sent.");
                putActionResponce();
            }
            case DELETE -> {
                deleteActionRequest(command);
                System.out.println("The request was sent.");
                deleteActionResponce();
            }
            case EXIT -> isNextAction =  false;
            case HELP -> System.out.println(((ParsedCommand.HelpCommand) command).getHelp());
            case INVALID -> System.out.println("Wrong command, use 'help'");
            default -> throw new IllegalStateException("Unknown command type");
        }
        return isNextAction;
    }

    private void getActionRequest(ParsedCommand.Command command) {
        final GetRequest request = ((ParsedCommand.GetCommand) command).getRequest();
        String str = "GET " + request.getFileName();
        try {
            messageOS.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putActionRequest(ParsedCommand.Command command) {
        final PutRequest request = ((ParsedCommand.PutCommand) command).getRequest();
        String str = "PUT " + request.getFileName() + " " + request.getFileContent();
        try {
            messageOS.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteActionRequest(ParsedCommand.Command command) {
        final DeleteRequest request = ((ParsedCommand.DeleteCommand) command).getRequest();
        String str = "DELETE " + request.getFileName();
        try {
            messageOS.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: refactor responce funtion (now they are copypast)
    private void getActionResponce() {
        try {
            Response response = Response.valueOfCode(messageIS.readInt());
            switch (response) {
                case OK -> {
                    String content = messageIS.readUTF();
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
            Response response = Response.valueOfCode(messageIS.readInt());
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
            Response response = Response.valueOfCode(messageIS.readInt());
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

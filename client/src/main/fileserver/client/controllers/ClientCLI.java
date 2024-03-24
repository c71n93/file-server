package fileserver.client.controllers;

import java.io.*;
import java.util.Scanner;
import fileserver.client.models.ParsedCommand;
import fileserver.client.models.connection.ServerConnection;
import fileserver.common.httpc.request.CloseRequest;
import fileserver.common.httpc.response.Response;
import fileserver.common.httpc.response.ResponseWithContent;

public final class ClientCLI {
    private final Scanner scanner;
    private final ServerConnection connection;

    public ClientCLI(final InputStream commandIS, final ServerConnection connection) {
        scanner = new Scanner(commandIS);
        this.connection = connection;
    }

    public void work() throws IOException {
        ParsedCommand.Command command;
        boolean isNextAction;
        do {
            System.out.println("Enter action (enter help for help): ");
            command = new ParsedCommand(scanner.nextLine()).takeCommand();
            isNextAction = chooseAction(command);
        } while (isNextAction);
        scanner.close();
    }

    public void workOnce() throws IOException {
        ParsedCommand.Command command;
        System.out.println("Enter action (enter help for help): ");
        command = new ParsedCommand(scanner.nextLine()).takeCommand();
        chooseAction(command);
        scanner.close();
    }

    private boolean chooseAction(final ParsedCommand.Command command) throws IOException {
        boolean isNextAction = true;
        switch (command.type) {
            case GET -> {
                connection.requestOS().writeObject(((ParsedCommand.GetCommand) command).getRequest());
                System.out.println("The request was sent.");
                getActionResponce();
            }
            case PUT -> {
                connection.requestOS().writeObject(((ParsedCommand.PutCommand) command).getRequest());
                System.out.println("The request was sent.");
                putActionResponce();
            }
            case DELETE -> {
                connection.requestOS().writeObject(((ParsedCommand.DeleteCommand) command).getRequest());
                System.out.println("The request was sent.");
                deleteActionResponce();
            }
            case HELP -> System.out.println(((ParsedCommand.HelpCommand) command).getHelp());
            case INVALID -> System.out.println("Wrong command, use 'help'");
            case EXIT -> {
                connection.requestOS().writeObject(new CloseRequest());
                isNextAction =  false;
            }
            default -> throw new IllegalStateException("Unknown command type");
        }
        return isNextAction;
    }

    //TODO: refactor responce funtion (now they are copypast)
    private void getActionResponce() {
        try {
            Response response = (Response) connection.responseIS().readObject();
            switch (response.getType()) {
                case OK -> {
                    System.out.printf("The content of the file is: %s\n", ((ResponseWithContent) response).getContent());
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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void putActionResponce() {
        try {
            Response response = (Response) connection.responseIS().readObject();
            switch (response.getType()) {
                case OK -> {
                    System.out.printf("The response says that the file was created!\n");
                }
                case NOT_FOUND -> {
                    System.out.printf("The response says that the file already exist!\n");
                }
                case FORBIDDEN -> {
                    System.out.printf("The response says that creating the file was forbidden!\n");
                }
                case BAD_REQUEST -> {
                    System.out.printf("The response says that the request was incorrect!\n");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteActionResponce() {
        try {
            Response response = (Response) connection.responseIS().readObject();
            switch (response.getType()) {
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

package fileserver.client.controllers;

import java.io.*;
import fileserver.client.models.ParsedCommand;
import fileserver.client.models.connection.ServerConnection;
import fileserver.common.httpc.request.CloseRequest;
import fileserver.common.httpc.response.Response;
import fileserver.common.httpc.response.ResponseWithContent;

public final class ConnectedServerRequester {
    private final ServerConnection connection;
    private final ClienCLI clienCLI;

    public ConnectedServerRequester(final ClienCLI clienCLI, final ServerConnection connection) {
        this.connection = connection;
        this.clienCLI = clienCLI;
    }

    public void work() throws IOException {
        ParsedCommand.Command command;
        boolean isNextAction;
        do {
            command = clienCLI.nextCommand();
            isNextAction = chooseAction(command);
        } while (isNextAction);
    }

    public void workOnce() throws IOException {
        ParsedCommand.Command command;
        command = clienCLI.nextCommandOnce();

        chooseAction(command);
    }

    private boolean chooseAction(final ParsedCommand.Command command) throws IOException {
        boolean isNextAction = true;
        switch (command.type) {
            case GET -> {
                connection.requestOS().writeObject(((ParsedCommand.GetCommand) command).getRequest());
                clienCLI.displayMsg(processResponseWithContent());
            }
            case PUT -> {
                connection.requestOS().writeObject(((ParsedCommand.PutCommand) command).getRequest());
                clienCLI.displayMsg(processResponse());
            }
            case DELETE -> {
                connection.requestOS().writeObject(((ParsedCommand.DeleteCommand) command).getRequest());
                clienCLI.displayMsg(processResponse());
            }
            case EXIT -> {
                connection.requestOS().writeObject(new CloseRequest());
                isNextAction =  false;
            }
            case HELP -> throw new IllegalStateException(
                "'Help' command should not have been appeared here"
            );
            case INVALID -> throw new IllegalStateException(
                "'Invalid' command should not have been appeared here"
            );
            default -> throw new IllegalStateException("Unknown command type");
        }
        return isNextAction;
    }

    private String processResponseWithContent() throws ResponseReadingException {
        try {
            Response response = (Response) connection.responseIS().readObject();
            if (response.getType() == Response.ResponseType.OK) {
                return String.format(
                    "The content of the file is: %s",
                    ((ResponseWithContent) response).getContent()
                );
            } else {
                return responseErrorToText(response.getType());
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class of serializable response cannot be found", e);
        } catch (IOException e) {
            throw new ResponseReadingException(
                "Error while reading response with content.",
                e
            );
        }
    }

    private String processResponse() throws ResponseReadingException {
        try {
            Response response = (Response) connection.responseIS().readObject();
            if (response.getType() == Response.ResponseType.OK) {
                return "Done";
            } else {
                return responseErrorToText(response.getType());
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class of serializable response cannot be found", e);
        } catch (IOException e) {
            throw new ResponseReadingException(
                "Error while reading response without content.",
                e
            );
        }
    }

    private static String responseErrorToText(final Response.ResponseType type) {
        switch (type) {
            case NOT_FOUND -> {
                return "Response error: NOT FOUND";
            }
            case FORBIDDEN -> {
                return "Response error: FORBIDDEN";
            }
            case BAD_REQUEST -> {
                return "Response error: BAD REQUEST";
            }
            default -> throw new IllegalStateException("Unknown response error");
        }
    }
}

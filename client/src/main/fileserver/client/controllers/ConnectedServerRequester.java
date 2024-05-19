package fileserver.client.controllers;

import java.io.*;
import fileserver.client.models.ParsedCommand;
import fileserver.client.models.connection.ServerConnection;
import fileserver.common.httpc.request.CloseRequest;
import fileserver.common.httpc.response.Response;
import fileserver.common.httpc.response.ResponseWithContent;
import java.nio.file.Files;

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
            case DOWNLOAD -> {
                ParsedCommand.DownloadCommand downloadCmd = (ParsedCommand.DownloadCommand) command;
                connection.requestOS().writeObject(downloadCmd.request);
                clienCLI.displayMsg(processResponseWithContent(downloadCmd.savedContentFile));
            }
            case UPLOAD -> {
                connection.requestOS().writeObject(((ParsedCommand.UploadCommand) command).request);
                clienCLI.displayMsg(processResponse());
            }
            case DELETE -> {
                connection.requestOS().writeObject(((ParsedCommand.DeleteCommand) command).request);
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

    private String processResponseWithContent(final File savedContentFile)
        throws ResponseReadingException {
        try {
            Response response = (Response) connection.responseIS().readObject();
            if (response.getType() == Response.ResponseType.OK) {
                writeContentToFile(
                    ((ResponseWithContent) response).getContent(),
                    savedContentFile
                );
                return String.format(
                    "The content is saved to file '%s'",
                    savedContentFile.getPath()
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

    private void writeContentToFile(final String content, final File savedContentFile) {
        try {
            Files.writeString(savedContentFile.toPath(), content);
        } catch (IOException e) {
            throw new IllegalStateException("Can't write content to file", e);
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

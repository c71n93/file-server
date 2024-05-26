package fileserver.client.models;

import fileserver.client.controllers.ConnectedServerRequester;
import fileserver.common.httpc.request.DeleteRequest;
import fileserver.common.httpc.request.GetRequest;
import fileserver.common.httpc.request.PutRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class for parsing commands to {@link ConnectedServerRequester}
 */
public final class ParsedCommand {
    private final String command;

    public ParsedCommand(String command) {
        this.command = command;
    }

    public Command takeCommand() throws InvalidPassedFileException {
        final String[] splitCommand = command.split(" ");
        return switch (CommandsType.value(splitCommand[0])) {
            case DOWNLOAD -> parseDownload(splitCommand);
            case UPLOAD -> parseUpload(splitCommand);
            case DELETE -> parseDelete(splitCommand);
            case EXIT -> new ExitCommand();
            case HELP -> new HelpCommand();
            default -> new InvalidCommand();
        };
    }

    private Command parseDownload(String[] splitCommand) throws InvalidPassedFileException {
        // download filename path
        if (splitCommand.length != 3) {
            return new InvalidCommand();
        }
        String filename = splitCommand[1];
        String path = splitCommand[2];
        File savedContentFile = new File(path);
        try (FileWriter ignored = new FileWriter(savedContentFile)) {
        } catch (IOException e) {
            throw new InvalidPassedFileException(
                String.format("Can't write to file %s", path),
                e
            );
        }
        return new DownloadCommand(new GetRequest(filename), new File(path));
    }

    private Command parseUpload(String[] splitCommand) throws InvalidPassedFileException {
        // upload path filename
        if (splitCommand.length != 3) {
            return new InvalidCommand();
        }
        String path = splitCommand[1];
        String filename = splitCommand[2];
        try {
            String content = Files.readString(Paths.get(path));
            return new UploadCommand(new PutRequest(filename, content));
        } catch (IOException e) {
            throw new InvalidPassedFileException(
                String.format("Error occurred while reading content from file %s", path),
                e
            );
        }
    }

    private Command parseDelete(String[] splitCommand) {
        if (splitCommand.length != 2) {
            return new InvalidCommand();
        }
        return new DeleteCommand(new DeleteRequest(splitCommand[1]));
    }

    public static class Command {
        public final CommandsType type;

        Command(CommandsType type) {
            this.type = type;
        }
    }

    public static class DownloadCommand extends Command {
        public final File savedContentFile;
        public final GetRequest request;

        DownloadCommand(final GetRequest request, final File savedFile) {
            super(CommandsType.DOWNLOAD);
            this.request = request;
            this.savedContentFile = savedFile;
        }
    }

    public static class UploadCommand extends Command {
        public final PutRequest request;

        UploadCommand(PutRequest request) {
            super(CommandsType.UPLOAD);
            this.request = request;
        }
    }

    public static class DeleteCommand extends Command {
        public final DeleteRequest request;

        DeleteCommand(DeleteRequest request) {
            super(CommandsType.DELETE);
            this.request = request;
        }
    }

    public static class HelpCommand extends Command {
        public final static String help = """
            commands:
                download <file-on-server> <local-file> - downloads file from server to a local file
                upload <local-file> <file-on-server> - uploads local file to a file on server
                delete <file-on-server> - deletes file on server
                exit - end current client session
                help - show this help message""";
        HelpCommand() {
            super(CommandsType.HELP);
        }
    }

    public static class ExitCommand extends Command {
        ExitCommand() {
            super(CommandsType.EXIT);
        }
    }

    public static class InvalidCommand extends Command {
        InvalidCommand() {
            super(CommandsType.INVALID);
        }
    }

    public enum CommandsType {
        DOWNLOAD("download"),
        UPLOAD("upload"),
        DELETE("delete"),
        HELP("help"),
        EXIT("exit"),
        INVALID;

        public final String name;

        CommandsType(String name) {
            this.name = name;
        }
        CommandsType() {
            this.name = "invalid";
        }

        public static CommandsType value(String name) {
            for (CommandsType c : CommandsType.values()) {
                if (name.equals(c.name)) {
                    return c;
                }
            }
            return INVALID;
        }
    }
}

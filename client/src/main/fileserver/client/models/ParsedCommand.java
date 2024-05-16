package fileserver.client.models;

import fileserver.client.controllers.ConnectedServerRequester;
import fileserver.common.httpc.request.DeleteRequest;
import fileserver.common.httpc.request.GetRequest;
import fileserver.common.httpc.request.PutRequest;
import java.io.File;
import java.nio.file.Path;

/**
 * Class for parsing commands to {@link ConnectedServerRequester}
 */
public final class ParsedCommand {
    private final String command;

    public ParsedCommand(String command) {
        this.command = command;
    }

    public Command takeCommand() {
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

    private Command parseDownload(String[] splitCommand) {
        // download name path
        if (splitCommand.length != 3) {
            return new InvalidCommand();
        }
        // TODO: to check if file
        return new DownloadCommand(new GetRequest(splitCommand[1]), Path.of(splitCommand[2]));
    }

    private Command parseUpload(String[] splitCommand) {
        // upload path name
        if (splitCommand.length != 3) {
            return new InvalidCommand();
        }
        String content = new File(splitCommand[2]).getName(); // TODO: read content from file
        return new UploadCommand(new PutRequest(splitCommand[1], content));
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
        public final Path downloadedFile;
        public final GetRequest request;

        DownloadCommand(final GetRequest request, final Path downloadedFile) {
            super(CommandsType.DOWNLOAD);
            this.request = request;
            this.downloadedFile = downloadedFile;
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
        public final static String help = "HELP"; //TODO: add help message
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

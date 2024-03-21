package fileserver.client.models;

import fileserver.common.http.request.DeleteRequest;
import fileserver.common.http.request.GetRequest;
import fileserver.common.http.request.PutRequest;
import fileserver.common.http.response.Response;

/**
 * Class for parsing commands to {@link fileserver.client.controllers.ClientCLI}
 */
public final class ParsedCommand {
    private final String command;

    public Command takeCommand() {
        final String[] splitCommand = command.split(" ");
        return switch (CommandsType.value(splitCommand[0])) {
            case GET -> parseGet(splitCommand);
            case PUT -> parsePut(splitCommand);
            case DELETE -> parseDelete(splitCommand);
            case EXIT -> new ExitCommand();
            case HELP -> new HelpCommand();
            default -> new InvalidCommand();
        };
    }

    private Command parseGet(String[] splitCommand) {
        if (splitCommand.length != 2) {
            return new InvalidCommand();
        }
        return new GetCommand(new GetRequest(splitCommand[1]));
    }

    private Command parsePut(String[] splitCommand) {
        if (splitCommand.length != 3) {
            return new InvalidCommand();
        }
        return new PutCommand(new PutRequest(splitCommand[1], splitCommand[2]));
    }

    private Command parseDelete(String[] splitCommand) {
        if (splitCommand.length != 2) {
            return new InvalidCommand();
        }
        return new DeleteCommand(new DeleteRequest(splitCommand[1]));
    }

    public ParsedCommand(String command) {
        this.command = command;
    }

    public static class Command {
        public final CommandsType type;

        Command(CommandsType type) {
            this.type = type;
        }
    }

    public static class GetCommand extends Command {
        private final GetRequest request;

        GetCommand(GetRequest request) {
            super(CommandsType.GET);
            this.request = request;
        }

        public GetRequest getRequest() {
            return this.request;
        }
    }

    public static class PutCommand extends Command {
        private final PutRequest request;

        PutCommand(PutRequest request) {
            super(CommandsType.PUT);
            this.request = request;
        }

        public PutRequest getRequest() {
            return this.request;
        }
    }

    public static class DeleteCommand extends Command {
        private final DeleteRequest request;

        DeleteCommand(DeleteRequest request) {
            super(CommandsType.DELETE);
            this.request = request;
        }

        public DeleteRequest getRequest() {
            return this.request;
        }
    }

    public static class HelpCommand extends Command {
        private final static String help = "HELP"; //TODO: add help message
        HelpCommand() {
            super(CommandsType.HELP);
        }

        public String getHelp() {
            return help;
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
        GET("get"),
        PUT("put"),
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

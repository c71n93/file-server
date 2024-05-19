package fileserver.client.controllers;

import fileserver.client.models.InvalidPassedFileException;
import fileserver.client.models.ParsedCommand;
import java.io.InputStream;
import java.util.Scanner;

public final class ClienCLI {
    private final Scanner scanner;
    private final static String greetingMsg = "Enter action (enter help for help): ";

    public ClienCLI(final InputStream commandIS) {
        scanner = new Scanner(commandIS);
    }
    public ParsedCommand.Command nextCommand() {
        System.out.println(greetingMsg);
        ParsedCommand.Command command;
        do {
            try {
                command = new ParsedCommand(scanner.nextLine()).takeCommand();
            } catch (InvalidPassedFileException e) {
                System.out.printf("%s. %s. Try again.", e.getMessage(), e.getCause().getMessage());
                continue;
            }
            if (command.type == ParsedCommand.CommandsType.INVALID) {
                System.out.println("Wrong command, use 'help'");
                continue;
            }
            if (command.type == ParsedCommand.CommandsType.HELP) {
                System.out.println(ParsedCommand.HelpCommand.help);
                continue;
            }
            break;
        } while (true);
        return command;
    }

    public ParsedCommand.Command nextCommandOnce() {
        System.out.println(greetingMsg);
        ParsedCommand.Command command;
        try {
            command = new ParsedCommand(scanner.nextLine()).takeCommand();
        } catch (InvalidPassedFileException e) {
            throw new RuntimeException(e);
        }
        if (command.type == ParsedCommand.CommandsType.INVALID) {
            System.out.println("Wrong command, use 'help'");
        }
        if (command.type == ParsedCommand.CommandsType.HELP) {
            System.out.println(ParsedCommand.HelpCommand.help);
        }
        return command;
    }

    public void displayMsg(final String msg) {
        System.out.println(msg);
    }
}

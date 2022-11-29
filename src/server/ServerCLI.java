package server;

import java.util.Scanner;

public class ServerCLI {
    private final Database serverDatabase;
    //temporary field
    private final String[] availableFileNames = {
            "file1",
            "file2",
            "file3",
            "file4",
            "file5",
            "file6",
            "file7",
            "file8",
            "file9",
            "file10"
    };

    public ServerCLI(Database serverDatabase) {
        this.serverDatabase = serverDatabase;
    }

    public void work() {
        Scanner scanner = new Scanner(System.in);
        String command;
        do {
            command = scanner.nextLine();
        } while (chooseAction(command));
    }

    private boolean chooseAction(String command) {
        String[] splittedCommand = command.split(" ");
        String action = splittedCommand[0];
        String fileName = "";
        if (splittedCommand.length == 2)
            fileName = splittedCommand[1];
        else if (splittedCommand.length > 2) {
            System.out.println("error: too much words in command");
            return true;
        }
        switch (action) {
            case "add" -> addAction(fileName);
            case "get" -> getAction(fileName);
            case "delete" -> deleteAction(fileName);
            case "exit" -> {
                return false;
            }
            default -> System.out.println("error: no such command");
        }
        return true;
    }

    private void addAction(String fileName) {
        if (isFileNameAvailable(fileName) &&
                !serverDatabase.containsFIle(fileName)) {
            File newFile = new File(fileName);
            serverDatabase.addFile(newFile);
            System.out.println("The file " + fileName + " added successfully");
        } else {
            System.out.println("Cannot add the file " + fileName);
        }
    }

    private void getAction(String fileName) {
        if (serverDatabase.containsFIle(fileName)) {
            File file = serverDatabase.getFIle(fileName);
            System.out.println("The file " + file.getName() + " was sent");
        } else {
            System.out.println("The file " + fileName + " not found");
        }
    }

    private void deleteAction(String fileName) {
        if (serverDatabase.containsFIle(fileName)) {
            serverDatabase.deleteFile(fileName);
            System.out.println("The file " + fileName + " was deleted");
        } else {
            System.out.println("The file " + fileName + " not found");
        }
    }

    private boolean isFileNameAvailable(String fileName) {
        for (String availableFileName : availableFileNames) {
            if (fileName.equals(availableFileName))
                return true;
        }
        return false;
    }
}

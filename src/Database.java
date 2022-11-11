package server;

import java.util.HashMap;

public class Database {
    HashMap<String, File> files = new HashMap<>();

    public void addFile(String fileName) {
        File newFile = new File(fileName);
        files.put(fileName, newFile);
    }

    public File getFIle(String fileName) {
        if (files.containsKey(fileName))
            return files.get(fileName);
        else
            return null;
    }

    public void deleteFile(String fileName) {
        files.remove(fileName);
    }
}

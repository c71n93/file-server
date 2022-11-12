package server;

import java.util.HashMap;

public class Database {
    private HashMap<String, File> files = new HashMap<>();

    public void addFile(File newFile) {
        files.put(newFile.getName(), newFile);
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

    public boolean containsFIle(String fileName) {
        return files.containsKey(fileName);
    }
}

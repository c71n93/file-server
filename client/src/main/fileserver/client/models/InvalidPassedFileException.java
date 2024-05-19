package fileserver.client.models;

import java.io.IOException;

/**
 * Signals that path to file that was passed using CLI commands was invalid (can't be opened, can't
 * be created, is directory)
 */
public class InvalidPassedFileException extends IOException {
    public InvalidPassedFileException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

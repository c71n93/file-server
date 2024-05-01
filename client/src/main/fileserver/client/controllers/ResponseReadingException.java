package fileserver.client.controllers;

import java.io.IOException;

public class ResponseReadingException extends IOException {
    public ResponseReadingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

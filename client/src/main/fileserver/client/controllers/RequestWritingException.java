package fileserver.client.controllers;

import java.io.IOException;

public class RequestWritingException extends IOException {
    public RequestWritingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

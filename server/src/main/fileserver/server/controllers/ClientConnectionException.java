package fileserver.server.controllers;

import java.io.IOException;

public class ClientConnectionException extends IOException {
    public ClientConnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

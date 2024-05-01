package fileserver.client.controllers;

import java.io.IOException;

public class ServerConnectionException extends IOException {
    public ServerConnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

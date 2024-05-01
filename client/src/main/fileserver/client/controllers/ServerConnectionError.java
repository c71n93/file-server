package fileserver.client.controllers;

import java.io.IOException;

public class ServerConnectionError extends IOException {
    public ServerConnectionError(String msg, Throwable err) {
        super(msg, err);
    }
}

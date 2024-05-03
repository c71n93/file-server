package fileserver.server.controllers;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketException extends IOException {
    public ServerSocketException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

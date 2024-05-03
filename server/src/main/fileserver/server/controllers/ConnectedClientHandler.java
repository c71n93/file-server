package fileserver.server.controllers;

import fileserver.server.models.responders.ResponseWritingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import fileserver.common.httpc.request.Request;
import fileserver.server.models.connection.ClientConnection;
import fileserver.server.models.responders.RequestResponderFactory;
import fileserver.server.models.responders.RequestResponder;

public final class ConnectedClientHandler {
    private final Path dataFolder;
    private final ClientConnection connection;

    public ConnectedClientHandler(Path dataFolder, ClientConnection connection) {
        this.dataFolder = dataFolder;
        this.connection = connection;
    }

    public void work() throws IOException {
        try {
            Files.createDirectories(dataFolder);
            Request request;
            do {
                request = (Request) connection.requestIS().readObject();
            } while(handleNextRequest(request));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class of serializable request cannot be found.", e);
        }
    }

    public void workOnce() throws IOException {
        try {
            Files.createDirectories(dataFolder);
            Request request;
            request = (Request) connection.requestIS().readObject();
            handleNextRequest(request);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class of serializable request cannot be found.", e);
        }
    }

    private boolean handleNextRequest(Request request) throws ResponseWritingException {
        final RequestResponder responder = new RequestResponderFactory(
            request,
            connection,
            dataFolder
        ).make();
        responder.executeAndRespond();
        return !responder.isClose();
    }
}

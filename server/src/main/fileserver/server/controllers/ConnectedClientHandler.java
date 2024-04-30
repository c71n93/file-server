package fileserver.server.controllers;

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

    public void work() {
        try {
            Files.createDirectories(dataFolder);
            Request request;
            do {
                request = (Request) connection.requestIS().readObject();
            } while(handleNextRequest(request));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void workOnce() {
        try {
            Files.createDirectories(dataFolder);
            Request request;
            request = (Request) connection.requestIS().readObject();
            handleNextRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean handleNextRequest(Request request) {
        final RequestResponder responder = new RequestResponderFactory(
            request,
            connection,
            dataFolder
        ).make();
        responder.executeAndRespond();
        return !responder.isClose();
    }
}

package fileserver.server.models.responders;

import fileserver.server.models.connection.ClientConnection;

public abstract class InfoRequestResponder implements RequestResponder {
    protected final ClientConnection connection;

    protected InfoRequestResponder(final ClientConnection connection) {
        this.connection = connection;
    }
}

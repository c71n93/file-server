package fileserver.server.models.responders;

import fileserver.common.httpc.response.Response;
import fileserver.server.models.connection.ClientConnection;

import java.io.IOException;

public final class BadRequestResponder extends InfoRequestResponder {
    public BadRequestResponder(final ClientConnection connection) {
        super(connection);
    }

    @Override
    public void executeAndRespond() {
        try {
            connection.responseOS().writeObject(new Response(Response.ResponseType.BAD_REQUEST));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isClose() {
        return false;
    }
}

package fileserver.server.models.responders;

import fileserver.common.httpc.response.Response;
import fileserver.server.models.connection.ClientConnection;

import java.io.IOException;

public final class CloseRequestResponder extends InfoRequestResponder {
    public CloseRequestResponder(ClientConnection connection) {
        super(connection);
    }

    @Override
    public void executeAndRespond() throws ResponseWritingException {
        try {
            connection.responseOS().writeObject(new Response(Response.ResponseType.OK));
        } catch (IOException e) {
            throw new ResponseWritingException("Unable to send response to client", e);
        }
    }

    @Override
    public boolean isClose() {
        return true;
    }
}

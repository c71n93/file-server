package fileserver.server.models.responders;

import fileserver.common.httpc.request.DeleteRequest;
import fileserver.common.httpc.response.Response;
import fileserver.server.models.connection.ClientConnection;

import java.io.IOException;
import java.nio.file.Path;

public final class DeleteRequestResponder extends DataRequestResponder {

    public DeleteRequestResponder(
        final DeleteRequest request,
        final ClientConnection connection,
        final Path dataFolder
    ) {
        super(request, connection, dataFolder);
    }

    @Override
    public void executeAndRespond() {
        try {
            if (!this.file.exists()) {
                connection.responseOS().writeObject(new Response(Response.ResponseType.NOT_FOUND));
            } else {
                this.file.delete();
                connection.responseOS().writeObject(new Response(Response.ResponseType.OK));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

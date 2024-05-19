package fileserver.server.models.responders;

import fileserver.common.httpc.request.PutRequest;
import fileserver.common.httpc.response.Response;
import fileserver.server.models.connection.ClientConnection;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public final class PutRequestResponder extends DataRequestResponder {

    public PutRequestResponder(
        final PutRequest request,
        final ClientConnection connection,
        final Path dataFolder
    ) {
        super(request, connection, dataFolder);
    }

    @Override
    public void executeAndRespond() throws ResponseWritingException {
        try {
            if (this.file.exists() || this.file.isDirectory()) {
                this.connection.responseOS().writeObject(
                    new Response(Response.ResponseType.FORBIDDEN)
                );
            } else {
                try (final FileWriter writer = new FileWriter(this.file)) {
                    writer.write(((PutRequest) this.request).getFileContent());
                } catch (IOException e) {
                    this.connection.responseOS().writeObject(
                        new Response(Response.ResponseType.FORBIDDEN)
                    );
                    return;
                }
                this.connection.responseOS().writeObject(new Response(Response.ResponseType.OK));
            }
        } catch (IOException e) {
            throw new ResponseWritingException("Unable to send response to client", e);
        }
    }
}

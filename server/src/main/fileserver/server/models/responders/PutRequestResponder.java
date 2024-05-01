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
    public void executeAndRespond() {
        try {
            if (this.file.exists() || this.file.isDirectory()) {
                this.connection.responseOS().writeObject(
                    new Response(Response.ResponseType.FORBIDDEN)
                );
            } else {
                final FileWriter writer = new FileWriter(this.file);
                writer.write(((PutRequest) this.request).getFileContent());
                writer.close();
                this.connection.responseOS().writeObject(new Response(Response.ResponseType.OK));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

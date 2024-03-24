package fileserver.server.models.responders;

import fileserver.common.httpc.request.GetRequest;
import fileserver.common.httpc.response.Response;
import fileserver.common.httpc.response.ResponseWithContent;
import fileserver.server.models.connection.ClientConnection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class GetRequestResponder extends DataRequestResponder {
    public GetRequestResponder(
        final GetRequest request,
        final ClientConnection connection,
        final Path dataFolder
    ) {
        super(request, connection, dataFolder);
    }

    @Override
    public void executeAndRespond() {
        try {
            if (!this.file.exists()) {
                this.connection.responseOS().writeObject(
                    new Response(Response.ResponseType.NOT_FOUND)
                );
            } else {
                final String content = new String(Files.readAllBytes(this.file.toPath()));
                this.connection.responseOS().writeObject(new ResponseWithContent(content));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

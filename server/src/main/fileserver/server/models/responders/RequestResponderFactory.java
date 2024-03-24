package fileserver.server.models.responders;

import fileserver.common.httpc.request.DeleteRequest;
import fileserver.common.httpc.request.GetRequest;
import fileserver.common.httpc.request.PutRequest;
import fileserver.common.httpc.request.Request;
import fileserver.server.models.connection.ClientConnection;

import java.nio.file.Path;

public final class RequestResponderFactory {
    private final Request request;
    private final ClientConnection connection;
    private final Path dataFolder;

    public RequestResponderFactory(Request request, ClientConnection connection, Path dataFolder) {
        this.request = request;
        this.connection = connection;
        this.dataFolder = dataFolder;
    }

    public RequestResponder make() {
        return switch (request.getType()) {
            case GET -> new GetRequestResponder((GetRequest) request, connection, dataFolder);
            case DELETE ->
                new DeleteRequestResponder((DeleteRequest) request, connection, dataFolder);
            case PUT -> new PutRequestResponder((PutRequest) request, connection, dataFolder);
            case CLOSE -> new CloseRequestResponder(connection);
            default -> new BadRequestResponder(connection);
        };
    }
}

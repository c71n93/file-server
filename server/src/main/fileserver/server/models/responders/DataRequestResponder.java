package fileserver.server.models.responders;

import fileserver.common.httpc.request.DataRequest;
import fileserver.server.models.connection.ClientConnection;

import java.io.File;
import java.nio.file.Path;

public abstract class DataRequestResponder implements RequestResponder {
    protected final DataRequest request;
    protected final ClientConnection connection;
    protected final File file;

    protected DataRequestResponder(
        final DataRequest request,
        final ClientConnection connection,
        final Path dataFolder
    ) {
        this.request = request;
        this.connection = connection;
        this.file = dataFolder.resolve(this.request.getFileName()).toFile();
    }

    @Override
    public boolean isClose() {
        return false;
    }
}

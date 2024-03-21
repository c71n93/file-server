package fileserver.client.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ServerConnection implements AutoCloseable {
    public final ObjectOutputStream requestOS;
    public final ObjectInputStream responseIS;
    public ServerConnection(final OutputStream requestOS, final InputStream responseIS) throws IOException {
        this.requestOS = new ObjectOutputStream(requestOS);
        this.responseIS = new ObjectInputStream(responseIS);
    }

    @Override
    public void close() throws IOException {
        this.requestOS.close();
        this.responseIS.close();
    }
}

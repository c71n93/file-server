package fileserver.server.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class ClientSocketConnection implements ClientConnection {
    private final Socket socket;
    private final ObjectOutputStream responseOS;
    private final ObjectInputStream requestIS;

    public ClientSocketConnection(final Socket socket) throws IOException {
        this.socket = socket;
        this.responseOS = new ObjectOutputStream(socket.getOutputStream());
        this.requestIS = new ObjectInputStream(socket.getInputStream());
    }


    @Override
    public void close() throws IOException {
        socket.close();
        requestIS.close();
        responseOS.close();
    }
    @Override
    public ObjectOutputStream responseOS() {
        return responseOS;
    }
    @Override
    public ObjectInputStream requestIS() {
        return requestIS;
    }
}

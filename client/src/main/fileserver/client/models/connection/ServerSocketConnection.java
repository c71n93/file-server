package fileserver.client.models.connection;

import fileserver.client.controllers.ServerConnectionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public final class ServerSocketConnection implements ServerConnection {
    private final Socket socket;
    private final ObjectOutputStream requestOS;
    private final ObjectInputStream responseIS;

    public ServerSocketConnection(final Socket socket) throws IOException {
        this.socket = socket;
        this.requestOS = new ObjectOutputStream(this.socket.getOutputStream());
        this.responseIS = new ObjectInputStream(this.socket.getInputStream());
    }
    public ServerSocketConnection(final InetAddress serverAddress, final int serverPort) throws IOException {
        try {
            this.socket = new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            throw new ServerConnectionException("Can't create client server connection. " + e.getMessage(), e);
        }
        this.requestOS = new ObjectOutputStream(this.socket.getOutputStream());
        this.responseIS = new ObjectInputStream(this.socket.getInputStream());
    }

    @Override
    public void close() throws IOException {
        this.requestOS.close();
        this.responseIS.close();
    }
    @Override
    public ObjectOutputStream requestOS() {
        return this.requestOS;
    }
    @Override
    public ObjectInputStream responseIS() {
        return this.responseIS;
    }
}

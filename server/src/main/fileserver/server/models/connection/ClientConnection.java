package fileserver.server.models.connection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface ClientConnection extends AutoCloseable {
    ObjectOutputStream responseOS();
    ObjectInputStream requestIS();
}

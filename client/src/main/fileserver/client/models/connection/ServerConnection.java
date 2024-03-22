package fileserver.client.models.connection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface ServerConnection extends AutoCloseable {
    ObjectOutputStream requestOS();
    ObjectInputStream responseIS();
}

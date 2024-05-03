package fileserver.server.models.responders;

import java.io.IOException;

public class ResponseWritingException extends IOException {
    public ResponseWritingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

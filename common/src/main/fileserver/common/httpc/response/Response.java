package fileserver.common.httpc.response;

import java.io.Serializable;

public class Response implements Serializable {
    public enum ResponseType {
        OK(200),
        BAD_REQUEST(400),
        FORBIDDEN(403),
        NOT_FOUND(404);

        public final int code;

        ResponseType(int code) {
            this.code = code;
        }

        public static ResponseType value(int code) {
            for (ResponseType r : ResponseType.values()) {
                if (r.code == code) {
                    return r;
                }
            }
            throw new IllegalArgumentException("Invalid response code");
        }
    }

    private final ResponseType type;

    public Response(ResponseType type) {
        this.type = type;
    }

    public ResponseType getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return this.type == ((Response) other).type;
    }
}

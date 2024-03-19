package fileserver.common.http.response;

import java.io.Serializable;

public class Response implements Serializable {
    public enum ResponseType {
        OK(200),
        BAD_REQUEST(400),
        FORBIDDEN(403),
        NOT_FOUND(404);

        public final int code;

        private ResponseType(int code) {
            this.code = code;
        }

        public static ResponseType valueOfCode(int code) {
            for (ResponseType r : values()) {
                if (r.code == code) {
                    return r;
                }
            }
            return null;
        }
    }

    private final ResponseType type;

    public Response(ResponseType type) {
        this.type = type;
    }

    public ResponseType getType() {
        return type;
    }

}

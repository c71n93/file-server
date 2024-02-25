package fileserver.common.http;

public enum Response {
    OK(200),
    BAD_REQUEST(400),
    FORBIDDEN(403),
    NOT_FOUND(404);

    public final int code;

    private Response(int code) {
        this.code = code;
    }

    public static Response valueOfCode(int code) {
        for (Response r : values()) {
            if (r.code == code) {
                return r;
            }
        }
        return null;
    }
}

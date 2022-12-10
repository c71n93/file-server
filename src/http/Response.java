package http;

public enum Response {
    OK(200),
    BAD_REQUEST(400),
    FORBIDDEN(403),
    NOT_FOUND(404);

    public final int code;

    private Response(int code) {
        this.code = code;
    }
}

package fileserver.common.http.request;

public class BadRequest extends Request {
    public BadRequest() {
        super(RequestType.BAD);
    }
}

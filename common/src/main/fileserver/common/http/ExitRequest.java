package fileserver.common.http;

public class ExitRequest extends Request {
    public ExitRequest() {
        super(RequestType.EXIT);
    }
}
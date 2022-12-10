package http;

public class GetRequest extends Request {
    private String fileName;

    public GetRequest(String fileName) {
        super(RequestType.GET);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
package http;

public class DeleteRequest extends Request {
    private String fileName;

    public DeleteRequest(String fileName) {
        super(RequestType.DELETE);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}

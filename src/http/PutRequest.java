package http;

public class PutRequest extends Request {
    private String fileName;
    private String fileContent;

    public PutRequest(String fileName, String fileContent) {
        super(RequestType.PUT);
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileContent() {
        return fileContent;
    }
}
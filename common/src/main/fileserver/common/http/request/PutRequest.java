package fileserver.common.http.request;

import fileserver.common.http.request.Request;

public class PutRequest extends Request {
    private final String fileName;
    private final String fileContent;

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
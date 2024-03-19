package fileserver.common.http.request;

import fileserver.common.http.request.Request;

public class DeleteRequest extends Request {
    private final String fileName;

    public DeleteRequest(String fileName) {
        super(RequestType.DELETE);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}

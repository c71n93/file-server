package fileserver.common.http.request;

import fileserver.common.http.request.Request;

public class GetRequest extends Request {
    private final String fileName;

    public GetRequest(String fileName) {
        super(RequestType.GET);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
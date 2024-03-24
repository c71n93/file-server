package fileserver.common.httpc.request;

public abstract class DataRequest extends Request {
    protected final String fileName;
    public DataRequest(RequestType requestType, String fileName) {
        super(requestType);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}

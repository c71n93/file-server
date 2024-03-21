package fileserver.common.http.request;

public final class GetRequest extends Request {
    private final String fileName;

    public GetRequest(String fileName) {
        super(RequestType.GET);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object other) {
        if (super.equals(other)) {
            GetRequest that = (GetRequest) other;
            return this.fileName.equals(that.fileName);
        } else {
            return false;
        }
    }
}
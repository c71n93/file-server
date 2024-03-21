package fileserver.common.http.request;

public final class DeleteRequest extends Request {
    private final String fileName;

    public DeleteRequest(String fileName) {
        super(RequestType.DELETE);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object other) {
        if (super.equals(other)) {
            DeleteRequest that = (DeleteRequest) other;
            return this.fileName.equals(that.fileName);
        } else {
            return false;
        }
    }
}

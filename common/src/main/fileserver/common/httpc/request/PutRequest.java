package fileserver.common.httpc.request;

public final class PutRequest extends DataRequest {
    private final String fileContent;

    public PutRequest(String fileName, String fileContent) {
        super(RequestType.PUT, fileName);
        this.fileContent = fileContent;
    }

    public String getFileContent() {
        return fileContent;
    }

    @Override
    public boolean equals(Object other) {
        if (super.equals(other)) {
            PutRequest that = (PutRequest) other;
            return this.fileContent.equals(that.fileContent) && this.fileName.equals(that.fileName);
        } else {
            return false;
        }
    }
}
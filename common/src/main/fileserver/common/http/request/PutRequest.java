package fileserver.common.http.request;

public final class PutRequest extends Request {
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
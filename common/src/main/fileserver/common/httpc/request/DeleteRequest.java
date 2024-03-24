package fileserver.common.httpc.request;

public final class DeleteRequest extends DataRequest {
    public DeleteRequest(final String fileName) {
        super(RequestType.DELETE, fileName);
    }

    @Override
    public boolean equals(final Object other) {
        if (super.equals(other)) {
            DeleteRequest that = (DeleteRequest) other;
            return this.fileName.equals(that.fileName);
        } else {
            return false;
        }
    }
}

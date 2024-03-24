package fileserver.common.httpc.request;

public final class GetRequest extends DataRequest {
    public GetRequest(final String fileName) {
        super(RequestType.GET, fileName);
    }

    @Override
    public boolean equals(final Object other) {
        if (super.equals(other)) {
            GetRequest that = (GetRequest) other;
            return this.fileName.equals(that.fileName);
        } else {
            return false;
        }
    }
}
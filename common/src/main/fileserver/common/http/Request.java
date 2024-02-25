package fileserver.common.http;

public class Request {
    public enum RequestType {
        GET, PUT, DELETE, BAD, EXIT
    }

    private RequestType requestType;
    
    public Request(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
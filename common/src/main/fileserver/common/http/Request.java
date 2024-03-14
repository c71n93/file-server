package fileserver.common.http;

public class Request {
    public enum RequestType {
        GET, PUT, DELETE, BAD
    }

    private final RequestType requestType;
    
    public Request(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
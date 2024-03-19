package fileserver.common.http.request;

import java.io.Serializable;

public class Request implements Serializable {
    public enum RequestType {
        GET, PUT, DELETE, CLOSE, BAD
    }

    private final RequestType type;
    
    public Request(RequestType requestType) {
        this.type = requestType;
    }

    public RequestType getType() {
        return type;
    }
}
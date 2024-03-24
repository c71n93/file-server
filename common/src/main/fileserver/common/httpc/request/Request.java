package fileserver.common.httpc.request;

import java.io.Serializable;

public class Request implements Serializable {
    public enum RequestType {
        GET, PUT, DELETE, CLOSE
    }

    private final RequestType type;
    
    public Request(RequestType requestType) {
        this.type = requestType;
    }

    public RequestType getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return this.type == ((Request) other).type;
    }
}
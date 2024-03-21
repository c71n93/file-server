package fileserver.common.http.response;

import fileserver.common.http.request.GetRequest;

public class ResponseWithContent extends Response {
    String content;
    public ResponseWithContent(String content) {
        super(ResponseType.OK);
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public boolean equals(Object other) {
        if (super.equals(other)) {
            ResponseWithContent that = (ResponseWithContent) other;
            return this.content.equals(that.content);
        } else {
            return false;
        }
    }
}

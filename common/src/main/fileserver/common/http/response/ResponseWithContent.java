package fileserver.common.http.response;

public class ResponseWithContent extends Response {
    String content;
    public ResponseWithContent(ResponseType type, String content) {
        super(type);
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}

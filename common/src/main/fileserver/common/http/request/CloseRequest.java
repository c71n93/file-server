package fileserver.common.http.request;


// TODO: This is not HTTP request (extreme measures of bad design)
public class CloseRequest extends Request{
    public CloseRequest() {
        super(RequestType.CLOSE);
    }
}

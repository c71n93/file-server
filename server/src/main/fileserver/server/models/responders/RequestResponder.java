package fileserver.server.models.responders;

public interface RequestResponder {
    void executeAndRespond() throws ResponseWritingException;
    boolean isClose();
}

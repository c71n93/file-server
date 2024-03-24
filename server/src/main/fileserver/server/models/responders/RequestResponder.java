package fileserver.server.models.responders;

public interface RequestResponder {
    void executeAndRespond();
    boolean isClose();
}

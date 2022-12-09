package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerRequestHeandler {
    private final Database serverDatabase;
    private DataInputStream messageInputStream;
    private DataOutputStream messageOutputStream;


    public ServerRequestHeandler(Database serverDatabase, DataInputStream messageInputStream, DataOutputStream messageOutputStream) {
        this.serverDatabase = serverDatabase;
        this.messageInputStream = messageInputStream;
        this.messageOutputStream = messageOutputStream;
    }

    public void work() {
        try {
            HTTPRequest request;
            do {
                String stringRequest = messageInputStream.readUTF();
                request = parseStringRequest(stringRequest);
            } while(handleNextRequest(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HTTPRequest parseStringRequest(String stringRequest) {
        HTTPRequest request;
        String[] splittedStringRequest = stringRequest.split(" ");
        if (splittedStringRequest.length < 2) {
            request = new HTTPRequest(HTTPRequest.Request.BAD);
        }
        switch (splittedStringRequest[0]) {
            case "GET" -> {
                if (splittedStringRequest.length > 2) {
                    request = new HTTPRequest(HTTPRequest.Request.BAD);
                } else {
                    String fileName = splittedStringRequest[1];
                    request = new GETRequest(fileName);
                }
            }
            case "DELETE" -> {
                if (splittedStringRequest.length > 2) {
                    request = new HTTPRequest(HTTPRequest.Request.BAD);
                } else {
                    String fileName = splittedStringRequest[1];
                    request = new DELETERequest(fileName);
                }
            }
            case "PUT" -> {
                String fileName = splittedStringRequest[1];
                String fileContent = getFileContentFromRequest(stringRequest);
                request = new PUTRequest(fileName, fileContent);
            }
            default -> request = new HTTPRequest(HTTPRequest.Request.BAD);

        }
        return request;
    }

    private String getFileContentFromRequest(String stringRequest) {
        // Here we find an index of the second " ". After this index, the content starts.
        int beginIndexOfContent = stringRequest.indexOf(" ", stringRequest.indexOf(" ") + 1) + 1;
        return beginIndexOfContent == 0 ? "" : stringRequest.substring(beginIndexOfContent);
    }

    private boolean handleNextRequest(HTTPRequest request) {
        //TODO: Add request END to turn off the server
        boolean isNextRequest = false;

        switch (request.getRequestType()) {
            case GET -> getRequest((GETRequest)request);
            case DELETE -> deleteRequest((DELETERequest)request);
            case PUT -> putRequest((PUTRequest)request);
            case BAD -> badRequest();
        }

        return isNextRequest;
    }

    //TODO: implement Request funstions
    private void getRequest(GETRequest request) {
    }

    private void putRequest(PUTRequest request) {
    }

    private void deleteRequest(DELETERequest request) {
    }

    private void badRequest() {
    }
}

class HTTPRequest {
    public enum Request {
        GET, PUT, DELETE, BAD
    }

    private Request requestType;
    
    public HTTPRequest(Request requestType) {
        this.requestType = requestType;
    }

    public Request getRequestType() {
        return requestType;
    }
}

class GETRequest extends HTTPRequest {
    private String fileName;

    public GETRequest(String fileName) {
        super(Request.GET);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}

class DELETERequest extends HTTPRequest {
    private String fileName;

    public DELETERequest(String fileName) {
        super(Request.DELETE);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}

class PUTRequest extends HTTPRequest {
    private String fileName;
    private String fileContent;

    public PUTRequest(String fileName, String fileContent) {
        super(Request.DELETE);
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileContent() {
        return fileContent;
    }
}
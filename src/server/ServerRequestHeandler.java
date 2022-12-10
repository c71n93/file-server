package server;

import java.io.*;
import http.*;

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
            Request request;
            do {
                String stringRequest = messageInputStream.readUTF();
                request = parseStringRequest(stringRequest);
            } while(handleNextRequest(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Request parseStringRequest(String stringRequest) {
        Request request;
        String[] splittedStringRequest = stringRequest.split(" ");
        if (splittedStringRequest.length < 2) {
            request = new Request(Request.RequestType.BAD);
        }
        switch (splittedStringRequest[0]) {
            case "GET" -> {
                if (splittedStringRequest.length > 2) {
                    request = new Request(Request.RequestType.BAD);
                } else {
                    String fileName = splittedStringRequest[1];
                    request = new GetRequest(fileName);
                }
            }
            case "DELETE" -> {
                if (splittedStringRequest.length > 2) {
                    request = new Request(Request.RequestType.BAD);
                } else {
                    String fileName = splittedStringRequest[1];
                    request = new DeleteRequest(fileName);
                }
            }
            case "PUT" -> {
                String fileName = splittedStringRequest[1];
                String fileContent = getFileContentFromRequest(stringRequest);
                request = new PutRequest(fileName, fileContent);
            }
            default -> request = new Request(Request.RequestType.BAD);

        }
        return request;
    }

    private String getFileContentFromRequest(String stringRequest) {
        // Here we find an index of the second " ". After this index, the content starts.
        int beginIndexOfContent = stringRequest.indexOf(" ", stringRequest.indexOf(" ") + 1) + 1;
        return beginIndexOfContent == 0 ? "" : stringRequest.substring(beginIndexOfContent);
    }

    private boolean handleNextRequest(Request request) {
        //TODO: Add request END to turn off the server
        boolean isNextRequest = false;

        switch (request.getRequestType()) {
            case GET -> getRequest((GetRequest)request);
            case DELETE -> deleteRequest((DeleteRequest)request);
            case PUT -> putRequest((PutRequest)request);
            case BAD -> badRequest();
        }

        return isNextRequest;
    }

    //TODO: implement Request funstions
    private void getRequest(GetRequest request) {
        try {
            messageOutputStream.writeInt(Response.OK.code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putRequest(PutRequest request) {
        try {
            messageOutputStream.writeInt(Response.OK.code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteRequest(DeleteRequest request) {
        try {
            messageOutputStream.writeInt(Response.OK.code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void badRequest() {
        try {
            messageOutputStream.writeInt(Response.BAD_REQUEST.code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import http.*;

public class ServerRequestHandler {
    private DataInputStream messageInputStream;
    private DataOutputStream messageOutputStream;
    private String dataFolder = "./src/server/data/folder/"; //TODO: make a possibility to change this folder

    public ServerRequestHandler(DataInputStream messageInputStream, DataOutputStream messageOutputStream) {
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

    private void getRequest(GetRequest request) {
        String filePath = dataFolder + "/" + request.getFileName();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                messageOutputStream.writeInt(Response.NOT_FOUND.code);
            } else {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                messageOutputStream.writeInt(Response.OK.code);
                messageOutputStream.writeUTF(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putRequest(PutRequest request) {
        String filePath = dataFolder + "/" + request.getFileName();
        try {
            File file = new File(filePath);
            if (file.exists() || file.isDirectory()) {
                messageOutputStream.writeInt(Response.FORBIDDEN.code);
            } else {
                FileWriter writer = new FileWriter(file);
                writer.write(request.getFileContent());
                writer.close();
                messageOutputStream.writeInt(Response.OK.code);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteRequest(DeleteRequest request) {
        String filePath = dataFolder + "/" + request.getFileName();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                messageOutputStream.writeInt(Response.NOT_FOUND.code);
            } else {
                file.delete();
                messageOutputStream.writeInt(Response.OK.code);
            }
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
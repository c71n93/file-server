package fileserver.server.controllers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fileserver.common.http.request.DeleteRequest;
import fileserver.common.http.request.GetRequest;
import fileserver.common.http.request.PutRequest;
import fileserver.common.http.request.Request;
import fileserver.common.http.response.Response;
import fileserver.common.http.response.ResponseWithContent;

// TODO: rename this class
public class ServerRequestHandler implements AutoCloseable {
    final private Path dataFolder;
    // TODO: join it into ServerConnection class
    final private ObjectInputStream requestIS;
    final private ObjectOutputStream responseOS;

    public ServerRequestHandler(Path dataFolder, InputStream requestIS, OutputStream responseOS) throws IOException {
        this.dataFolder = dataFolder;
        this.responseOS = new ObjectOutputStream(responseOS);
        this.requestIS = new ObjectInputStream(requestIS);
    }

    public void work() {
        try {
            Files.createDirectories(dataFolder);
            Request request;
            do {
                request = (Request) requestIS.readObject();
            } while(handleNextRequest(request));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void workOnce() {
        try {
            Files.createDirectories(dataFolder);
            Request request;
            request = (Request) requestIS.readObject();
            handleNextRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean handleNextRequest(Request request) {
        //TODO: make one client work in a loop, and server handle its requests in a loop
        switch (request.getType()) {
            case GET -> getRequest((GetRequest)request);
            case DELETE -> deleteRequest((DeleteRequest)request);
            case PUT -> putRequest((PutRequest)request);
            case CLOSE -> {
                closeRequest();
                return false;
            }
            default -> badRequest();
        }
        return true;
    }

    private void getRequest(GetRequest request) {
        String filePath = dataFolder + "/" + request.getFileName();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                responseOS.writeObject(new Response(Response.ResponseType.NOT_FOUND));
            } else {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                responseOS.writeObject(new ResponseWithContent(content));
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
                responseOS.writeObject(new Response(Response.ResponseType.NOT_FOUND));
            } else {
                FileWriter writer = new FileWriter(file);
                writer.write(request.getFileContent());
                writer.close();
                responseOS.writeObject(new Response(Response.ResponseType.OK));
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
                responseOS.writeObject(new Response(Response.ResponseType.NOT_FOUND));
            } else {
                file.delete();
                responseOS.writeObject(new Response(Response.ResponseType.OK));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void badRequest() {
        try {
            responseOS.writeObject(new Response(Response.ResponseType.BAD_REQUEST));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeRequest() {
        try {
            responseOS.writeObject(new Response(Response.ResponseType.OK));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        responseOS.close();
        requestIS.close();
    }
}

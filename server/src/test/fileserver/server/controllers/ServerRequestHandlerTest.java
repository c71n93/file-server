package fileserver.server.controllers;

import fileserver.common.http.request.CloseRequest;
import fileserver.common.http.request.DeleteRequest;
import fileserver.common.http.request.GetRequest;
import fileserver.common.http.request.PutRequest;
import fileserver.common.http.request.Request;
import fileserver.common.http.response.Response;
import fileserver.common.http.response.ResponseWithContent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ServerRequestHandlerTest {
    static final Path initial = new File("file").toPath();
    static final Path fresh = new File("file-new").toPath();
    static final String content = "content";

    @ParameterizedTest
    @MethodSource("testArgsProvider")
    public void commandToRequestTest(final TestArg params, @TempDir Path tmpDir) throws IOException, ClassNotFoundException {
        File init = tmpDir.resolve(ServerRequestHandlerTest.initial).toFile();
        init.createNewFile();
        try (FileWriter initWriter = new FileWriter(init)) {
            initWriter.write(content);
        }
        final ByteArrayInputStream requestIS = getAppropriateRequestIS(params.request);
        final ByteArrayOutputStream responseOS = new ByteArrayOutputStream();
        new ServerRequestHandler(tmpDir, requestIS, responseOS).workOnce();
        final ObjectInputStream resultIS = new ObjectInputStream(
            new ByteArrayInputStream(responseOS.toByteArray())
        );
        Assertions.assertEquals(params.response, resultIS.readObject());
    }

    private ByteArrayInputStream getAppropriateRequestIS(Request request) throws IOException {
        final ByteArrayOutputStream tmpBOS = new ByteArrayOutputStream();
        final ObjectOutputStream tmpObOS = new ObjectOutputStream(tmpBOS);
        tmpObOS.writeObject(request);
        return new ByteArrayInputStream(tmpBOS.toByteArray());
    }

    /**
     * Input arguments for unit tests.
     *
     * @return Stream of arguments.
     */
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private static Stream<Arguments> testArgsProvider() {
        return Stream.of(
            Arguments.of(
                new TestArg(
                    new GetRequest(initial.toString()),
                    new ResponseWithContent(content)
                )
            ),
            Arguments.of(
                new TestArg(
                    new DeleteRequest(initial.toString()),
                    new Response(Response.ResponseType.OK)
                )
            ),
            Arguments.of(
                new TestArg(
                    new PutRequest(fresh.toString(), content),
                    new Response(Response.ResponseType.OK)
                )
            ),
            Arguments.of(
                new TestArg(
                    new CloseRequest(),
                    new Response(Response.ResponseType.OK)
                )
            )
        );
    }

    private record TestArg(Request request, Response response) {
    }

}

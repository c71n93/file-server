package fileserver.server.controllers;

import fileserver.common.httpc.request.CloseRequest;
import fileserver.common.httpc.request.DeleteRequest;
import fileserver.common.httpc.request.GetRequest;
import fileserver.common.httpc.request.PutRequest;
import fileserver.common.httpc.request.Request;
import fileserver.common.httpc.response.Response;
import fileserver.common.httpc.response.ResponseWithContent;
import fileserver.server.models.connection.ClientConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ConnectedClientHandlerTest {
    static final Path initial = new File("file").toPath();
    static final Path fresh = new File("file-new").toPath();
    static final String content = "content";

    @ParameterizedTest
    @MethodSource("testArgsProvider")
    public void commandToRequestTest(final TestArg params, @TempDir Path tmpDir) throws IOException, ClassNotFoundException {
        File init = tmpDir.resolve(initial).toFile();
        init.createNewFile();
        try (FileWriter initWriter = new FileWriter(init)) {
            initWriter.write(content);
        }
        final ByteArrayInputStream requestIS = getAppropriateRequestIS(params.request);
        final ByteArrayOutputStream responseOS = new ByteArrayOutputStream();
        try (ClientTestConnection connection = new ClientTestConnection(responseOS, requestIS)) {
            new ConnectedClientHandler(tmpDir, connection).workOnce();
        }
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

    private static final class ClientTestConnection implements ClientConnection {
        private final ObjectOutputStream responseOS;
        private final ObjectInputStream requestIS;

        public ClientTestConnection(final OutputStream responseOS, final InputStream requestIS) throws IOException {
            this.responseOS = new ObjectOutputStream(responseOS);
            this.requestIS = new ObjectInputStream(requestIS);
        }

        @Override
        public void close() throws IOException {
            requestIS.close();
            responseOS.close();
        }
        @Override
        public ObjectOutputStream responseOS() {
            return responseOS;
        }
        @Override
        public ObjectInputStream requestIS() {
            return requestIS;
        }
    }
}

package fileserver.client.controllers;

import fileserver.client.models.ParsedCommand;
import fileserver.client.models.connection.ServerConnection;
import fileserver.common.httpc.request.CloseRequest;
import fileserver.common.httpc.request.DeleteRequest;
import fileserver.common.httpc.request.GetRequest;
import fileserver.common.httpc.request.PutRequest;
import fileserver.common.httpc.request.Request;
import fileserver.common.httpc.response.Response;
import fileserver.common.httpc.response.ResponseWithContent;
import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Stream;

final class ConnectedServerRequesterTest {
    private static final String filename = "file";
    private static final String content = "content";
    private static final File savedContentFile = new File("./file");

    @BeforeEach
    public void init() throws IOException {
        savedContentFile.createNewFile();
        Files.writeString(savedContentFile.toPath(), content);
    }

    @AfterEach
    public void cleanUp() {
        savedContentFile.delete();
    }

    @ParameterizedTest
    @MethodSource("testArgsProvider")
    public void commandToRequestTest(final TestArg params) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream commandIS = new ByteArrayInputStream(
            params.command.getBytes(StandardCharsets.UTF_8)
        );
        final ByteArrayInputStream responseIS = getAppropriateResponseIS(params.command);
        final ByteArrayOutputStream requestOS = new ByteArrayOutputStream();
        try (ServerTestConnection connection = new ServerTestConnection(requestOS, responseIS)) {
            ConnectedServerRequester requester = new ConnectedServerRequester(
                new ClienCLI(commandIS),
                connection
            );
            if (
                params.command.equals(ParsedCommand.CommandsType.HELP.name + "\n") ||
                params.command.equals(ParsedCommand.CommandsType.INVALID.name + "\n")
            ) {
                Assertions.assertThrows(IllegalStateException.class, requester::workOnce);
                return;
            } else {
                requester.workOnce();
            }
        }
        final ObjectInputStream resultIS = new ObjectInputStream(
            new ByteArrayInputStream(requestOS.toByteArray())
        );
        Assertions.assertTrue(params.request.isPresent());
        final Request request = params.request.get();
        Assertions.assertEquals(request, resultIS.readObject());
    }

    /**
     * Creates appropriate input stream, in relation to {@code command}
     *
     * @param command Command.
     * @return Appropriate input stream
     */
    private ByteArrayInputStream getAppropriateResponseIS(String command) throws IOException {
        final ByteArrayOutputStream tmpBOS = new ByteArrayOutputStream();
        final ObjectOutputStream tmpObOS = new ObjectOutputStream(tmpBOS);
        if (command.split(" ")[0].equals(ParsedCommand.CommandsType.DOWNLOAD.name)) {
            tmpObOS.writeObject(new ResponseWithContent(content));
        } else {
           tmpObOS.writeObject(new Response(Response.ResponseType.OK));
        }
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
                    String.format("%s %s %s\n", ParsedCommand.CommandsType.DOWNLOAD.name, filename, savedContentFile.toPath()),
                    Optional.of(new GetRequest("file"))
                )
            ),
            Arguments.of(
                new TestArg(
                    String.format("%s %s %s\n", ParsedCommand.CommandsType.UPLOAD.name, savedContentFile.toPath(), filename),
                    Optional.of(new PutRequest("file", "content"))
                )
            ),
            Arguments.of(
                new TestArg(
                    String.format("%s %s\n", ParsedCommand.CommandsType.DELETE.name, filename),
                    Optional.of(new DeleteRequest("file"))
                )
            ),
            Arguments.of(
                new TestArg(
                    String.format("%s\n", ParsedCommand.CommandsType.EXIT.name),
                    Optional.of(new CloseRequest())
                )
            ),
            Arguments.of(
                new TestArg(
                    String.format("%s\n", ParsedCommand.CommandsType.HELP.name),
                    Optional.empty()
                )
            ),
            Arguments.of(
                new TestArg(
                    String.format("%s\n", ParsedCommand.CommandsType.INVALID.name),
                    Optional.empty()
                )
            )
        );
    }

    private record TestArg(String command, Optional<Request> request) {
    }

    private static final class ServerTestConnection implements ServerConnection {
        private final ObjectOutputStream requestOS;
        private final ObjectInputStream responseIS;

        ServerTestConnection(OutputStream requestOS, InputStream requestIS) throws IOException {
            this.requestOS = new ObjectOutputStream(requestOS);
            this.responseIS = new ObjectInputStream(requestIS);
        }

        @Override
        public ObjectOutputStream requestOS() {
            return requestOS;
        }
        @Override
        public ObjectInputStream responseIS() {
            return responseIS;
        }
        @Override
        public void close() throws IOException {
            requestOS.close();
            responseIS.close();
        }
    }
}

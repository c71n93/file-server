package fileserver.client.controllers;

import fileserver.client.models.ParsedCommand;
import fileserver.common.http.request.CloseRequest;
import fileserver.common.http.request.DeleteRequest;
import fileserver.common.http.request.GetRequest;
import fileserver.common.http.request.PutRequest;
import fileserver.common.http.request.Request;
import fileserver.common.http.response.Response;
import fileserver.common.http.response.ResponseWithContent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Stream;

public class ClientCLITest {
    static final String filename = "file";
    static final String content = "content";

    @ParameterizedTest
    @MethodSource("testArgsProvider")
    public void commandToRequestTest(final TestArg params) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream commandIS = new ByteArrayInputStream(
            params.command.getBytes(StandardCharsets.UTF_8)
        );
        final ByteArrayInputStream responseIS = getAppropriateResponseIS(params.command);
        final ByteArrayOutputStream requestOS = new ByteArrayOutputStream();
        new ClientCLI(commandIS, responseIS, requestOS).workOnce();
        final ObjectInputStream resultIS = new ObjectInputStream(
            new ByteArrayInputStream(requestOS.toByteArray())
        );
        if (params.command.equals(ParsedCommand.CommandsType.HELP.name + "\n")) {
            Assertions.assertEquals(0, resultIS.available());
        } else {
            Assertions.assertTrue(params.request.isPresent());
            final Request request = params.request.get();
            Assertions.assertEquals(request, resultIS.readObject());
        }
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
        if (command.split(" ")[0].equals(ParsedCommand.CommandsType.GET.name)) {
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
                    String.format("%s %s\n", ParsedCommand.CommandsType.GET.name, filename),
                    Optional.of(new GetRequest("file"))
                )
            ),
            Arguments.of(
                new TestArg(
                    String.format("%s %s %s\n", ParsedCommand.CommandsType.PUT.name, filename, content),
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
            )
        );
    }

    private record TestArg(String command, Optional<Request> request) {
    }
}

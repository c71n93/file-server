package fileserver.client.controllers;

import fileserver.common.http.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class ClientCLITest {
    @ParameterizedTest
    @MethodSource("testArgsProvider")
    public void commandToRequestTest(final String[] params) throws IOException {
        final ByteArrayInputStream commandIS = new ByteArrayInputStream(
            params[0].getBytes(StandardCharsets.UTF_8)
        );
        final ByteArrayInputStream messageIS = getAppropriateMessageIS(params[0]);
        final ByteArrayOutputStream messageOS = new ByteArrayOutputStream();
        new ClientCLI(commandIS, messageIS, messageOS).work();
        final DataInputStream result = new DataInputStream(
            new ByteArrayInputStream(messageOS.toByteArray())
        );
        if (params[0].equals("help\n") || params[0].equals("exit\n")) {
            Assertions.assertEquals(0, result.available());
        } else {
            Assertions.assertEquals(params[1], result.readUTF());
        }
    }

    /**
     * Creates appropriate input stream, in relation to {@code command}
     *
     * @param command Command.
     * @return Appropriate input stream
     */
    private ByteArrayInputStream getAppropriateMessageIS(String command) throws IOException {
        final ByteArrayOutputStream tmpBS = new ByteArrayOutputStream();
        final DataOutputStream tmpDS = new DataOutputStream(tmpBS);
        tmpDS.writeInt(Response.OK.code);
        if (command.split(" ")[0].equals("get")) {
            tmpDS.writeUTF("content");
        }
        return new ByteArrayInputStream(tmpBS.toByteArray());
    }

    /**
     * Input arguments for unit tests.
     *
     * @return Stream of arguments.
     */
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private static Stream<Arguments> testArgsProvider() {
        return Stream.of(
            Arguments.of((Object) new String[]{"get file\n", "GET file"}),
            Arguments.of((Object) new String[]{"put file content\n", "PUT file content"}),
            Arguments.of((Object) new String[]{"delete file\n", "DELETE file"}),
            Arguments.of((Object) new String[]{"exit\n", ""}),
            Arguments.of((Object) new String[]{"help\n", ""})
        );
    }
}

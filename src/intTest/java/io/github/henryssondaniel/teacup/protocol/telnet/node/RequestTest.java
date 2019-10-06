package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.protocol.telnet.server.Request;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

class RequestTest {
  private static final byte BYTE = (byte) 1;
  private static final String COMMAND = "command";

  @Test
  void createRequestBuilder() {
    Factory.createRequestBuilder()
        .setCommand(
            io.github.henryssondaniel.teacup.core.assertion.Factory.createObjectAssert()
                .isEqualTo(BYTE))
        .setCommandAsString(
            io.github.henryssondaniel.teacup.core.assertion.Factory.createStringAssert()
                .isEqualTo(COMMAND))
        .build()
        .verify(new TestRequest());
  }

  private static class TestRequest implements Request {
    private static final Logger LOGGER =
        io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(TestRequest.class);

    @Override
    public byte getCommand() {
      LOGGER.log(Level.FINE, "Get command");
      return BYTE;
    }

    @Override
    public String getCommandAsString() {
      LOGGER.log(Level.FINE, "Get command as string");
      return COMMAND;
    }
  }
}

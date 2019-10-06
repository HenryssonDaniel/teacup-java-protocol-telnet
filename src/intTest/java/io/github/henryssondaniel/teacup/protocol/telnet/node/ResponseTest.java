package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.protocol.telnet.client.Response;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

class ResponseTest {
  private static final String DATA = "data";

  @Test
  void createResponseBuilder() {
    Response response = new TestResponse();

    Factory.createResponseBuilder()
        .setAssertion(
            io.github.henryssondaniel.teacup.core.assertion.Factory.<Response>createObjectAssert()
                .isSameAs(response))
        .setData(
            io.github.henryssondaniel.teacup.core.assertion.Factory.createByteArrayAssert()
                .isEqualTo(DATA.getBytes(Charset.defaultCharset())))
        .setDataAsString(
            io.github.henryssondaniel.teacup.core.assertion.Factory.createStringAssert()
                .isEqualTo(DATA))
        .build()
        .verify(response);
  }

  private static class TestResponse implements Response {
    private static final Logger LOGGER =
        io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(TestResponse.class);

    @Override
    public byte[] getData() {
      LOGGER.log(Level.FINE, "Get data");
      return DATA.getBytes(Charset.defaultCharset());
    }

    @Override
    public String getDataAsString() {
      LOGGER.log(Level.FINE, "Get data as string");
      return DATA;
    }
  }
}

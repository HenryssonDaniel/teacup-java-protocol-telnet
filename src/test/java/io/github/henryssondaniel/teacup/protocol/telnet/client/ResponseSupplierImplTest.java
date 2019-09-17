package io.github.henryssondaniel.teacup.protocol.telnet.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ResponseSupplierImplTest {
  private static final String TEST = "test";
  private final InputStream inputStream = mock(InputStream.class);

  @Test
  void get() throws IOException {
    try (InputStream stream = new ByteArrayInputStream(TEST.getBytes(StandardCharsets.UTF_8))) {
      var responseSupplier = createResponseSupplier(stream);
      assertThat(responseSupplier.get()).isEqualTo(TEST);
      responseSupplier.interrupt();
    }
  }

  @Test
  void getWhenConnectionClosed() {
    ResponseSupplier responseSupplier = new ResponseSupplierImpl(null);
    assertThat(responseSupplier.get()).isNull();
    responseSupplier.interrupt();
  }

  @Test
  void getWhenIOException() throws IOException {
    when(inputStream.read(any(byte[].class))).thenThrow(new IOException(TEST));

    var responseSupplier = createResponseSupplier(inputStream);
    assertThat(responseSupplier.get()).isNull();
    responseSupplier.interrupt();
  }

  @Test
  void getWhenInterrupted() throws IOException {
    when(inputStream.read(any(byte[].class))).thenReturn(0);

    var responseSupplier = createResponseSupplier(inputStream);

    var thread = new Thread(responseSupplier::get);
    thread.start();
    thread.interrupt();

    responseSupplier.interrupt();

    assertThat(responseSupplier.get()).isNull();
  }

  private static ResponseSupplier createResponseSupplier(InputStream inputStream) {
    ResponseSupplier responseSupplier = new ResponseSupplierImpl(inputStream);
    responseSupplier.start();

    return responseSupplier;
  }
}

package io.github.henryssondaniel.teacup.protocol.telnet.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ResponseSupplierImplTest {
  private final InputStream inputStream = mock(InputStream.class);
  private final Object lock = new Object();
  private final Object lockSecond = new Object();

  private boolean waiting = true;
  private boolean waitingSecond = true;

  @Test
  void get() throws IOException, InterruptedException {
    when(inputStream.read(any(byte[].class))).thenReturn(1, 2).thenAnswer(invocation -> read());

    var responseSupplier = createResponseSupplier(inputStream);

    synchronized (lock) {
      while (waiting) lock.wait(1L);
    }

    var bytes = new byte[] {(byte) 0, (byte) 0, (byte) 0};

    var response = responseSupplier.get();

    assertThat(response.getData()).isEqualTo(bytes);
    assertThat(response.getDataAsString())
        .isEqualTo(new String(bytes, 0, 3, StandardCharsets.UTF_8));

    synchronized (lockSecond) {
      waitingSecond = false;
      lockSecond.notifyAll();
    }

    responseSupplier.interrupt();
  }

  @Test
  void getWhenConnectionClosed() {
    ResponseSupplier responseSupplier = new ResponseSupplierImpl(null);
    assertThat(responseSupplier.get()).isNull();
    responseSupplier.interrupt();
  }

  @Test
  void getWhenIOException() throws IOException {
    when(inputStream.read(any(byte[].class))).thenThrow(new IOException("test"));

    var responseSupplier = createResponseSupplier(inputStream);
    assertThat(responseSupplier.get()).isNull();
    responseSupplier.interrupt();
  }

  @Test
  void getWhenInterrupted() throws IOException {
    ResponseSupplier responseSupplier;

    try (var stream = InputStream.nullInputStream()) {
      responseSupplier = createResponseSupplier(stream);
    }

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

  private Object read() throws InterruptedException {
    synchronized (lock) {
      waiting = false;
      lock.notifyAll();
    }

    synchronized (lockSecond) {
      while (waitingSecond) lockSecond.wait(1L);
    }

    return -1;
  }
}

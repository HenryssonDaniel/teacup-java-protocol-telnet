package io.github.henryssondaniel.teacup.protocol.telnet.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ResponseImplTest {
  private static final String DATA = "test";
  private static final byte[] BYTES = DATA.getBytes(StandardCharsets.UTF_8);

  private final ResponseAdder responseAdder = new ResponseImpl(DATA, BYTES);

  @Test
  void addAndGetData() {
    responseAdder.addData(DATA, BYTES);

    var bytes = new byte[BYTES.length + BYTES.length];
    System.arraycopy(BYTES, 0, bytes, 0, BYTES.length);
    System.arraycopy(BYTES, 0, bytes, BYTES.length, BYTES.length);

    assertThat(responseAdder.getData()).isEqualTo(bytes);
    assertThat(responseAdder.getDataAsString()).isEqualTo(DATA + DATA);
  }

  @Test
  void getData() {
    assertThat(responseAdder.getDataAsString()).isEqualTo(DATA);
  }

  @Test
  void getDataAsString() {
    assertThat(responseAdder.getDataAsString()).isEqualTo(DATA);
  }
}

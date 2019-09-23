package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class RequestImplTest {
  private final Request request = new RequestImpl((byte) 1);

  @Test
  void getCommand() {
    assertThat(request.getCommand()).isSameAs((byte) 1);
  }

  @Test
  void getCommandString() {
    assertThat(request.getCommandString())
        .isEqualTo(new String(new byte[] {(byte) 1}, StandardCharsets.UTF_8));
  }
}

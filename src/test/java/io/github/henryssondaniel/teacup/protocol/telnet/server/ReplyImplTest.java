package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ReplyImplTest {
  @Test
  void getData() {
    var data = "test".getBytes(StandardCharsets.UTF_8);
    assertThat(new ReplyImpl(data).getData()).isEqualTo(data);
  }
}

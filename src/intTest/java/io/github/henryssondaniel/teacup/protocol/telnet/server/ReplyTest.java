package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.Charset;
import org.junit.jupiter.api.Test;

class ReplyTest {
  @Test
  void getBytes() {
    var reply = "reply";
    assertThat(Factory.createReply(reply).getData())
        .isEqualTo(reply.getBytes(Charset.defaultCharset()));
  }
}

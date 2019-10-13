package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ContextTest {
  @Test
  void getReply() {
    var reply = Factory.createReply("reply");
    var context = Factory.createContext(reply);

    assertThat(context.getReply()).isEqualTo(reply);
  }
}

package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

class ContextImplTest {
  @Test
  void getReply() {
    var reply = mock(Reply.class);
    assertThat(new ContextImpl(reply).getReply()).isSameAs(reply);
  }
}

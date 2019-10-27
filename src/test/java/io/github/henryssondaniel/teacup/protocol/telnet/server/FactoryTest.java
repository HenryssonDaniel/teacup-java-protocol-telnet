package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class FactoryTest {
  private static final String DATA = "data";
  private final Listener listener = mock(Listener.class);

  @Test
  void createContext() {
    var reply = mock(Reply.class);
    assertThat(Factory.createContext(reply)).isExactlyInstanceOf(ContextImpl.class);
    verifyNoInteractions(reply);
  }

  @Test
  void createListener() {
    assertThat(Factory.createListener(1, 1, 1, 1)).isExactlyInstanceOf(ListenerImpl.class);
  }

  @Test
  void createListenerWithBacklog() {
    assertThat(Factory.createListener(1, 1, 1, 1, 1)).isExactlyInstanceOf(ListenerImpl.class);
  }

  @Test
  void createReply() {
    assertThat(Factory.createReply(DATA)).isExactlyInstanceOf(ReplyImpl.class);
  }

  @Test
  void createReplyWithString() {
    assertThat(Factory.createReply(DATA.getBytes(StandardCharsets.UTF_8)))
        .isExactlyInstanceOf(ReplyImpl.class);
  }

  @Test
  void createSimpleServer() {
    assertThat(Factory.createSimpleServer(listener)).isExactlyInstanceOf(Simple.class);
    verifyListener(listener);
  }

  @Test
  void createSimpleServerWhenBootException() {
    assertThat(Factory.createSimpleServer(listener, "vt100")).isExactlyInstanceOf(Simple.class);
    verifyListener(listener);
  }

  private static void verifyListener(Listener listener) {
    verify(listener).getBacklog();
    verify(listener).getHousekeepingInterval();
    verify(listener).getMaxConnections();
    verify(listener).getPort();
    verify(listener).getTimeout();
    verifyNoMoreInteractions(listener);
  }
}

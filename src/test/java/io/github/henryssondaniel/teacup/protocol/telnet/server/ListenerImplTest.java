package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ListenerImplTest {
  private final Listener listener = new ListenerImpl(1, 2, 3, 4, 5);

  @Test
  void getBacklog() {
    assertThat(listener.getBacklog()).isOne();
  }

  @Test
  void getHousekeepingInterval() {
    assertThat(listener.getHousekeepingInterval()).isEqualTo(2);
  }

  @Test
  void getMaxConnections() {
    assertThat(listener.getMaxConnections()).isEqualTo(3);
  }

  @Test
  void getPort() {
    assertThat(listener.getPort()).isEqualTo(4);
  }

  @Test
  void getTimeout() {
    assertThat(listener.getTimeout()).isEqualTo(5);
  }
}

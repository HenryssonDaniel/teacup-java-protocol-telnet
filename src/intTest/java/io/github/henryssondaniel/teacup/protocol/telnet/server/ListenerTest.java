package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ListenerTest {
  private final Listener listener = Factory.createListener(1, 2, 3, 4);

  @Test
  void getBacklog() {
    assertThat(listener.getBacklog()).isEqualTo(0);
  }

  @Test
  void getHousekeepingInterval() {
    assertThat(listener.getHousekeepingInterval()).isEqualTo(1);
  }

  @Test
  void getMaxConnections() {
    assertThat(listener.getMaxConnections()).isEqualTo(2);
  }

  @Test
  void getPort() {
    assertThat(listener.getPort()).isEqualTo(3);
  }

  @Test
  void getTimeout() {
    assertThat(listener.getTimeout()).isEqualTo(4);
  }
}

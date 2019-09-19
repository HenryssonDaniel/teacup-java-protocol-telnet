package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import org.junit.jupiter.api.Test;

class FactoryTest {
  @Test
  void createListener() {
    assertThat(Factory.createListener(1, 1, 1, 1)).isExactlyInstanceOf(ListenerImpl.class);
  }

  @Test
  void createListenerWithBacklog() {
    assertThat(Factory.createListener(1, 1, 1, 1, 1)).isExactlyInstanceOf(ListenerImpl.class);
  }

  @Test
  void createSimpleServer() {
    var listener = mock(Listener.class);

    Collection<Listener> listeners = new ArrayList<>(2);
    listeners.add(listener);
    listeners.add(listener);

    assertThat(Factory.createSimpleServer(listeners)).isExactlyInstanceOf(Simple.class);

    verify(listener, times(2)).getBacklog();
    verify(listener, times(2)).getHousekeepingInterval();
    verify(listener, times(2)).getMaxConnections();
    verify(listener, times(2)).getPort();
    verify(listener, times(2)).getTimeout();
    verifyNoMoreInteractions(listener);
  }

  @Test
  void createSimpleServerWhenBootException() {
    var properties = mock(Properties.class);
    assertThat(Factory.createSimpleServer(properties)).isExactlyInstanceOf(Simple.class);

    verify(properties).getProperty("terminals");
    verify(properties).getProperty("terminals.windoof");
    verifyNoMoreInteractions(properties);
  }
}

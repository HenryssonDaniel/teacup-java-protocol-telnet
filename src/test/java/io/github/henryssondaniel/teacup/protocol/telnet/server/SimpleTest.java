package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import io.github.henryssondaniel.teacup.core.Server;
import net.wimpi.telnetd.TelnetD;
import org.junit.jupiter.api.Test;

class SimpleTest {
  private final TelnetD telnetD = mock(TelnetD.class);
  private final Server server = new Simple(telnetD);

  @Test
  void setUp() {
    server.setUp();

    verify(telnetD).start();
    verifyNoMoreInteractions(telnetD);
  }

  @Test
  void tearDown() {
    server.tearDown();

    verify(telnetD).stop();
    verifyNoMoreInteractions(telnetD);
  }
}

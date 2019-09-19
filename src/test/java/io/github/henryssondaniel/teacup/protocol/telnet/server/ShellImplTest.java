package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShellImplTest {
  private final BasicTerminalIO basicTerminalIO = mock(BasicTerminalIO.class);
  private final Connection connection = mock(Connection.class);
  private final ConnectionEvent connectionEvent = mock(ConnectionEvent.class);
  private final Shell shell = new ShellImpl();

  @BeforeEach
  void beforeEach() {
    when(connection.getTerminalIO()).thenReturn(basicTerminalIO);
  }

  @Test
  void connectionIdle() {
    shell.connectionIdle(connectionEvent);
    verifyZeroInteractions(connectionEvent);
  }

  @Test
  void connectionLogoutRequest() {
    shell.connectionLogoutRequest(connectionEvent);
    verifyZeroInteractions(connectionEvent);
  }

  @Test
  void connectionSentBreak() {
    shell.connectionSentBreak(connectionEvent);
    verifyZeroInteractions(connectionEvent);
  }

  @Test
  void connectionTimedOut() {
    when(connectionEvent.getSource()).thenReturn(connection);

    shell.connectionTimedOut(connectionEvent);

    verify(connection).close();
    verifyNoMoreInteractions(connection);

    verify(connectionEvent).getSource();
    verifyNoMoreInteractions(connectionEvent);
  }

  @Test
  void run() throws IOException {
    when(basicTerminalIO.read()).thenReturn(1, -1);

    shell.run(connection);

    verify(basicTerminalIO).flush();
    verify(basicTerminalIO, times(2)).read();
    verify(basicTerminalIO).write("1");
    verifyNoMoreInteractions(basicTerminalIO);

    verify(connection).getTerminalIO();
    verifyNoMoreInteractions(connection);
  }

  @Test
  void runWhenException() throws IOException {
    when(basicTerminalIO.read()).thenThrow(new IOException("test"));

    shell.run(connection);

    verify(basicTerminalIO).read();
    verifyNoMoreInteractions(basicTerminalIO);

    verify(connection).getTerminalIO();
    verifyNoMoreInteractions(connection);
  }
}

package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.github.henryssondaniel.teacup.protocol.server.TimeoutSupplier;
import java.io.IOException;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class HandlerImplTest {
  private final BasicTerminalIO basicTerminalIO = mock(BasicTerminalIO.class);
  private final Connection connection = mock(Connection.class);
  private final ConnectionEvent connectionEvent = mock(ConnectionEvent.class);
  private final Handler handler = new HandlerImpl();

  @Mock private TimeoutSupplier<Request> timeoutSupplier;

  @Test
  void addAndGetTimeoutSuppliers() {
    handler.addTimeoutSupplier(timeoutSupplier);
    assertThat(handler.getTimeoutSuppliers()).containsExactly(timeoutSupplier);
  }

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.initMocks(this);
    when(connection.getTerminalIO()).thenReturn(basicTerminalIO);
  }

  @Test
  void connectionIdle() {
    handler.connectionIdle(connectionEvent);
    verifyNoInteractions(connectionEvent);
  }

  @Test
  void connectionLogoutRequest() {
    handler.connectionLogoutRequest(connectionEvent);
    verifyNoInteractions(connectionEvent);
  }

  @Test
  void connectionSentBreak() {
    handler.connectionSentBreak(connectionEvent);
    verifyNoInteractions(connectionEvent);
  }

  @Test
  void connectionTimedOut() {
    when(connectionEvent.getSource()).thenReturn(connection);

    handler.connectionTimedOut(connectionEvent);

    verify(connection).close();
    verifyNoMoreInteractions(connection);

    verify(connectionEvent).getSource();
    verifyNoMoreInteractions(connectionEvent);
  }

  @Test
  void getReply() {
    assertThat(handler.getReply()).isNull();
  }

  @Test
  void getTimeoutSuppliers() {
    assertThat(handler.getTimeoutSuppliers()).isEmpty();
  }

  @Test
  void removeAndGetTimeoutSupplier() {
    handler.removeTimeoutSupplier(timeoutSupplier);
    assertThat(handler.getTimeoutSuppliers()).isEmpty();
  }

  @Test
  void run() throws IOException {
    when(basicTerminalIO.read()).thenReturn(1, -1);

    var reply = mock(Reply.class);
    when(reply.getData()).thenReturn(new byte[] {(byte) 1});

    handler.setReply(reply);
    handler.run(connection);

    verify(basicTerminalIO).flush();
    verify(basicTerminalIO, times(2)).read();
    verify(basicTerminalIO).write((byte) 1);
    verifyNoMoreInteractions(basicTerminalIO);

    verify(connection).getTerminalIO();
    verifyNoMoreInteractions(connection);

    verify(reply).getData();
    verifyNoMoreInteractions(reply);
  }

  @Test
  void runWhenException() throws IOException {
    when(basicTerminalIO.read()).thenThrow(new IOException("test"));

    handler.run(connection);

    verify(basicTerminalIO).read();
    verifyNoMoreInteractions(basicTerminalIO);

    verify(connection).getTerminalIO();
    verifyNoMoreInteractions(connection);
  }

  @Test
  void setAndGetReply() {
    var reply = mock(Reply.class);
    handler.setReply(reply);

    assertThat(handler.getReply()).isSameAs(reply);
  }
}

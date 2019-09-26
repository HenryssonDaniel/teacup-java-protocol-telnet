package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import net.wimpi.telnetd.BootException;
import net.wimpi.telnetd.io.terminal.TerminalManager;
import net.wimpi.telnetd.io.terminal.vt100;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionData;
import net.wimpi.telnetd.shell.Shell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectionImplTest {
  private final ConnectionData connectionData = mock(ConnectionData.class);
  private final Object lock = new Object();
  private final Shell shell = mock(Shell.class);

  private Connection connection;
  private boolean waiting = true;

  @BeforeEach
  void beforeEach() throws BootException, IOException {
    var socket = mock(Socket.class);
    TerminalManager.createTerminalManager(Collections.singletonMap("default", new vt100()), false);

    when(connectionData.getNegotiatedTerminalType()).thenReturn("default");

    try (var dataSocket = connectionData.getSocket()) {
      when(dataSocket).thenReturn(socket);
    }

    try (var outputStream = OutputStream.nullOutputStream()) {
      createConnection(socket, outputStream);
    }
  }

  @Test
  void run() throws InterruptedException {
    doAnswer(invocation -> createAnswer()).when(shell).run(connection);

    var thread = new Thread(() -> connection.start());
    thread.start();

    synchronized (lock) {
      while (waiting) lock.wait(1L);

      thread.interrupt();
    }

    verify(shell).run(connection);
    verifyNoMoreInteractions(shell);
  }

  private Object createAnswer() {
    synchronized (lock) {
      waiting = false;
      lock.notifyAll();
    }

    return "";
  }

  private void createConnection(Socket socket, OutputStream outputStream) throws IOException {
    try (var stream = socket.getOutputStream()) {
      when(stream).thenReturn(outputStream);

      connection = new ConnectionImpl(connectionData, "name", shell);
    }
  }
}

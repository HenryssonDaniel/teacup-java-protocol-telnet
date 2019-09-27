package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.wimpi.telnetd.BootException;
import net.wimpi.telnetd.io.terminal.TerminalManager;
import net.wimpi.telnetd.io.terminal.vt100;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionData;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.net.ConnectionManager;
import net.wimpi.telnetd.shell.Shell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectionManagerImplTest {
  private final Connection connection = mock(Connection.class);
  private final ConnectionData connectionData = mock(ConnectionData.class);
  private final Listener listener = mock(Listener.class);
  private final Object lock = new Object();
  private final Object secondLock = new Object();
  private final Shell shell = mock(Shell.class);
  private final Socket socket = mock(Socket.class);

  private boolean secondWaiting = true;
  private boolean waiting = true;

  @BeforeEach
  void beforeEach() throws BootException, UnknownHostException {
    when(listener.getMaxConnections()).thenReturn(1);
    when(listener.getHousekeepingInterval()).thenReturn(1);
    when(listener.getTimeout()).thenReturn(1000);

    when(socket.getInetAddress()).thenReturn(InetAddress.getLocalHost());
    TerminalManager.createTerminalManager(Collections.singletonMap("default", new vt100()), false);
  }

  @Test
  void getConnection() {
    assertThat(
            new ConnectionManagerImpl(
                    Collections.emptyList(), listener, Collections.singletonList(connection), null)
                .getConnection(0))
        .isSameAs(connection);

    verifyListener();
    verifyZeroInteractions(connection);
  }

  @Test
  void getConnectionsByAddress() {
    var inetAddress = mock(InetAddress.class);

    when(connection.getConnectionData()).thenReturn(connectionData);
    when(connectionData.getInetAddress()).thenReturn(inetAddress);

    assertThat(
            new ConnectionManagerImpl(
                    Collections.emptyList(), listener, Collections.singletonList(connection), null)
                .getConnectionsByAdddress(inetAddress))
        .containsExactly(connection);

    verify(connection).getConnectionData();
    verifyNoMoreInteractions(connection);

    verify(connectionData).getInetAddress();
    verifyNoMoreInteractions(connectionData);

    verifyListener();
  }

  @Test
  void makeConnection() throws IOException, InterruptedException {
    doAnswer(
            invocation -> {
              stopWait();
              return "";
            })
        .when(socket)
        .close();

    List<Connection> openConnections = new ArrayList<>(0);

    ConnectionManager connectionManager =
        new ConnectionManagerImpl(Collections.emptyList(), listener, openConnections, shell);

    try (var outputStream = OutputStream.nullOutputStream()) {
      makeConnection(connectionManager, outputStream);
    }

    assertThat(openConnections).hasSize(1);

    verifyListener();

    synchronized (lock) {
      while (waiting) lock.wait(1L);

      verify(shell).run(any(ConnectionImpl.class));
      verifyNoMoreInteractions(shell);

      verify(socket).close();
      verify(socket).getInetAddress();
      verify(socket).getInputStream();
      verify(socket).getLocalAddress();
      verify(socket).getOutputStream();
      verify(socket).getPort();
      verify(socket).setSoTimeout(0);
      verify(socket).setSoTimeout(1000);
      verifyNoMoreInteractions(socket);
    }
  }

  @Test
  void makeConnectionWhenFull() {
    when(listener.getMaxConnections()).thenReturn(0);

    List<Connection> openConnections = new ArrayList<>(0);

    ConnectionManager connectionManager =
        new ConnectionManagerImpl(Collections.emptyList(), listener, openConnections, null);
    connectionManager.makeConnection(socket);

    assertThat(openConnections).isEmpty();

    verifyListener();

    verify(socket).getInetAddress();
    verify(socket).getPort();
    verifyNoMoreInteractions(socket);
  }

  @Test
  void openConnectionCount() {
    assertThat(
            new ConnectionManagerImpl(
                    Collections.emptyList(), listener, Collections.emptyList(), null)
                .openConnectionCount())
        .isZero();

    verifyListener();
  }

  @Test
  void runWhenInterrupted() throws InterruptedException {
    doAnswer(invocation -> isActive()).when(connection).isActive();

    ConnectionManager connectionManager =
        new ConnectionManagerImpl(
            Collections.emptyList(), listener, Collections.singletonList(connection), null);

    var thread = new Thread(connectionManager);
    thread.start();

    interruptThread(thread);

    verify(connection).isActive();
    verifyNoMoreInteractions(connection);

    verifyListener();

    notifyLock();
  }

  @Test
  void runWhenStopped() {
    List<Connection> connections = new ArrayList<>(1);
    connections.add(connection);

    ConnectionManager connectionManager =
        new ConnectionManagerImpl(Collections.emptyList(), listener, connections, null);
    connectionManager.stop();
    connectionManager.registerClosedConnection(connection);
    connectionManager.run();

    verifyListener();
  }

  @Test
  void start() throws InterruptedException {
    List<Connection> openConnections = new ArrayList<>(1);
    openConnections.add(connection);
    openConnections.add(connection);

    when(connection.isActive())
        .thenReturn(false, false, true)
        .thenAnswer(
            invocation -> {
              stopWait();
              return true;
            })
        .thenAnswer(
            invocation -> {
              synchronized (secondLock) {
                while (secondWaiting) secondLock.wait(1L);
              }

              return false;
            });
    when(connection.getConnectionData()).thenReturn(connectionData);

    when(listener.getTimeout()).thenReturn(Integer.MAX_VALUE);
    when(connectionData.getLastActivity()).thenReturn(Long.MAX_VALUE, 0L);

    ConnectionManager connectionManager =
        new ConnectionManagerImpl(Collections.emptyList(), listener, openConnections, null);

    var thread = new Thread(connectionManager::start);
    thread.start();

    synchronized (lock) {
      while (waiting) lock.wait(1L);

      connectionManager.stop();
    }

    verify(connection).close();
    verify(connection, times(2)).getConnectionData();
    verify(connection, times(4)).isActive();
    verify(connection).processConnectionEvent(any(ConnectionEvent.class));
    verifyNoMoreInteractions(connection);

    verify(connectionData, times(2)).getLastActivity();
    verifyNoMoreInteractions(connectionData);

    verifyListener();

    notifyLock();
  }

  @Test
  void stopWhenInterrupted() throws InterruptedException {
    doAnswer(invocation -> isActive()).when(connection).isActive();

    List<Connection> connections = new ArrayList<>(1);
    connections.add(connection);

    ConnectionManager connectionManager =
        new ConnectionManagerImpl(Collections.emptyList(), listener, connections, null);
    connectionManager.start();

    synchronized (lock) {
      while (waiting) lock.wait(1L);
    }

    var thread = new Thread(connectionManager::stop);
    thread.start();
    thread.interrupt();

    verify(connection).isActive();
    verifyNoMoreInteractions(connection);

    verifyListener();

    notifyLock();
  }

  private void interruptThread(Thread thread) throws InterruptedException {
    synchronized (lock) {
      while (waiting) lock.wait(1L);

      thread.interrupt();
    }
  }

  private Object isActive() throws InterruptedException {
    stopWait();

    synchronized (secondLock) {
      while (secondWaiting) secondLock.wait(1L);
    }

    return false;
  }

  private void makeConnection(ConnectionManager connectionManager, OutputStream outputStream)
      throws IOException {
    try (var stream = socket.getOutputStream()) {
      when(stream).thenReturn(outputStream);

      connectionManager.makeConnection(socket);
    }
  }

  private void notifyLock() {
    synchronized (secondLock) {
      secondWaiting = false;
      secondLock.notifyAll();
    }
  }

  private void stopWait() {
    synchronized (lock) {
      waiting = false;
      lock.notifyAll();
    }
  }

  private void verifyListener() {
    verify(listener).getHousekeepingInterval();
    verify(listener).getMaxConnections();
    verify(listener).getTimeout();
    verifyNoMoreInteractions(listener);
  }
}

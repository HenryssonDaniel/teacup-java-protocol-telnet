package io.github.henryssondaniel.teacup.protocol.telnet.server;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionData;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.net.ConnectionManager;
import net.wimpi.telnetd.shell.Shell;

final class ConnectionManagerImpl extends ConnectionManager {
  private static final int DISCONNECT = 101;
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(ConnectionManagerImpl.class);

  private final Stack<? super Connection> closedConnections;
  private final List<Connection> openConnections;
  private final Shell shell;
  private final ThreadGroup threadGroup =
      new ThreadGroup(String.format("%sConnections", toString()));

  private boolean stopped;
  private Thread thread;

  ConnectionManagerImpl(
      Collection<Connection> closedConnections,
      Listener listener,
      List<Connection> openConnections,
      Shell shell) {
    super(
        listener.getMaxConnections(),
        0,
        listener.getTimeout(),
        listener.getHousekeepingInterval(),
        null,
        "handler",
        false);

    this.closedConnections = new Stack<>();
    this.closedConnections.addAll(closedConnections);

    this.openConnections = Collections.synchronizedList(openConnections);
    this.shell = shell;
  }

  @Override
  public Connection getConnection(int index) {
    LOGGER.log(Level.FINE, "Get connection");

    Connection connection;

    synchronized (openConnections) {
      connection = openConnections.get(index);
    }

    return connection;
  }

  @Override
  public Connection[] getConnectionsByAdddress(InetAddress inetAddress) {
    LOGGER.log(Level.FINE, "Get connections by address");

    Connection[] connections;

    synchronized (openConnections) {
      connections =
          openConnections.stream()
              .filter(
                  connection -> connection.getConnectionData().getInetAddress().equals(inetAddress))
              .toArray(Connection[]::new);
    }

    return connections;
  }

  @Override
  public void makeConnection(Socket socket) {
    LOGGER.log(Level.FINE, "Make connection");

    var connectionData = new ConnectionData(socket, this);
    connectionData.setLineMode(isLineMode());
    connectionData.setLoginShell(getLoginShell());

    if (openConnections.size() < getMaxConnections()) {
      Connection connection = new ConnectionImpl(connectionData, shell, threadGroup);

      synchronized (openConnections) {
        openConnections.add(connection);
      }

      connection.start();
    }
  }

  @Override
  public int openConnectionCount() {
    LOGGER.log(Level.FINE, "Open connection count");
    return openConnections.size();
  }

  @Override
  public void registerClosedConnection(Connection connection) {
    LOGGER.log(Level.FINE, "Register closed connection");
    if (!stopped && !closedConnections.contains(connection)) closedConnections.push(connection);
  }

  @Override
  public void run() {
    LOGGER.log(Level.FINE, "Run");

    var running = true;

    while (running)
      try {
        removeClosedConnections();
        processConnectionEvent();
        Thread.sleep(getHousekeepingInterval());
        if (stopped) running = false;
      } catch (InterruptedException interruptedException) {
        LOGGER.log(Level.SEVERE, "The connection manager got interrupted", interruptedException);
        running = false;
        Thread.currentThread().interrupt();
      }
  }

  @Override
  public void start() {
    LOGGER.log(Level.FINE, "Start");

    thread = new Thread(this);
    thread.start();
  }

  @Override
  public void stop() {
    LOGGER.log(Level.FINE, "Stop");

    stopped = true;

    try {
      if (thread != null) thread.join();
    } catch (InterruptedException interruptedException) {
      LOGGER.log(Level.SEVERE, "Could not stop the connection manager", interruptedException);
      Thread.currentThread().interrupt();
    }

    synchronized (openConnections) {
      var iterator = openConnections.iterator();

      var hasMore = true;

      while (hasMore)
        if (iterator.hasNext()) iterator.next().close();
        else {
          openConnections.clear();
          hasMore = false;
        }
    }
  }

  private void processConnectionEvent() {
    if (!stopped)
      synchronized (openConnections) {
        for (var connection : openConnections) processConnectionEvent(connection);
      }
  }

  private void processConnectionEvent(Connection connection) {
    if (connection.isActive()) {
      var connectionData = connection.getConnectionData();

      var inactivity = System.currentTimeMillis() - connectionData.getLastActivity();

      if (inactivity > getDisconnectTimeout())
        connection.processConnectionEvent(new ConnectionEvent(connection, DISCONNECT));
    } else registerClosedConnection(connection);
  }

  private void removeClosedConnections() {
    if (!stopped)
      while (!closedConnections.isEmpty()) {
        var nextOne = closedConnections.pop();

        synchronized (openConnections) {
          openConnections.remove(nextOne);
        }
      }
  }
}

package io.github.henryssondaniel.teacup.protocol.telnet.server;

class ListenerImpl implements Listener {
  private final int backlog;
  private final int housekeepingInterval;
  private final int maxConnections;
  private final int port;
  private final int timeout;

  ListenerImpl(int backlog, int housekeepingInterval, int maxConnections, int port, int timeout) {
    this.backlog = backlog;
    this.housekeepingInterval = housekeepingInterval;
    this.maxConnections = maxConnections;
    this.port = port;
    this.timeout = timeout;
  }

  @Override
  public int getBacklog() {
    return backlog;
  }

  @Override
  public int getHousekeepingInterval() {
    return housekeepingInterval;
  }

  @Override
  public int getMaxConnections() {
    return maxConnections;
  }

  @Override
  public int getPort() {
    return port;
  }

  @Override
  public int getTimeout() {
    return timeout;
  }
}

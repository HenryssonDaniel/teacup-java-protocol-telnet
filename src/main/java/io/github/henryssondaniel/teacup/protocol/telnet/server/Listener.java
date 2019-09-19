package io.github.henryssondaniel.teacup.protocol.telnet.server;

/**
 * Listener.
 *
 * @since 1.0
 */
public interface Listener {
  /**
   * Returns the backlog.
   *
   * @return the backlog
   * @since 1.0
   */
  int getBacklog();

  /**
   * Returns the housekeeping interval.
   *
   * @return the housekeeping interval
   * @since 1.0
   */
  int getHousekeepingInterval();

  /**
   * Returns the max connections.
   *
   * @return the max connections
   * @since 1.0
   */
  int getMaxConnections();

  /**
   * Returns the port.
   *
   * @return the port
   * @since 1.0
   */
  int getPort();

  /**
   * Returns the timeout.
   *
   * @return the timeout
   * @since 1.0
   */
  int getTimeout();
}

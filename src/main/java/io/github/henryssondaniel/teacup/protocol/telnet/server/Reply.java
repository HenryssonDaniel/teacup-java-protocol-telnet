package io.github.henryssondaniel.teacup.protocol.telnet.server;

/**
 * An outgoing reply from the server.
 *
 * @since 1.0
 */
@FunctionalInterface
public interface Reply {
  /**
   * Returns the data.
   *
   * @return the data
   * @since 1.0
   */
  byte[] getData();
}

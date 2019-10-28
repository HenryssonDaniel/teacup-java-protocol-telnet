package io.github.henryssondaniel.teacup.protocol.telnet.server;

/**
 * An incoming request to the server.
 *
 * @since 1.0
 */
public interface Request {
  /**
   * Returns the command.
   *
   * @return the command
   * @since 1.0
   */
  byte getCommand();

  /**
   * Returns the command as a String.
   *
   * @return the command
   * @since 1.0
   */
  String getCommandAsString();
}

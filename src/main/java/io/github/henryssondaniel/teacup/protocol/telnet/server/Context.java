package io.github.henryssondaniel.teacup.protocol.telnet.server;

/**
 * Context to be used by a server.
 *
 * @since 1.0
 */
@FunctionalInterface
public interface Context extends io.github.henryssondaniel.teacup.protocol.server.Context {
  /**
   * Returns the reply.
   *
   * @return the reply
   * @since 1.0
   */
  Reply getReply();
}

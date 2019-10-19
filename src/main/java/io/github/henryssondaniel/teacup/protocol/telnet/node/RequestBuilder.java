package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;
import io.github.henryssondaniel.teacup.protocol.NodeBuilder;

/**
 * Request builder.
 *
 * @since 1.0
 */
public interface RequestBuilder
    extends NodeBuilder<
        io.github.henryssondaniel.teacup.protocol.telnet.server.Request, Request, RequestBuilder> {
  /**
   * Sets the command.
   *
   * @param command the command
   * @return the request builder
   * @since 1.0
   */
  RequestBuilder setCommand(GenericObjectAssert<? super Byte, ?> command);

  /**
   * Sets the command as string.
   *
   * @param commandAsString the command as string
   * @return the request builder
   * @since 1.0
   */
  RequestBuilder setCommandAsString(GenericStringAssert<?> commandAsString);
}

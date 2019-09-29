package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.Node;

/**
 * Response.
 *
 * @since 1.0
 */
@FunctionalInterface
public interface Reply
    extends Node<io.github.henryssondaniel.teacup.protocol.telnet.server.Reply> {}

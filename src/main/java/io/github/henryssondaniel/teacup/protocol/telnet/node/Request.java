package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.Node;

/**
 * Request.
 *
 * @since 1.0
 */
@FunctionalInterface
public interface Request
    extends Node<io.github.henryssondaniel.teacup.protocol.telnet.server.Request> {}

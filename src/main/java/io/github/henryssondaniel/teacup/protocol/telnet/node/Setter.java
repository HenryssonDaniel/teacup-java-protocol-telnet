package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.Node;
import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;

interface Setter<T, U extends GenericObjectAssert<T, ?>> extends Node<T> {
  U getAssertion();

  void setAssertion(U assertion);
}

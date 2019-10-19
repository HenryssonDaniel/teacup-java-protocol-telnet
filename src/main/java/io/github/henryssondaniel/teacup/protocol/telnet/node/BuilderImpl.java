package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.protocol.DefaultNodeBuilder;
import io.github.henryssondaniel.teacup.protocol.Node;
import io.github.henryssondaniel.teacup.protocol.NodeBuilder;

abstract class BuilderImpl<
        T,
        U extends Node<T>,
        V extends Setter<T, GenericObjectAssert<T, ?>>,
        X extends NodeBuilder<T, U, X>>
    extends DefaultNodeBuilder<T, U, V, X> {
  BuilderImpl(V setter) {
    super(setter);
  }

  @Override
  protected void doAssertion(GenericObjectAssert<T, ?> objectAssert) {
    getImplementation().setAssertion(objectAssert);
  }
}

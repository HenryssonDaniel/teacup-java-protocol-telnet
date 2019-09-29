package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;

abstract class SetterImpl<T, U extends GenericObjectAssert<T, ?>> implements Setter<T, U> {
  private U assertion;

  @Override
  public U getAssertion() {
    return assertion;
  }

  @Override
  public void setAssertion(U assertion) {
    this.assertion = assertion;
  }
}

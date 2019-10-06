package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;
import io.github.henryssondaniel.teacup.core.logging.Factory;
import io.github.henryssondaniel.teacup.protocol.telnet.server.Request;
import java.util.logging.Level;
import java.util.logging.Logger;

class RequestImpl extends SetterImpl<Request, GenericObjectAssert<Request, ?>>
    implements RequestSetter {
  private static final Logger LOGGER = Factory.getLogger(RequestImpl.class);

  private GenericObjectAssert<? super Byte, ?> command;
  private GenericObjectAssert<String, ?> commandAsString;

  @Override
  public void setCommand(GenericObjectAssert<? super Byte, ?> command) {
    LOGGER.log(Level.FINE, "Set command");
    this.command = command;
  }

  @Override
  public void setCommandAsString(GenericStringAssert<?> commandAsString) {
    LOGGER.log(Level.FINE, "Set command as string");
    this.commandAsString = commandAsString;
  }

  @Override
  public void verify(Request request) {
    LOGGER.log(Level.FINE, "Verify");

    var genericObjectAssert = getAssertion();
    if (genericObjectAssert != null) genericObjectAssert.verify(request);

    if (command != null) command.verify(request.getCommand());
    if (commandAsString != null) commandAsString.verify(request.getCommandAsString());
  }
}

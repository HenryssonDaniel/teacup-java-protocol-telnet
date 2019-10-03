package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;
import io.github.henryssondaniel.teacup.core.logging.Factory;
import java.util.logging.Level;
import java.util.logging.Logger;

class RequestBuilderImpl
    extends BuilderImpl<
        io.github.henryssondaniel.teacup.protocol.telnet.server.Request,
        Request,
        RequestSetter,
        RequestBuilder>
    implements RequestBuilder {
  private static final Logger LOGGER = Factory.getLogger(RequestBuilderImpl.class);

  RequestBuilderImpl() {
    super(new RequestImpl());
  }

  @Override
  public RequestBuilder setCommand(GenericObjectAssert<? super Byte, ?> command) {
    LOGGER.log(Level.FINE, "Set command");
    getImplementation().setCommand(command);
    return this;
  }

  @Override
  public RequestBuilder setCommandAsString(GenericStringAssert<?> commandAsString) {
    LOGGER.log(Level.FINE, "Set command as string");
    getImplementation().setCommandAsString(commandAsString);
    return this;
  }

  @Override
  protected RequestSetter createImplementation() {
    return new RequestImpl();
  }
}

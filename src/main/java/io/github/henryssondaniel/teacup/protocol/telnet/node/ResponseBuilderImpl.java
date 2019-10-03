package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.assertion.GenericByteArrayAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;
import io.github.henryssondaniel.teacup.core.logging.Factory;
import java.util.logging.Level;
import java.util.logging.Logger;

class ResponseBuilderImpl
    extends BuilderImpl<
        io.github.henryssondaniel.teacup.protocol.telnet.client.Response,
        Response,
        ResponseSetter,
        ResponseBuilder>
    implements ResponseBuilder {
  private static final Logger LOGGER = Factory.getLogger(ResponseBuilderImpl.class);

  ResponseBuilderImpl() {
    super(new ResponseImpl());
  }

  @Override
  public ResponseBuilder setData(GenericByteArrayAssert<?> data) {
    LOGGER.log(Level.FINE, "Set code");
    getImplementation().setData(data);
    return this;
  }

  @Override
  public ResponseBuilder setDataAsString(GenericStringAssert<?> dataAsString) {
    LOGGER.log(Level.FINE, "Set code");
    getImplementation().setDataAsString(dataAsString);
    return this;
  }

  @Override
  protected ResponseSetter createImplementation() {
    return new ResponseImpl();
  }
}

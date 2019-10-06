package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.assertion.GenericByteArrayAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;
import io.github.henryssondaniel.teacup.core.logging.Factory;
import io.github.henryssondaniel.teacup.protocol.telnet.client.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

class ResponseImpl extends SetterImpl<Response, GenericObjectAssert<Response, ?>>
    implements ResponseSetter {
  private static final Logger LOGGER = Factory.getLogger(ResponseImpl.class);

  private GenericObjectAssert<byte[], ?> data;
  private GenericObjectAssert<String, ?> dataAsString;

  @Override
  public void setData(GenericByteArrayAssert<?> data) {
    LOGGER.log(Level.FINE, "Set data");
    this.data = data;
  }

  @Override
  public void setDataAsString(GenericStringAssert<?> dataAsString) {
    LOGGER.log(Level.FINE, "Set data as string");
    this.dataAsString = dataAsString;
  }

  @Override
  public void verify(Response response) {
    LOGGER.log(Level.FINE, "Verify");

    var genericObjectAssert = getAssertion();
    if (genericObjectAssert != null) genericObjectAssert.verify(response);

    if (data != null) data.verify(response.getData());
    if (dataAsString != null) dataAsString.verify(response.getDataAsString());
  }
}

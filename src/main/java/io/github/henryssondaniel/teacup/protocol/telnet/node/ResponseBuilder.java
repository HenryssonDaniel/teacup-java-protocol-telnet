package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.NodeBuilder;
import io.github.henryssondaniel.teacup.core.assertion.GenericByteArrayAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;

/**
 * Response builder.
 *
 * @since 1.0
 */
public interface ResponseBuilder
    extends NodeBuilder<
        io.github.henryssondaniel.teacup.protocol.telnet.client.Response,
        Response,
        ResponseBuilder> {
  /**
   * Sets the data.
   *
   * @param data the data
   * @return the response builder
   * @since 1.0
   */
  ResponseBuilder setData(GenericByteArrayAssert<?> data);

  /**
   * Sets the data as string.
   *
   * @param dataAsString the data as string
   * @return the response builder
   * @since 1.0
   */
  ResponseBuilder setDataAsString(GenericStringAssert<?> dataAsString);
}

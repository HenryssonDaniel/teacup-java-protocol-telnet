package io.github.henryssondaniel.teacup.protocol.telnet.node;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory.
 *
 * @since 1.0
 */
public enum Factory {
  ;

  private static final String CREATE_BUILDER = "Creating a new {0} builder";
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(Factory.class);

  /**
   * Creates a new request builder.
   *
   * @return the request builder
   * @since 1.0
   */
  public static RequestBuilder createRequestBuilder() {
    LOGGER.log(Level.FINE, CREATE_BUILDER, "request");
    return new RequestBuilderImpl();
  }

  /**
   * Creates a new response builder.
   *
   * @return the response builder
   * @since 1.0
   */
  public static ResponseBuilder createResponseBuilder() {
    LOGGER.log(Level.FINE, CREATE_BUILDER, "response");
    return new ResponseBuilderImpl();
  }
}

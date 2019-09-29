package io.github.henryssondaniel.teacup.protocol.telnet.client;

/**
 * Response.
 *
 * @since 1.0
 */
public interface Response {
  /**
   * Returns the data.
   *
   * @return the data
   * @since 1.0
   */
  byte[] getData();

  /**
   * Returns the data as string.
   *
   * @return the data as string
   * @since 1.0
   */
  String getDataAsString();
}

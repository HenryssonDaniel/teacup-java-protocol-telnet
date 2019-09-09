package io.github.henryssondaniel.teacup.protocol.telnet.client;

import io.github.henryssondaniel.teacup.protocol.telnet.Client;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.telnet.TelnetClient;

/**
 * Factory class for the client package.
 *
 * @since 1.0
 */
public enum Factory {
  ;

  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(Factory.class);

  /**
   * Creates a new {@link Client}.
   *
   * @param terminalType the terminal type
   * @return the client
   * @since 1.0
   */
  public static Client createClient(String terminalType) {
    LOGGER.log(Level.FINE, "Create client");
    return new Simple(new TelnetClient(terminalType));
  }
}

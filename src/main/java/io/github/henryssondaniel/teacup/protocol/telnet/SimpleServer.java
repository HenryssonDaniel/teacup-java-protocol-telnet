package io.github.henryssondaniel.teacup.protocol.telnet;

import io.github.henryssondaniel.teacup.core.Server;
import io.github.henryssondaniel.teacup.protocol.telnet.server.Context;
import io.github.henryssondaniel.teacup.protocol.telnet.server.Request;
import java.util.List;
import java.util.function.Supplier;

/**
 * Simple server.
 *
 * @since 1.0
 */
public interface SimpleServer extends Server {
  /**
   * Removes the supplier from the context.
   *
   * @param supplier the supplier
   * @since 1.0
   */
  void removeSupplier(Supplier<List<Request>> supplier);

  /**
   * Sets the context to the server and returns a supplier.
   *
   * @param context the context
   * @return the supplier
   * @since 1.0
   */
  Supplier<List<Request>> setContext(Context context);
}

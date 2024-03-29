package io.github.henryssondaniel.teacup.protocol.telnet.server;

import io.github.henryssondaniel.teacup.protocol.Server;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.telnetd.BootException;
import net.wimpi.telnetd.TelnetD;
import net.wimpi.telnetd.io.terminal.TerminalManager;
import net.wimpi.telnetd.io.terminal.vt100;
import net.wimpi.telnetd.net.ConnectionManager;
import net.wimpi.telnetd.net.PortListener;
import net.wimpi.telnetd.shell.Shell;

/**
 * Factory class for the server package.
 *
 * @since 1.0
 */
public enum Factory {
  ;

  private static final String CREATE_LISTENER = "Create listener";
  private static final String CREATE_REPLY = "Create reply";
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(Factory.class);

  /**
   * Creates a new {@link Context}.
   *
   * @param reply the reply
   * @return the context
   * @since 1.0
   */
  public static Context createContext(Reply reply) {
    LOGGER.log(Level.FINE, "Create context");
    return new ContextImpl(reply);
  }

  /**
   * Creates a new listener.
   *
   * @param backlog the backlog
   * @param housekeepingInterval the housekeeping interval
   * @param maxConnections the max connections
   * @param port the port
   * @param timeout the timeout
   * @return the listener
   * @since 1.0
   */
  public static Listener createListener(
      int backlog, int housekeepingInterval, int maxConnections, int port, int timeout) {
    LOGGER.log(Level.FINE, CREATE_LISTENER);
    return new ListenerImpl(backlog, housekeepingInterval, maxConnections, port, timeout);
  }

  /**
   * Creates a new listener.
   *
   * @param housekeepingInterval the housekeeping interval
   * @param maxConnections the max connections
   * @param port the port
   * @param timeout the timeout
   * @return the listener
   * @since 1.0
   */
  public static Listener createListener(
      int housekeepingInterval, int maxConnections, int port, int timeout) {
    LOGGER.log(Level.FINE, CREATE_LISTENER);
    return createListener(0, housekeepingInterval, maxConnections, port, timeout);
  }

  /**
   * Creates a new {@link Reply}.
   *
   * @param data the data
   * @return the reply
   * @since 1.0
   */
  public static Reply createReply(byte... data) {
    LOGGER.log(Level.FINE, CREATE_REPLY);
    return new ReplyImpl(data);
  }

  /**
   * Creates a new {@link Reply}.
   *
   * @param data the data
   * @return the reply
   * @since 1.0
   */
  public static Reply createReply(String data) {
    LOGGER.log(Level.FINE, CREATE_REPLY);
    return createReply(data.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Creates a new {@link Server}.
   *
   * @param listener the listener
   * @return the server
   * @since 1.0
   */
  public static Server<Context, Request> createSimpleServer(Listener listener) {
    LOGGER.log(Level.FINE, "Create simple server");
    return createSimpleServer(listener, "default");
  }

  static Server<Context, Request> createSimpleServer(Listener listener, String name) {
    Handler handler = new HandlerImpl();

    var telnetD = TelnetD.createTelnetD();
    telnetD.setListeners(createPortListeners(listener, handler));

    try {
      TerminalManager.createTerminalManager(Collections.singletonMap(name, new vt100()), false);
    } catch (BootException bootException) {
      LOGGER.log(
          Level.SEVERE,
          "The server was not properly initialized and might not behave as expected.",
          bootException);
    }

    return new SimpleImpl(handler, telnetD);
  }

  private static ConnectionManager createConnectionManager(Listener listener, Shell handler) {
    ConnectionManager connectionManager =
        new ConnectionManagerImpl(
            new Stack<>(), listener, Collections.synchronizedList(new ArrayList<>(100)), handler);
    connectionManager.start();

    return connectionManager;
  }

  private static List<PortListener> createPortListeners(Listener listener, Shell shell) {
    var portListener = new PortListener("listener", listener.getPort(), listener.getBacklog());
    portListener.setConnectionManager(createConnectionManager(listener, shell));

    return Collections.singletonList(portListener);
  }
}

package io.github.henryssondaniel.teacup.protocol.telnet.server;

import io.github.henryssondaniel.teacup.protocol.telnet.SimpleServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.telnetd.BootException;
import net.wimpi.telnetd.TelnetD;
import net.wimpi.telnetd.io.terminal.TerminalManager;
import net.wimpi.telnetd.net.PortListener;
import net.wimpi.telnetd.shell.Shell;
import net.wimpi.telnetd.shell.ShellManager;
import net.wimpi.telnetd.util.StringUtil;

/**
 * Factory class for the server package.
 *
 * @since 1.0
 */
public enum Factory {
  ;

  private static final String CREATE_LISTENER = "Create listener";
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(Factory.class);

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
   * Creates a new {@link SimpleServer}.
   *
   * @param listeners the listeners
   * @return the simple server
   * @since 1.0
   */
  public static SimpleServer createSimpleServer(Iterable<? extends Listener> listeners) {
    LOGGER.log(Level.FINE, "Create simple server");
    return createSimpleServer(createProperties(listeners));
  }

  static SimpleServer createSimpleServer(Properties properties) {
    LOGGER.log(Level.FINE, "Create simple server");

    var telnetD = TelnetD.createTelnetD();

    try {
      var hashMap = new HashMap<String, Shell>(1);
      hashMap.put("shell", new ShellImpl());

      telnetD.setShellManager(ShellManager.createShellManager(hashMap));
      TerminalManager.createTerminalManager(properties);
      setListeners(properties, telnetD);
    } catch (BootException bootException) {
      LOGGER.log(
          Level.SEVERE,
          "The server was not properly initialized and might not behave as expected.",
          bootException);
    }

    return new Simple(telnetD);
  }

  private static PortListener createPortListener(String name, Properties properties)
      throws BootException {
    return PortListener.createPortListener(name, properties);
  }

  private static Properties createProperties(Iterable<? extends Listener> listeners) {
    var properties = new Properties();

    properties.setProperty("listeners", getListeners(listeners, properties));
    properties.setProperty("term.vt100.class", "net.wimpi.telnetd.io.terminal.vt100");
    properties.setProperty("term.vt100.aliases", "default");
    properties.setProperty("terminals", "vt100");

    return properties;
  }

  private static String getListeners(
      Iterable<? extends Listener> listeners, Properties properties) {
    var listenersString = new StringBuilder(0);

    var i = 0;
    for (Listener listener : listeners) {
      listenersString.append(listenersString.length() == 0 ? "" : ",").append(i);

      properties.setProperty(i + ".floodprotection", String.valueOf(listener.getBacklog()));
      properties.setProperty(
          i + ".housekeepinginterval", String.valueOf(listener.getHousekeepingInterval()));
      properties.setProperty(i + ".loginshell", "shell");
      properties.setProperty(i + ".maxcon", String.valueOf(listener.getMaxConnections()));
      properties.setProperty(i + ".port", String.valueOf(listener.getPort()));
      properties.setProperty(i + ".time_to_timedout", String.valueOf(listener.getTimeout()));
      properties.setProperty(i + ".time_to_warning", "0");

      i++;
    }

    return listenersString.toString();
  }

  private static void setListeners(Properties properties, TelnetD telnetD) throws BootException {
    var listeners = StringUtil.split(properties.getProperty("listeners"), ",");

    var size = listeners.length;
    List<PortListener> portListeners = new ArrayList<>(listeners.length);

    var i = 0;
    while (i < size) {
      portListeners.add(createPortListener(String.valueOf(i), properties));
      i++;
    }

    telnetD.setListeners(portListeners);
  }
}

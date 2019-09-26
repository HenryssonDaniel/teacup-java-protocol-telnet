package io.github.henryssondaniel.teacup.protocol.telnet.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionData;
import net.wimpi.telnetd.shell.Shell;

class ConnectionImpl extends Connection {
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(ConnectionImpl.class);
  private final Shell shell;

  ConnectionImpl(ConnectionData connectionData, String name, Shell shell) {
    super(new ThreadGroup(name), connectionData);
    this.shell = shell;
  }

  @Override
  public void run() {
    LOGGER.log(Level.FINE, "Run");

    try {
      shell.run(this);
    } finally {
      close();
    }
  }
}

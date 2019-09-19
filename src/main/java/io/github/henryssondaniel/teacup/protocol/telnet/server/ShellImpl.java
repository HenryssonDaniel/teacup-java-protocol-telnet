package io.github.henryssondaniel.teacup.protocol.telnet.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

/**
 * Shell implementation.
 *
 * @since 1.0
 */
public final class ShellImpl implements Shell {
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(ShellImpl.class);

  @Override
  public void connectionIdle(ConnectionEvent connectionEvent) {
    LOGGER.log(Level.FINE, "Connection connection idle");
  }

  @Override
  public void connectionLogoutRequest(ConnectionEvent connectionEvent) {
    LOGGER.log(Level.FINE, "Connection logout request");
  }

  @Override
  public void connectionSentBreak(ConnectionEvent connectionEvent) {
    LOGGER.log(Level.FINE, "Connection sent break");
  }

  @Override
  public void connectionTimedOut(ConnectionEvent connectionEvent) {
    LOGGER.log(Level.FINE, "Connection timed out");
    connectionEvent.getSource().close();
  }

  @Override
  public void run(Connection connection) {
    try {
      var terminalIO = connection.getTerminalIO();

      var incomplete = true;
      while (incomplete) incomplete = isIncomplete(terminalIO);
    } catch (IOException ioException) {
      LOGGER.log(Level.INFO, "Error while listening to incoming requests", ioException);
    }
  }

  private static boolean isIncomplete(BasicTerminalIO basicTerminalIO) throws IOException {
    var readByte = basicTerminalIO.read();

    LOGGER.log(Level.INFO, () -> new String(new byte[] {(byte) readByte}, StandardCharsets.UTF_8));

    var incomplete = true;

    if (readByte < 0) incomplete = false;
    else {
      basicTerminalIO.write("1");
      basicTerminalIO.flush();
    }

    return incomplete;
  }
}

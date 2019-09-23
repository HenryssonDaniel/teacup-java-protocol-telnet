package io.github.henryssondaniel.teacup.protocol.telnet.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;

class HandlerImpl implements Handler {
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(HandlerImpl.class);
  private static final String MESSAGE = "{0}ing the timeout supplier{1}";

  private final List<TimeoutSupplier> timeoutSuppliers = new LinkedList<>();

  private Reply reply;

  @Override
  public void addTimeoutSupplier(TimeoutSupplier timeoutSupplier) {
    LOGGER.log(Level.FINE, MESSAGE, new Object[] {"Add", ""});
    timeoutSuppliers.add(timeoutSupplier);
  }

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
  public Reply getReply() {
    return reply;
  }

  @Override
  public List<TimeoutSupplier> getTimeoutSuppliers() {
    LOGGER.log(Level.FINE, MESSAGE, new Object[] {"Sett", "s"});
    return new ArrayList<>(timeoutSuppliers);
  }

  @Override
  public void removeTimeoutSupplier(TimeoutSupplier timeoutSupplier) {
    LOGGER.log(Level.FINE, MESSAGE, new Object[] {"Remov", ""});
    timeoutSuppliers.remove(timeoutSupplier);
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

  @Override
  public void setReply(Reply reply) {
    this.reply = reply;
  }

  private boolean isIncomplete(BasicTerminalIO basicTerminalIO) throws IOException {
    var readByte = basicTerminalIO.read();

    var incomplete = true;

    if (readByte < 0) incomplete = false;
    else {
      Objects.requireNonNull(reply, "The reply is not set");

      var byteData = (byte) readByte;

      LOGGER.log(
          Level.INFO,
          () -> "Request: " + new String(new byte[] {byteData}, StandardCharsets.UTF_8));

      Request request = new RequestImpl(byteData);
      timeoutSuppliers.forEach(timeoutSupplier -> timeoutSupplier.addRequest(request));

      for (var data : reply.getData()) basicTerminalIO.write(data);
      basicTerminalIO.flush();
    }

    return incomplete;
  }
}

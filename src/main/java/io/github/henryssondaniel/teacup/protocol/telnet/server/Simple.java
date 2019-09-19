package io.github.henryssondaniel.teacup.protocol.telnet.server;

import io.github.henryssondaniel.teacup.protocol.telnet.SimpleServer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.telnetd.TelnetD;

class Simple implements SimpleServer {
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(Simple.class);
  private final TelnetD telnetD;

  Simple(TelnetD telnetD) {
    this.telnetD = telnetD;
  }

  @Override
  public void setUp() {
    LOGGER.log(Level.INFO, "Set up");
    telnetD.start();
  }

  @Override
  public void tearDown() {
    LOGGER.log(Level.INFO, "Tear down");
    telnetD.stop();
  }
}

package io.github.henryssondaniel.teacup.protocol.telnet.server;

import io.github.henryssondaniel.teacup.protocol.server.Base;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.telnetd.TelnetD;

class SimpleImpl extends Base<Context, Handler, Request> implements SimpleBase {
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(SimpleImpl.class);

  private final Handler handler;
  private final TelnetD telnetD;

  SimpleImpl(Handler handler, TelnetD telnetD) {
    this.handler = handler;
    this.telnetD = telnetD;
  }

  @Override
  public Handler createProtocolContext(
      Context context,
      io.github.henryssondaniel.teacup.protocol.server.Handler<Request> requestHandler) {
    LOGGER.log(Level.FINE, "Create protocol context");

    handler.setHandler(requestHandler);
    handler.setReply(context.getReply());

    return handler;
  }

  @Override
  public String getKey(Context context) {
    LOGGER.log(Level.FINE, "Get key");
    return "key";
  }

  @Override
  public boolean isEquals(Context context, Handler protocolContext) {
    LOGGER.log(Level.FINE, "Is equals");
    return Arrays.equals(context.getReply().getData(), protocolContext.getReply().getData());
  }

  @Override
  public void serverCleanup(Handler protocolContext) {
    LOGGER.log(Level.FINE, "Server cleanup");
    protocolContext.setReply(null);
  }

  @Override
  public void setUp() {
    LOGGER.log(Level.FINE, "Set up");
    telnetD.start();
  }

  @Override
  public void tearDown() {
    LOGGER.log(Level.FINE, "Tear down");
    telnetD.stop();
  }
}

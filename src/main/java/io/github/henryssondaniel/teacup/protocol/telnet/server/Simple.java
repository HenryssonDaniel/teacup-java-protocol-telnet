package io.github.henryssondaniel.teacup.protocol.telnet.server;

import io.github.henryssondaniel.teacup.protocol.server.Base;
import io.github.henryssondaniel.teacup.protocol.server.TimeoutSupplier;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.telnetd.TelnetD;

class Simple extends Base<Context, Handler, Request> {
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(Simple.class);

  private final Handler handler;
  private final TelnetD telnetD;

  Simple(Handler handler, TelnetD telnetD) {
    this.handler = handler;
    this.telnetD = telnetD;
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

  @Override
  protected Handler createProtocolContext(
      Context context, TimeoutSupplier<Request> timeoutSupplier) {
    handler.addTimeoutSupplier(timeoutSupplier);
    handler.setReply(context.getReply());

    return handler;
  }

  @Override
  protected Handler getHandler(Handler protocolContext) {
    return protocolContext;
  }

  @Override
  protected String getKey(Context context) {
    return "key";
  }

  @Override
  protected boolean isEquals(Context context, Handler protocolContext) {
    return Arrays.equals(context.getReply().getData(), protocolContext.getReply().getData());
  }

  @Override
  protected void serverCleanup(Handler protocolContext) {
    protocolContext.setReply(null);
  }
}

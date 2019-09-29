package io.github.henryssondaniel.teacup.protocol.telnet.server;

import io.github.henryssondaniel.teacup.protocol.telnet.SimpleServer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.telnetd.TelnetD;

class Simple implements SimpleServer {
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(Simple.class);

  private final Handler handler;
  private final Object lock = new Object();
  private final TelnetD telnetD;

  private boolean waiting = true;

  Simple(Handler handler, TelnetD telnetD) {
    this.handler = handler;
    this.telnetD = telnetD;
  }

  @Override
  public void removeSupplier(Supplier<List<Request>> supplier) {
    LOGGER.log(Level.FINE, "Remove supplier");
    if (supplier instanceof TimeoutSupplier) ((TimeoutSupplier) supplier).stop();
  }

  @Override
  public Supplier<List<Request>> setContext(Context context) {
    LOGGER.log(Level.FINE, "Set context");

    TimeoutSupplier timeoutSupplier = new TimeoutSupplierImpl(new ReentrantLock());

    try {
      addSupplier(context, timeoutSupplier);
      timeoutSupplier.whenStopped(consumer -> cleanup(timeoutSupplier));
    } catch (InterruptedException e) {
      LOGGER.log(Level.SEVERE, "The server got interrupted", e);
      Thread.currentThread().interrupt();
    }

    return timeoutSupplier;
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

  private void addSupplier(Context context, TimeoutSupplier timeoutSupplier)
      throws InterruptedException {
    synchronized (lock) {
      var reply = handler.getReply();

      if (reply == null) setReply(context.getReply(), timeoutSupplier);
      else tryAddSupplier(context, reply.getData(), timeoutSupplier);
    }
  }

  private void cleanup(TimeoutSupplier timeoutSupplier) {
    handler.removeTimeoutSupplier(timeoutSupplier);

    synchronized (lock) {
      if (handler.getTimeoutSuppliers().isEmpty()) {
        handler.setReply(null);
        waiting = false;
        lock.notifyAll();
      }
    }
  }

  private void setReply(Reply reply, TimeoutSupplier timeoutSupplier) {
    handler.addTimeoutSupplier(timeoutSupplier);
    handler.setReply(reply);
  }

  private void tryAddSupplier(Context context, byte[] data, TimeoutSupplier timeoutSupplier)
      throws InterruptedException {
    if (Arrays.equals(context.getReply().getData(), data))
      handler.addTimeoutSupplier(timeoutSupplier);
    else
      synchronized (lock) {
        while (waiting) lock.wait(1L);

        waiting = true;
        addSupplier(context, timeoutSupplier);
      }
  }
}

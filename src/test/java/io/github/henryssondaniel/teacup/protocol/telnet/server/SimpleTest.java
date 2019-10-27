package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.github.henryssondaniel.teacup.protocol.Server;
import io.github.henryssondaniel.teacup.protocol.server.TimeoutSupplier;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;
import net.wimpi.telnetd.TelnetD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SimpleTest {
  private final Context context = mock(Context.class);
  private final Handler handler = mock(Handler.class);
  private final Object lock = new Object();
  private final Reply reply = mock(Reply.class);
  private final Reply replyDifferent = mock(Reply.class);
  private final TelnetD telnetD = mock(TelnetD.class);
  private final Server<Context, Request> simpleServer = new Simple(handler, telnetD);
  private final Object verifyLock = new Object();

  @Mock private Supplier<List<Request>> supplierNonExisting;
  @Mock private TimeoutSupplier<Request> timeoutSupplier;
  private boolean waitVerify = true;
  private boolean waiting = true;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.initMocks(this);

    when(context.getReply()).thenReturn(reply);
    when(reply.getData()).thenReturn("message".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void removeSupplier() {
    simpleServer.removeSupplier(supplierNonExisting);
    verifyNoInteractions(supplierNonExisting);
  }

  @Test
  void removeSupplierWhenTimeoutSupplier() {
    simpleServer.removeSupplier(timeoutSupplier);
    verify(timeoutSupplier).stop();
  }

  @Test
  void setContext() {
    simpleServer.setContext(context);

    verify(context).getReply();
    verifyNoMoreInteractions(context);

    verify(handler).addTimeoutSupplier(any());
    verify(handler).setReply(reply);
    verifyNoMoreInteractions(handler);

    verifyNoInteractions(reply);
  }

  @Test
  void setContextWhenReply() throws InterruptedException {
    var supplier = simpleServer.setContext(context);

    when(handler.getReply()).thenReturn(replyDifferent, reply);

    createThread("2".getBytes(StandardCharsets.UTF_8));
    removeSupplier(supplier);

    synchronized (verifyLock) {
      while (waitVerify) verifyLock.wait(1L);

      verify(context, times(3)).getReply();
      verifyNoMoreInteractions(context);

      verify(handler, times(2)).addTimeoutSupplier(any());
      verify(handler).getReply();
      verify(handler).getTimeoutSuppliers();
      verify(handler).removeTimeoutSupplier(any());
      verify(handler).setReply(null);
      verify(handler, times(2)).setReply(reply);
      verifyNoMoreInteractions(handler);

      verify(reply).getData();
      verifyNoMoreInteractions(reply);

      verify(replyDifferent).getData();
      verifyNoMoreInteractions(replyDifferent);
    }
  }

  @Test
  void setUp() {
    simpleServer.setUp();

    verifyNoInteractions(handler);

    verify(telnetD).start();
    verifyNoMoreInteractions(telnetD);
  }

  @Test
  void tearDown() {
    simpleServer.tearDown();

    verifyNoInteractions(handler);

    verify(telnetD).stop();
    verifyNoMoreInteractions(telnetD);
  }

  private Thread createThread(byte... bytes) {
    var thread =
        new Thread(
            () -> {
              when(replyDifferent.getData()).thenAnswer(invocationOnMock -> waiting(bytes));
              setSecondContext();
            });
    thread.start();
    return thread;
  }

  private void interrupt(Thread thread) throws InterruptedException {
    synchronized (lock) {
      while (waiting) lock.wait(1L);

      thread.interrupt();
    }
  }

  private void removeSupplier(Supplier<List<Request>> supplier) throws InterruptedException {
    synchronized (lock) {
      while (waiting) lock.wait(1L);

      simpleServer.removeSupplier(supplier);
    }
  }

  private void setSecondContext() {
    synchronized (verifyLock) {
      simpleServer.setContext(context);
      waitVerify = false;
      verifyLock.notifyAll();
    }
  }

  private Object waiting(Object object) {
    synchronized (lock) {
      waiting = false;
      lock.notifyAll();
    }

    return object;
  }
}

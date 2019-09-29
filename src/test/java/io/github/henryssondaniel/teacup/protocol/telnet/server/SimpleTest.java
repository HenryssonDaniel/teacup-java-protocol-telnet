package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import io.github.henryssondaniel.teacup.protocol.telnet.SimpleServer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
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
  private final SimpleServer simpleServer = new Simple(handler, telnetD);
  private final TimeoutSupplier timeoutSupplier = mock(TimeoutSupplier.class);
  private final Object verifyLock = new Object();

  @Mock private Supplier<List<Request>> supplierNonExisting;
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
    verifyZeroInteractions(supplierNonExisting);
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

    verify(handler).addTimeoutSupplier(any(TimeoutSupplier.class));
    verify(handler).getReply();
    verify(handler).setReply(reply);
    verifyNoMoreInteractions(handler);

    verifyZeroInteractions(reply);
  }

  @Test
  void setContextWhenDuplicateContext() {
    when(handler.getReply()).thenReturn(reply);

    simpleServer.setContext(context);

    verify(context).getReply();
    verifyNoMoreInteractions(context);

    verify(handler).addTimeoutSupplier(any(TimeoutSupplier.class));
    verify(handler).getReply();
    verifyNoMoreInteractions(handler);

    verify(reply, times(2)).getData();
    verifyNoMoreInteractions(reply);
  }

  @Test
  void setContextWhenInterrupted() throws InterruptedException {
    var supplier = simpleServer.setContext(context);

    when(handler.getReply()).thenReturn(replyDifferent);
    when(handler.getTimeoutSuppliers())
        .thenAnswer(invocationOnMock -> waiting(Collections.singletonList(timeoutSupplier)));
    when(replyDifferent.getData()).thenReturn("other message".getBytes(StandardCharsets.UTF_8));

    var thread = createThread();
    removeSupplier(supplier);
    interrupt(thread);

    synchronized (verifyLock) {
      while (waitVerify) verifyLock.wait(1L);

      verify(context, times(2)).getReply();
      verifyNoMoreInteractions(context);

      verify(handler).addTimeoutSupplier(any(TimeoutSupplier.class));
      verify(handler, times(2)).getReply();
      verify(handler).getTimeoutSuppliers();
      verify(handler).removeTimeoutSupplier(any(TimeoutSupplier.class));
      verify(handler).setReply(reply);
      verifyNoMoreInteractions(handler);

      verify(reply).getData();
      verifyNoMoreInteractions(reply);

      verify(replyDifferent).getData();
      verifyNoMoreInteractions(replyDifferent);
    }
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

      verify(handler, times(2)).addTimeoutSupplier(any(TimeoutSupplier.class));
      verify(handler, times(3)).getReply();
      verify(handler).getTimeoutSuppliers();
      verify(handler).removeTimeoutSupplier(any(TimeoutSupplier.class));
      verify(handler).setReply(null);
      verify(handler).setReply(reply);
      verifyNoMoreInteractions(handler);

      verify(reply, times(3)).getData();
      verifyNoMoreInteractions(reply);

      verify(replyDifferent).getData();
      verifyNoMoreInteractions(replyDifferent);
    }
  }

  @Test
  void setUp() {
    simpleServer.setUp();

    verifyZeroInteractions(handler);

    verify(telnetD).start();
    verifyNoMoreInteractions(telnetD);
  }

  @Test
  void tearDown() {
    simpleServer.tearDown();

    verifyZeroInteractions(handler);

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

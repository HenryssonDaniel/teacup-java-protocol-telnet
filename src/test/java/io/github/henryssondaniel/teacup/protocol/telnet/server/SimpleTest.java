package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.github.henryssondaniel.teacup.protocol.Server;
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
  void setContext() {
    simpleServer.setContext(context);

    verify(context).getReply();
    verifyNoMoreInteractions(context);

    verify(handler).setHandler(any());
    verify(handler).setReply(reply);
    verifyNoMoreInteractions(handler);

    verifyNoInteractions(reply);
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
}

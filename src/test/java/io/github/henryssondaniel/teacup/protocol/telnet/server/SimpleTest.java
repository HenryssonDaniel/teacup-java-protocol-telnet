package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import net.wimpi.telnetd.TelnetD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SimpleTest {
  private final Context context = mock(Context.class);
  private final Handler handler = mock(Handler.class);
  private final Reply reply = mock(Reply.class);
  private final TelnetD telnetD = mock(TelnetD.class);
  private final Simple simple = new Simple(handler, telnetD);

  @Mock private io.github.henryssondaniel.teacup.protocol.server.Handler<Request> requestHandler;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.initMocks(this);
    when(context.getReply()).thenReturn(reply);
  }

  @Test
  void createProtocolContext() {
    assertThat(simple.createProtocolContext(context, requestHandler)).isNotNull();

    verify(context).getReply();
    verifyNoMoreInteractions(context);

    verify(handler).setHandler(requestHandler);
    verify(handler).setReply(reply);
    verifyNoMoreInteractions(handler);

    verifyNoInteractions(requestHandler);
    verifyNoInteractions(telnetD);
  }

  @Test
  void getKey() {
    assertThat(simple.getKey(context)).isEqualTo("key");

    verifyNoInteractions(context);
    verifyNoInteractions(handler);
    verifyNoInteractions(telnetD);
  }

  @Test
  void isEquals() {
    when(handler.getReply()).thenReturn(reply);

    assertThat(simple.isEquals(context, handler)).isTrue();

    verify(context).getReply();
    verifyNoMoreInteractions(context);

    verify(handler).getReply();
    verifyNoMoreInteractions(handler);

    verifyNoInteractions(telnetD);
  }

  @Test
  void serverCleanup() {
    simple.serverCleanup(handler);

    verify(handler).setReply(null);
    verifyNoMoreInteractions(handler);

    verifyNoInteractions(telnetD);
  }

  @Test
  void setUp() {
    simple.setUp();

    verifyNoInteractions(handler);

    verify(telnetD).start();
    verifyNoMoreInteractions(telnetD);
  }

  @Test
  void tearDown() {
    simple.tearDown();

    verifyNoInteractions(handler);

    verify(telnetD).stop();
    verifyNoMoreInteractions(telnetD);
  }
}

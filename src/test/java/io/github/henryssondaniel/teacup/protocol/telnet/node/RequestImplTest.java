package io.github.henryssondaniel.teacup.protocol.telnet.node;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;
import io.github.henryssondaniel.teacup.protocol.telnet.server.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RequestImplTest {
  private final Request request = mock(Request.class);
  private final RequestSetter requestSetter = new RequestImpl();

  @Mock private GenericObjectAssert<? super Byte, ?> genericByteAssert;
  @Mock private GenericObjectAssert<Request, ?> genericObjectAssert;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void setAssertion() {
    requestSetter.setAssertion(genericObjectAssert);
    requestSetter.verify(request);

    verify(genericObjectAssert).verify(request);
    verifyNoMoreInteractions(genericObjectAssert);

    verifyNoInteractions(request);
  }

  @Test
  void setCommand() {
    requestSetter.setCommand(genericByteAssert);
    requestSetter.verify(request);

    verify(genericByteAssert).verify(request.getCommand());
    verifyNoMoreInteractions(genericByteAssert);

    verify(request, times(2)).getCommand();
    verifyNoMoreInteractions(request);
  }

  @Test
  void setCommandAsString() {
    GenericStringAssert<?> genericStringAssert = mock(GenericStringAssert.class);

    requestSetter.setCommandAsString(genericStringAssert);
    requestSetter.verify(request);

    verify(genericStringAssert).verify(request.getCommandAsString());
    verifyNoMoreInteractions(genericStringAssert);

    verify(request, times(2)).getCommandAsString();
    verifyNoMoreInteractions(request);
  }
}

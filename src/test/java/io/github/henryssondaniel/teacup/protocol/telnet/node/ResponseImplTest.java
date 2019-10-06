package io.github.henryssondaniel.teacup.protocol.telnet.node;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import io.github.henryssondaniel.teacup.core.assertion.GenericByteArrayAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;
import io.github.henryssondaniel.teacup.protocol.telnet.client.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ResponseImplTest {
  private final Response response = mock(Response.class);
  private final ResponseSetter responseSetter = new ResponseImpl();

  @Mock private GenericObjectAssert<Response, ?> genericObjectAssert;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void setAssertion() {
    responseSetter.setAssertion(genericObjectAssert);
    responseSetter.verify(response);

    verify(genericObjectAssert).verify(response);
    verifyNoMoreInteractions(genericObjectAssert);

    verifyNoInteractions(response);
  }

  @Test
  void setCode() {
    GenericByteArrayAssert<?> genericByteArrayAssert = mock(GenericByteArrayAssert.class);

    responseSetter.setData(genericByteArrayAssert);
    responseSetter.verify(response);

    verify(genericByteArrayAssert).verify(response.getData());
    verifyNoMoreInteractions(genericByteArrayAssert);

    verify(response, times(2)).getData();
    verifyNoMoreInteractions(response);
  }

  @Test
  void setText() {
    GenericStringAssert<?> genericStringAssert = mock(GenericStringAssert.class);

    responseSetter.setDataAsString(genericStringAssert);
    responseSetter.verify(response);

    verify(genericStringAssert).verify(response.getDataAsString());
    verifyNoMoreInteractions(genericStringAssert);

    verify(response, times(2)).getDataAsString();
    verifyNoMoreInteractions(response);
  }
}

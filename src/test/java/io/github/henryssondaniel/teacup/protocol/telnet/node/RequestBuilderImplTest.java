package io.github.henryssondaniel.teacup.protocol.telnet.node;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RequestBuilderImplTest {

  @InjectMocks private final RequestBuilder requestBuilder = new RequestBuilderImpl();
  @Mock private GenericObjectAssert<? super Byte, ?> genericObjectAssert;
  @Mock private RequestSetter implementation;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createImplementation() {
    assertThat(new RequestBuilderImpl().createImplementation())
        .isExactlyInstanceOf(RequestImpl.class);
  }

  @Test
  void setCommand() {
    assertThat(requestBuilder.setCommand(genericObjectAssert)).isSameAs(requestBuilder);
    verify(implementation).setCommand(genericObjectAssert);
  }

  @Test
  void setCommandAsString() {
    GenericStringAssert<?> genericStringAssert = mock(GenericStringAssert.class);
    assertThat(requestBuilder.setCommandAsString(genericStringAssert)).isSameAs(requestBuilder);
    verify(implementation).setCommandAsString(genericStringAssert);
  }
}

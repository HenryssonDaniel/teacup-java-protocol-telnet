package io.github.henryssondaniel.teacup.protocol.telnet.node;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.github.henryssondaniel.teacup.core.assertion.GenericByteArrayAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ResponseBuilderImplTest {
  @InjectMocks private final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
  @Mock private ResponseSetter implementation;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createImplementation() {
    assertThat(new ResponseBuilderImpl().createImplementation())
        .isExactlyInstanceOf(ResponseImpl.class);
  }

  @Test
  void setCode() {
    var genericByteArrayAssert = mock(GenericByteArrayAssert.class);

    assertThat(responseBuilder.setData(genericByteArrayAssert)).isSameAs(responseBuilder);
    verify(implementation).setData(genericByteArrayAssert);
  }

  @Test
  void setText() {
    var genericStringAssert = mock(GenericStringAssert.class);

    assertThat(responseBuilder.setDataAsString(genericStringAssert)).isSameAs(responseBuilder);
    verify(implementation).setDataAsString(genericStringAssert);
  }
}

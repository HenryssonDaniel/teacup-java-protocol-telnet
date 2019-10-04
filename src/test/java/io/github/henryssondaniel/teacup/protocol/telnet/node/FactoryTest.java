package io.github.henryssondaniel.teacup.protocol.telnet.node;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FactoryTest {
  @Test
  void createRequestBuilder() {
    assertThat(Factory.createRequestBuilder()).isExactlyInstanceOf(RequestBuilderImpl.class);
  }

  @Test
  void createResponseBuilder() {
    assertThat(Factory.createResponseBuilder()).isExactlyInstanceOf(ResponseBuilderImpl.class);
  }
}

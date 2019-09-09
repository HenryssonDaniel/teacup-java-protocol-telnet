package io.github.henryssondaniel.teacup.protocol.telnet.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FactoryTest {
  @Test
  void createClient() {
    assertThat(Factory.createClient("terminal")).isExactlyInstanceOf(Simple.class);
  }
}

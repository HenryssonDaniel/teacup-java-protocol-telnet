package io.github.henryssondaniel.teacup.protocol.telnet.client;

import java.util.function.Supplier;

interface ResponseSupplier extends Supplier<String> {
  void interrupt();

  void start();
}

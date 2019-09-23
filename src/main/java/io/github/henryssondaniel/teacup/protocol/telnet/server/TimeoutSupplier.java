package io.github.henryssondaniel.teacup.protocol.telnet.server;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

interface TimeoutSupplier extends Supplier<List<Request>> {
  void addRequest(Request request);

  void stop();

  void whenStopped(Consumer<? super List<Request>> consumer);
}

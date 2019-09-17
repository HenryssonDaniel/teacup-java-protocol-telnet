package io.github.henryssondaniel.teacup.protocol.telnet.client;

import io.github.henryssondaniel.teacup.core.logging.Factory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

class ResponseSupplierImpl implements ResponseSupplier {
  private static final int BUFFER_SIZE = 1024;
  private static final Logger LOGGER = Factory.getLogger(ResponseSupplierImpl.class);

  private final Object lock = new Object();
  private final Thread thread;

  private String response;
  private boolean waiting = true;

  ResponseSupplierImpl(InputStream inputStream) {
    thread = new Thread(() -> readResponse(inputStream));
  }

  @Override
  public String get() {
    LOGGER.log(Level.FINE, "Get");

    String currentResponse;

    synchronized (lock) {
      currentResponse = Optional.ofNullable(response).orElseGet(this::getResponse);
      response = null;
    }

    return currentResponse;
  }

  @Override
  public void interrupt() {
    LOGGER.log(Level.FINE, "Start");
    thread.interrupt();

    synchronized (lock) {
      waiting = false;
      lock.notifyAll();
    }
  }

  @Override
  public void start() {
    LOGGER.log(Level.FINE, "Start");
    thread.start();
  }

  private String getResponse() {
    String currentResponse = null;

    if (thread.isAlive())
      try {
        synchronized (lock) {
          while (waiting) lock.wait(1L);

          waiting = true;

          currentResponse = response;
          response = null;
        }
      } catch (InterruptedException interruptedException) {
        LOGGER.log(Level.SEVERE, "The thread got interrupted", interruptedException);
      }

    return currentResponse;
  }

  private int readResponse(InputStream inputStream, byte... buff) throws IOException {
    var bytesRead = inputStream.read(buff);

    if (bytesRead > 0) {
      var currentResponse = new String(buff, 0, bytesRead, StandardCharsets.UTF_8);

      LOGGER.log(Level.INFO, currentResponse);

      synchronized (lock) {
        response = Objects.requireNonNullElse(response, "") + currentResponse;
        waiting = false;
        lock.notifyAll();
      }
    }

    return bytesRead;
  }

  private void readResponse(InputStream inputStream) {
    try {
      var buff = new byte[BUFFER_SIZE];

      int bytesRead;
      do bytesRead = readResponse(inputStream, buff);
      while (bytesRead >= 0);
    } catch (IOException ioException) {
      LOGGER.log(Level.SEVERE, "Exception while reading socket", ioException);

      synchronized (lock) {
        waiting = false;
        lock.notifyAll();
      }
    }
  }
}

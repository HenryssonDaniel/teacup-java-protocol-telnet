package io.github.henryssondaniel.teacup.protocol.telnet.client;

import io.github.henryssondaniel.teacup.core.logging.Factory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

class ResponseSupplierImpl implements ResponseSupplier {
  private static final int BUFFER_SIZE = 1024;
  private static final Logger LOGGER = Factory.getLogger(ResponseSupplierImpl.class);

  private final Object lock = new Object();
  private final Thread thread;

  private ResponseAdder responseAdder;
  private boolean waiting = true;

  ResponseSupplierImpl(InputStream inputStream) {
    thread = new Thread(() -> readResponse(inputStream));
  }

  @Override
  public Response get() {
    LOGGER.log(Level.FINE, "Get");

    Response response;

    synchronized (lock) {
      response = Optional.ofNullable((Response) responseAdder).orElseGet(this::getResponse);
      responseAdder = null;
    }

    return response;
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

  private Response getResponse() {
    Response response = null;

    if (thread.isAlive())
      try {
        synchronized (lock) {
          while (waiting) lock.wait(1L);

          waiting = true;

          response = responseAdder;
          responseAdder = null;
        }
      } catch (InterruptedException interruptedException) {
        LOGGER.log(Level.SEVERE, "The thread got interrupted", interruptedException);
        Thread.currentThread().interrupt();
      }

    return response;
  }

  private int readResponse(InputStream inputStream, byte... buff) throws IOException {
    var bytesRead = inputStream.read(buff);

    if (bytesRead > 0) {
      var currentResponse = new String(buff, 0, bytesRead, StandardCharsets.UTF_8);

      LOGGER.log(Level.INFO, currentResponse);

      var bytes = new byte[bytesRead];
      System.arraycopy(buff, 0, bytes, 0, bytesRead);

      synchronized (lock) {
        if (responseAdder == null) responseAdder = new ResponseImpl(currentResponse, bytes);
        else responseAdder.addData(currentResponse, bytes);

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
    }

    synchronized (lock) {
      waiting = false;
      lock.notifyAll();
    }
  }
}

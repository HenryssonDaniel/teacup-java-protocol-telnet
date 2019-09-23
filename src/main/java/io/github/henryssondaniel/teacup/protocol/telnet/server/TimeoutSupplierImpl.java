package io.github.henryssondaniel.teacup.protocol.telnet.server;

import io.github.henryssondaniel.teacup.core.logging.Factory;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

class TimeoutSupplierImpl implements TimeoutSupplier {
  private static final Logger LOGGER = Factory.getLogger(TimeoutSupplierImpl.class);

  private final Lock lock;
  private final Condition notEmpty;
  private final List<Request> requests = new LinkedList<>();

  private Consumer<? super List<Request>> consumerStopped;
  private volatile boolean empty = true;
  private boolean running = true;

  TimeoutSupplierImpl(Lock lock) {
    this.lock = lock;
    notEmpty = lock.newCondition();
  }

  @Override
  public void addRequest(Request request) {
    LOGGER.log(Level.FINE, "Adding the request");

    lock.lock();
    try {
      requests.add(request);
      empty = false;
      notEmpty.signalAll();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<Request> get() {
    List<Request> temporaryList = new ArrayList<>(requests);

    if (running) {
      lock.lock();

      try {
        while (empty) notEmpty.await();

        temporaryList.clear();
        temporaryList.addAll(requests);

        requests.clear();
        empty = true;
      } catch (InterruptedException e) {
        LOGGER.log(Level.SEVERE, "The supplier got interrupted", e);
        Thread.currentThread().interrupt();
      } finally {
        lock.unlock();
      }
    } else requests.clear();

    return temporaryList;
  }

  @Override
  public void stop() {
    LOGGER.log(Level.FINE, "Stopping the supplier");
    consumerStopped.accept(null);
    running = false;
  }

  @Override
  public void whenStopped(Consumer<? super List<Request>> consumer) {
    LOGGER.log(Level.FINE, "When stopped");
    consumerStopped = consumer;
  }
}

package io.github.henryssondaniel.teacup.protocol.telnet.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TimeoutSupplierImplTest {
  private final Lock lock = mock(Lock.class);

  @Mock private Consumer<? super List<Request>> consumer;
  private TimeoutSupplier timeoutSupplier;

  @Test
  void addAndGetRequest() {
    var request = mock(Request.class);
    timeoutSupplier.addRequest(request);
    assertThat(timeoutSupplier.get()).containsExactly(request);
  }

  @BeforeEach
  void beforeEach() throws InterruptedException {
    MockitoAnnotations.initMocks(this);

    var condition = mock(Condition.class);
    doNothing().doThrow(new InterruptedException("test")).when(condition).await();
    when(lock.newCondition()).thenReturn(condition);

    timeoutSupplier = new TimeoutSupplierImpl(lock);
  }

  @Test
  void getWhenInterrupted() {
    assertThat(timeoutSupplier.get()).isEmpty();
  }

  @Test
  void getWhenNotRunning() {
    timeoutSupplier.whenStopped(consumer);
    timeoutSupplier.stop();

    verify(consumer).accept(null);
    assertThat(timeoutSupplier.get()).isEmpty();
  }

  @Test
  void stop() {
    timeoutSupplier.whenStopped(consumer);
    timeoutSupplier.stop();

    verify(consumer).accept(null);
  }
}

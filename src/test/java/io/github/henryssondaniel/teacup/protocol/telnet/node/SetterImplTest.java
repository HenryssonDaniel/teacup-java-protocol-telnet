package io.github.henryssondaniel.teacup.protocol.telnet.node;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class SetterImplTest {
  private static final Logger LOGGER = Logger.getLogger(SetterImplTest.class.getName());
  private final Setter<String, GenericObjectAssert<String, ?>> setter = new TestSetterImpl();
  @Mock private GenericObjectAssert<String, ?> genericObjectAssert;

  @Test
  void getAndSetAssertion() {
    setter.setAssertion(genericObjectAssert);
    assertThat(setter.getAssertion()).isSameAs(genericObjectAssert);
  }

  @Test
  void getAssertion() {
    assertThat(setter.getAssertion()).isNull();
  }

  private static final class TestSetterImpl
      extends SetterImpl<String, GenericObjectAssert<String, ?>> {
    @Override
    public void verify(String actual) {
      LOGGER.log(Level.FINE, "Verify the test");
    }
  }
}

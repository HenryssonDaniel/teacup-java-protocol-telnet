package io.github.henryssondaniel.teacup.protocol.telnet.node;

import static org.mockito.Mockito.verify;

import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.protocol.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BuilderImplTest {
  @Mock private GenericObjectAssert<String, ?> genericObjectAssert;
  @Mock private Setter<String, GenericObjectAssert<String, ?>> implementation;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void doAssertion() {
    new TestBuilderImpl(implementation).doAssertion(genericObjectAssert);
    verify(implementation).setAssertion(genericObjectAssert);
  }

  private static final class TestBuilderImpl
      extends BuilderImpl<
          String, Node<String>, Setter<String, GenericObjectAssert<String, ?>>, TestBuilderImpl> {
    private TestBuilderImpl(Setter<String, GenericObjectAssert<String, ?>> setter) {
      super(setter);
    }

    @Override
    protected Setter<String, GenericObjectAssert<String, ?>> createImplementation() {
      return null;
    }
  }
}

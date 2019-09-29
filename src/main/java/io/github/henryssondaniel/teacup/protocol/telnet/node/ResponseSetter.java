package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.assertion.GenericByteArrayAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;

interface ResponseSetter
    extends Response,
        Setter<
            io.github.henryssondaniel.teacup.protocol.telnet.client.Response,
            GenericObjectAssert<
                io.github.henryssondaniel.teacup.protocol.telnet.client.Response, ?>> {
  void setData(GenericByteArrayAssert<?> data);

  void setDataAsString(GenericStringAssert<?> dataAsString);
}

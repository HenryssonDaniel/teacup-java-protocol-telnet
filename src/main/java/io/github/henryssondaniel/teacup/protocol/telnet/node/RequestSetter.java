package io.github.henryssondaniel.teacup.protocol.telnet.node;

import io.github.henryssondaniel.teacup.core.assertion.GenericObjectAssert;
import io.github.henryssondaniel.teacup.core.assertion.GenericStringAssert;

interface RequestSetter
    extends Request,
        Setter<
            io.github.henryssondaniel.teacup.protocol.telnet.server.Request,
            GenericObjectAssert<
                io.github.henryssondaniel.teacup.protocol.telnet.server.Request, ?>> {
  void setCommand(GenericObjectAssert<? super Byte, ?> command);

  void setCommandAsString(GenericStringAssert<?> commandAsString);
}

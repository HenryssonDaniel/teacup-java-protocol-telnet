package io.github.henryssondaniel.teacup.protocol.telnet.server;

import io.github.henryssondaniel.teacup.core.logging.Factory;
import java.util.logging.Level;
import java.util.logging.Logger;

class ReplyImpl implements Reply {
  private static final Logger LOGGER = Factory.getLogger(ReplyImpl.class);
  private final byte[] data;

  ReplyImpl(byte... data) {
    this.data = data.clone();
  }

  @Override
  public byte[] getData() {
    LOGGER.log(Level.FINE, "Get data");
    return data.clone();
  }
}

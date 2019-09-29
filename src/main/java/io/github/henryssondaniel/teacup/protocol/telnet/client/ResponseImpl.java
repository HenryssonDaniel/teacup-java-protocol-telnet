package io.github.henryssondaniel.teacup.protocol.telnet.client;

import java.util.logging.Level;
import java.util.logging.Logger;

class ResponseImpl implements ResponseAdder {
  private static final Logger LOGGER =
      io.github.henryssondaniel.teacup.core.logging.Factory.getLogger(ResponseImpl.class);

  private byte[] bytes;
  private String bytesAsString;

  ResponseImpl(String dataAsString, byte... data) {
    bytes = data.clone();
    bytesAsString = dataAsString;
  }

  @Override
  public void addData(String dataAsString, byte... data) {
    LOGGER.log(Level.FINE, "Add data");

    var newBytes = new byte[bytes.length + data.length];
    System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
    System.arraycopy(data, 0, newBytes, bytes.length, data.length);

    bytes = newBytes;
    bytesAsString += dataAsString;
  }

  @Override
  public byte[] getData() {
    LOGGER.log(Level.FINE, "Get data");
    return bytes.clone();
  }

  @Override
  public String getDataAsString() {
    LOGGER.log(Level.FINE, "Get data as string");
    return bytesAsString;
  }
}

package io.github.henryssondaniel.teacup.protocol.telnet.server;

import java.nio.charset.StandardCharsets;

class RequestImpl implements Request {
  private final byte command;
  private final String commandAsString;

  RequestImpl(byte command) {
    this.command = command;
    commandAsString = new String(new byte[] {command}, StandardCharsets.UTF_8);
  }

  @Override
  public byte getCommand() {
    return command;
  }

  @Override
  public String getCommandAsString() {
    return commandAsString;
  }
}

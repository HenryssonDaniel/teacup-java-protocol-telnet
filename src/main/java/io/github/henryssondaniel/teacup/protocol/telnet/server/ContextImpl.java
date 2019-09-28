package io.github.henryssondaniel.teacup.protocol.telnet.server;

class ContextImpl implements Context {
  private final Reply reply;

  ContextImpl(Reply reply) {
    this.reply = reply;
  }

  @Override
  public Reply getReply() {
    return reply;
  }
}

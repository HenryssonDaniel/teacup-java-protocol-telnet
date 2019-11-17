package io.github.henryssondaniel.teacup.protocol.telnet.server;

interface SimpleBase extends Simple {
  Handler createProtocolContext(
      Context context,
      io.github.henryssondaniel.teacup.protocol.server.Handler<Request> requestHandler);

  String getKey(Context context);

  boolean isEquals(Context context, Handler protocolContext);

  void serverCleanup(Handler protocolContext);
}

package io.github.henryssondaniel.teacup.protocol.telnet.server;

import net.wimpi.telnetd.shell.Shell;

interface Handler extends Shell, io.github.henryssondaniel.teacup.protocol.server.Handler<Request> {
  Reply getReply();

  void setReply(Reply reply);
}

package io.github.henryssondaniel.teacup.protocol.telnet.server;

import net.wimpi.telnetd.shell.Shell;

interface Handler extends Shell {
  Reply getReply();

  void setHandler(io.github.henryssondaniel.teacup.protocol.server.Handler<Request> handler);

  void setReply(Reply reply);
}

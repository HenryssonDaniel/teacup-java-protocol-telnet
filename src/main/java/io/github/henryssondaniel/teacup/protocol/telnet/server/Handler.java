package io.github.henryssondaniel.teacup.protocol.telnet.server;

import java.util.List;
import net.wimpi.telnetd.shell.Shell;

interface Handler extends Shell {
  void addTimeoutSupplier(TimeoutSupplier timeoutSupplier);

  Reply getReply();

  List<TimeoutSupplier> getTimeoutSuppliers();

  void removeTimeoutSupplier(TimeoutSupplier timeoutSupplier);

  void setReply(Reply reply);
}

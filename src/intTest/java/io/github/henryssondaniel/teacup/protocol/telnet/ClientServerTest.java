package io.github.henryssondaniel.teacup.protocol.telnet;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.henryssondaniel.teacup.protocol.telnet.client.Factory;
import java.io.IOException;
import java.net.Proxy;
import java.nio.charset.Charset;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.junit.jupiter.api.Test;

class ClientServerTest {
  private static final String LOCALHOST = "localhost";
  private static final int PORT = 1234;

  @Test
  void send() throws IOException {
    var client = Factory.createClient("vt100");
    client.setCharset(Charset.defaultCharset());
    client.setConnectTimeout(Integer.MAX_VALUE);
    client.setDefaultPort(PORT);
    client.setDefaultTimeout(Integer.MAX_VALUE);
    client.setProxy(Proxy.NO_PROXY);
    client.setReceiveBufferSize(Integer.MAX_VALUE);
    client.setSendBufferSize(Integer.MAX_VALUE);
    client.setServerSocketFactory(ServerSocketFactory.getDefault());
    client.setSocketFactory(SocketFactory.getDefault());

    var simpleServer =
        io.github.henryssondaniel.teacup.protocol.telnet.server.Factory.createSimpleServer(
            io.github.henryssondaniel.teacup.protocol.telnet.server.Factory.createListener(
                1, 2, PORT, Integer.MAX_VALUE));
    simpleServer.setUp();
    var supplier =
        simpleServer.setContext(
            io.github.henryssondaniel.teacup.protocol.telnet.server.Factory.createContext(
                io.github.henryssondaniel.teacup.protocol.telnet.server.Factory.createReply("t")));

    var responses = client.connect(LOCALHOST);

    client.setKeepAlive(false);
    client.setSoLinger(false, Integer.MAX_VALUE);
    client.setSoTimeout(Integer.MAX_VALUE);
    client.setTcpNoDelay(false);

    client.send("td");

    var response = responses.get();

    assertThat(response.getData()).isEqualTo("t".getBytes(Charset.defaultCharset()));
    assertThat(response.getDataAsString()).isEqualTo("t");

    client.disconnect();

    var requests = supplier.get();
    assertThat(requests).hasSize(1);

    var request = requests.get(0);
    assertThat(request.getCommand()).isEqualTo((byte) 100);
    assertThat(request.getCommandAsString()).isEqualTo("d");

    simpleServer.removeSupplier(supplier);
    simpleServer.tearDown();
  }
}

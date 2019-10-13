package io.github.henryssondaniel.teacup.protocol.telnet.client;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.henryssondaniel.teacup.protocol.telnet.Client;
import io.github.henryssondaniel.teacup.protocol.telnet.SimpleServer;
import java.io.IOException;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.function.Supplier;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleTest {
  private static final String LOCALHOST = "localhost";
  private static final int PORT = 1234;

  private final Client client = Factory.createClient("vt100");

  private Supplier<Response> responses;
  private SimpleServer simpleServer;

  @AfterEach
  void afterEach() throws IOException {
    client.disconnect();
    simpleServer.tearDown();
  }

  @BeforeEach
  void beforeEach() throws IOException {
    simpleServer =
        io.github.henryssondaniel.teacup.protocol.telnet.server.Factory.createSimpleServer(
            io.github.henryssondaniel.teacup.protocol.telnet.server.Factory.createListener(
                1, 1, PORT, Integer.MAX_VALUE));
    simpleServer.setUp();
    simpleServer.setContext(
        io.github.henryssondaniel.teacup.protocol.telnet.server.Factory.createContext(
            io.github.henryssondaniel.teacup.protocol.telnet.server.Factory.createReply("t")));

    client.setCharset(Charset.defaultCharset());
    client.setConnectTimeout(Integer.MAX_VALUE);
    client.setDefaultPort(PORT);
    client.setDefaultTimeout(Integer.MAX_VALUE);
    client.setProxy(Proxy.NO_PROXY);
    client.setReceiveBufferSize(Integer.MAX_VALUE);
    client.setSendBufferSize(Integer.MAX_VALUE);
    client.setServerSocketFactory(ServerSocketFactory.getDefault());
    client.setSocketFactory(SocketFactory.getDefault());

    responses = client.connect(LOCALHOST);

    client.setKeepAlive(false);
    client.setSoLinger(false, Integer.MAX_VALUE);
    client.setSoTimeout(Integer.MAX_VALUE);
    client.setTcpNoDelay(false);
  }

  @Test
  void send() throws IOException {
    client.send("te");

    var response = responses.get();

    assertThat(response.getData()).isEqualTo("t".getBytes(Charset.defaultCharset()));
    assertThat(response.getDataAsString()).isEqualTo("t");
  }
}

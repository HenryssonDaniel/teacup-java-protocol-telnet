package io.github.henryssondaniel.teacup.protocol.telnet.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.github.henryssondaniel.teacup.protocol.telnet.Client;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.apache.commons.net.telnet.TelnetClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleTest {
  private static final String COMMAND = "command";
  private static final String HOSTNAME = "hostname";

  private final OutputStream outputStream = mock(OutputStream.class);
  private final TelnetClient telnetClient = mock(TelnetClient.class);
  private final Client client = new Simple(telnetClient);

  @BeforeEach
  void beforeEach() {
    when(telnetClient.getOutputStream()).thenReturn(outputStream);
  }

  @Test
  void connect() throws IOException {
    assertThat(client.connect(HOSTNAME)).isExactlyInstanceOf(ResponseSupplierImpl.class);

    verifyConnect();
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void connectOnlyOnce() throws IOException {
    assertThat(client.connect(HOSTNAME)).isSameAs(client.connect(HOSTNAME, 1));

    verifyConnect();
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void connectWithPort() throws IOException {
    assertThat(client.connect(HOSTNAME, 1)).isExactlyInstanceOf(ResponseSupplierImpl.class);

    verify(telnetClient).connect(HOSTNAME, 1);
    verify(telnetClient).getInputStream();
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void disconnect() throws IOException {
    client.disconnect();
    verifyDisconnect();
  }

  @Test
  void disconnectWhenConnected() throws IOException {
    client.connect(HOSTNAME);
    client.disconnect();

    verifyConnect();
    verifyDisconnect();
  }

  @Test
  void send() throws IOException {
    client.send(COMMAND);

    verify(outputStream).write(COMMAND.getBytes(StandardCharsets.UTF_8));
    verifySend();
  }

  @Test
  void sendBytes() throws IOException {
    var bytes = COMMAND.getBytes(StandardCharsets.UTF_8);

    client.send(bytes);

    verify(outputStream).write(bytes);
    verifySend();
  }

  @Test
  void setCharset() {
    var charset = Charset.defaultCharset();

    client.setCharset(charset);

    verify(telnetClient).setCharset(charset);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setConnectTimeout() {
    client.setConnectTimeout(1);

    verify(telnetClient).setConnectTimeout(1);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setDefaultPort() {
    client.setDefaultPort(1);

    verify(telnetClient).setDefaultPort(1);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setDefaultTimeout() {
    client.setDefaultTimeout(1);

    verify(telnetClient).setDefaultTimeout(1);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setKeepAlive() throws SocketException {
    client.setKeepAlive(true);

    verify(telnetClient).setKeepAlive(true);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setProxy() {
    var proxy = mock(Proxy.class);

    client.setProxy(proxy);

    verify(telnetClient).setProxy(proxy);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setReceiveBufferSize() throws SocketException {
    client.setReceiveBufferSize(1);

    verify(telnetClient).setReceiveBufferSize(1);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setSendBufferSize() throws SocketException {
    client.setSendBufferSize(1);

    verify(telnetClient).setSendBufferSize(1);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setServerSocketFactory() {
    var serverSocketFactory = mock(ServerSocketFactory.class);

    client.setServerSocketFactory(serverSocketFactory);

    verify(telnetClient).setServerSocketFactory(serverSocketFactory);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setSoLinger() throws SocketException {
    client.setSoLinger(true, 1);

    verify(telnetClient).setSoLinger(true, 1);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setSoTimeout() throws SocketException {
    client.setSoTimeout(1);

    verify(telnetClient).setSoTimeout(1);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setSocketFactory() {
    var socketFactory = mock(SocketFactory.class);

    client.setSocketFactory(socketFactory);

    verify(telnetClient).setSocketFactory(socketFactory);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void setTcpNoDelay() throws SocketException {
    client.setTcpNoDelay(true);

    verify(telnetClient).setTcpNoDelay(true);
    verifyNoMoreInteractions(telnetClient);
  }

  private void verifyConnect() throws IOException {
    verify(telnetClient).connect(HOSTNAME, 0);
    verify(telnetClient).getDefaultPort();
    verify(telnetClient).getInputStream();
  }

  private void verifyDisconnect() throws IOException {
    verify(telnetClient).disconnect();
    verifyNoMoreInteractions(telnetClient);
  }

  private void verifySend() throws IOException {
    verify(telnetClient).getOutputStream();
    verifyNoMoreInteractions(telnetClient);

    verify(outputStream).flush();
    verifyNoMoreInteractions(outputStream);
  }
}

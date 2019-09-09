package io.github.henryssondaniel.teacup.protocol.telnet.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import io.github.henryssondaniel.teacup.protocol.telnet.Client;
import java.io.IOException;
import java.net.Proxy;
import java.net.SocketException;
import java.nio.charset.Charset;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.apache.commons.net.telnet.TelnetClient;
import org.junit.jupiter.api.Test;

class SimpleTest {
  private final TelnetClient telnetClient = mock(TelnetClient.class);
  private final Client client = new Simple(telnetClient);

  @Test
  void send() throws IOException {
    client.send(1);

    verify(telnetClient).sendCommand((byte) 1);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void sendSubNegotiation() throws IOException {
    client.sendSubNegotiation(1);
    verify(telnetClient).sendSubnegotiation(new int[] {1});
    verifyNoMoreInteractions(telnetClient);
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
  void setReaderThread() {
    client.setReaderThread(true);

    verify(telnetClient).setReaderThread(true);
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
}

package io.github.henryssondaniel.teacup.protocol.telnet.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import io.github.henryssondaniel.teacup.protocol.telnet.Client;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.SocketException;
import java.nio.charset.Charset;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.apache.commons.net.telnet.TelnetClient;
import org.junit.jupiter.api.Test;

class SimpleTest {
  private static final String HOSTNAME = "hostname";

  private final TelnetClient telnetClient = mock(TelnetClient.class);
  private final Client client = new Simple(telnetClient);

  @Test
  void connect() throws IOException {
    client.connect(HOSTNAME);

    verify(telnetClient).connect(HOSTNAME, 0);
    verify(telnetClient).getDefaultPort();
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void connectWithPort() throws IOException {
    client.connect(HOSTNAME, 1);

    verify(telnetClient).connect(HOSTNAME, 1);
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void disconnect() throws IOException {
    client.disconnect();

    verify(telnetClient).disconnect();
    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void getInputStream() throws IOException {
    var inputStream = mock(InputStream.class);

    try (var stream = telnetClient.getInputStream()) {
      when(stream).thenReturn(inputStream);
    }

    try (var stream = client.getInputStream()) {
      verifyInputStream(stream);
      assertThat(stream).isSameAs(inputStream);
    }

    verifyNoMoreInteractions(telnetClient);
  }

  @Test
  void getOutputStream() throws IOException {
    var outputStream = mock(OutputStream.class);

    try (var stream = telnetClient.getOutputStream()) {
      when(stream).thenReturn(outputStream);
    }

    try (var stream = client.getOutputStream()) {
      verifyOutputStream(stream);
      assertThat(stream).isSameAs(outputStream);
    }

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

  private void verifyInputStream(InputStream inputStream) throws IOException {
    try (var ignore = verify(telnetClient).getInputStream()) {
      verifyZeroInteractions(inputStream);
    }
  }

  private void verifyOutputStream(OutputStream outputStream) throws IOException {
    try (var ignore = verify(telnetClient).getOutputStream()) {
      verifyZeroInteractions(outputStream);
    }
  }
}

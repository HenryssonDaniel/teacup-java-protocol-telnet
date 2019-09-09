package io.github.henryssondaniel.teacup.protocol.telnet.client;

import io.github.henryssondaniel.teacup.protocol.telnet.Client;
import java.io.IOException;
import java.net.Proxy;
import java.net.SocketException;
import java.nio.charset.Charset;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.apache.commons.net.telnet.TelnetClient;

class Simple implements Client {
  private final TelnetClient telnetClient;

  Simple(TelnetClient telnetClient) {
    this.telnetClient = telnetClient;
  }

  @Override
  public void send(int command) throws IOException {
    telnetClient.sendCommand((byte) command);
  }

  @Override
  public void sendSubNegotiation(int... message) throws IOException {
    telnetClient.sendSubnegotiation(message);
  }

  @Override
  public void setCharset(Charset charset) {
    telnetClient.setCharset(charset);
  }

  @Override
  public void setConnectTimeout(int connectTimeout) {
    telnetClient.setConnectTimeout(connectTimeout);
  }

  @Override
  public void setDefaultPort(int defaultPort) {
    telnetClient.setDefaultPort(defaultPort);
  }

  @Override
  public void setDefaultTimeout(int defaultTimeout) {
    telnetClient.setDefaultTimeout(defaultTimeout);
  }

  @Override
  public void setKeepAlive(boolean keepAlive) throws SocketException {
    telnetClient.setKeepAlive(keepAlive);
  }

  @Override
  public void setProxy(Proxy proxy) {
    telnetClient.setProxy(proxy);
  }

  @Override
  public void setReaderThread(boolean readerThread) {
    telnetClient.setReaderThread(readerThread);
  }

  @Override
  public void setReceiveBufferSize(int receiveBufferSize) throws SocketException {
    telnetClient.setReceiveBufferSize(receiveBufferSize);
  }

  @Override
  public void setSendBufferSize(int sendBufferSize) throws SocketException {
    telnetClient.setSendBufferSize(sendBufferSize);
  }

  @Override
  public void setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
    telnetClient.setServerSocketFactory(serverSocketFactory);
  }

  @Override
  public void setSoLinger(boolean on, int soLinger) throws SocketException {
    telnetClient.setSoLinger(on, soLinger);
  }

  @Override
  public void setSoTimeout(int soTimeout) throws SocketException {
    telnetClient.setSoTimeout(soTimeout);
  }

  @Override
  public void setSocketFactory(SocketFactory socketFactory) {
    telnetClient.setSocketFactory(socketFactory);
  }

  @Override
  public void setTcpNoDelay(boolean tcpNoDelay) throws SocketException {
    telnetClient.setTcpNoDelay(tcpNoDelay);
  }
}

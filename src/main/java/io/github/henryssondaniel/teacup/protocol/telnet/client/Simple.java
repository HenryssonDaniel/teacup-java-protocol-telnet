package io.github.henryssondaniel.teacup.protocol.telnet.client;

import io.github.henryssondaniel.teacup.core.logging.Factory;
import io.github.henryssondaniel.teacup.protocol.telnet.Client;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.apache.commons.net.telnet.TelnetClient;

class Simple implements Client {
  private static final String CONNECT = "Connect";
  private static final Logger LOGGER = Factory.getLogger(Simple.class);

  private final TelnetClient telnetClient;

  Simple(TelnetClient telnetClient) {
    this.telnetClient = telnetClient;
  }

  @Override
  public void connect(String hostname) throws IOException {
    LOGGER.log(Level.FINE, CONNECT);
    connect(hostname, telnetClient.getDefaultPort());
  }

  @Override
  public void connect(String hostname, int port) throws IOException {
    LOGGER.log(Level.FINE, CONNECT);
    telnetClient.connect(hostname, port);
  }

  @Override
  public void disconnect() throws IOException {
    LOGGER.log(Level.FINE, "Disconnect");
    telnetClient.disconnect();
  }

  @Override
  public InputStream getInputStream() {
    LOGGER.log(Level.FINE, "Get input stream");
    return telnetClient.getInputStream();
  }

  @Override
  public OutputStream getOutputStream() {
    LOGGER.log(Level.FINE, "Get output stream");
    return telnetClient.getOutputStream();
  }

  @Override
  public void setCharset(Charset charset) {
    LOGGER.log(Level.FINE, "Set charset");
    telnetClient.setCharset(charset);
  }

  @Override
  public void setConnectTimeout(int connectTimeout) {
    LOGGER.log(Level.FINE, "Set connect timeout");
    telnetClient.setConnectTimeout(connectTimeout);
  }

  @Override
  public void setDefaultPort(int defaultPort) {
    LOGGER.log(Level.FINE, "Set default port");
    telnetClient.setDefaultPort(defaultPort);
  }

  @Override
  public void setDefaultTimeout(int defaultTimeout) {
    LOGGER.log(Level.FINE, "Set default timeout");
    telnetClient.setDefaultTimeout(defaultTimeout);
  }

  @Override
  public void setKeepAlive(boolean keepAlive) throws SocketException {
    LOGGER.log(Level.FINE, "Set keep alive");
    telnetClient.setKeepAlive(keepAlive);
  }

  @Override
  public void setProxy(Proxy proxy) {
    LOGGER.log(Level.FINE, "Set proxy");
    telnetClient.setProxy(proxy);
  }

  @Override
  public void setReceiveBufferSize(int receiveBufferSize) throws SocketException {
    LOGGER.log(Level.FINE, "Set received buffer size");
    telnetClient.setReceiveBufferSize(receiveBufferSize);
  }

  @Override
  public void setSendBufferSize(int sendBufferSize) throws SocketException {
    LOGGER.log(Level.FINE, "Set send buffer size");
    telnetClient.setSendBufferSize(sendBufferSize);
  }

  @Override
  public void setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
    LOGGER.log(Level.FINE, "Set server socket factory");
    telnetClient.setServerSocketFactory(serverSocketFactory);
  }

  @Override
  public void setSoLinger(boolean on, int soLinger) throws SocketException {
    LOGGER.log(Level.FINE, "Set so linger");
    telnetClient.setSoLinger(on, soLinger);
  }

  @Override
  public void setSoTimeout(int soTimeout) throws SocketException {
    LOGGER.log(Level.FINE, "Set so timeout");
    telnetClient.setSoTimeout(soTimeout);
  }

  @Override
  public void setSocketFactory(SocketFactory socketFactory) {
    LOGGER.log(Level.FINE, "Set socket factory");
    telnetClient.setSocketFactory(socketFactory);
  }

  @Override
  public void setTcpNoDelay(boolean tcpNoDelay) throws SocketException {
    LOGGER.log(Level.FINE, "Set TCP no delay");
    telnetClient.setTcpNoDelay(tcpNoDelay);
  }
}

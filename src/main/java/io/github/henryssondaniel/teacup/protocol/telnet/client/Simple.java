package io.github.henryssondaniel.teacup.protocol.telnet.client;

import io.github.henryssondaniel.teacup.core.logging.Factory;
import io.github.henryssondaniel.teacup.protocol.telnet.Client;
import java.io.IOException;
import java.net.Proxy;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.apache.commons.net.telnet.TelnetClient;

class Simple implements Client {
  private static final String CONNECT = "Connect";
  private static final Logger LOGGER = Factory.getLogger(Simple.class);

  private final TelnetClient telnetClient;

  private ResponseSupplier responseSupplier;

  Simple(TelnetClient telnetClient) {
    this.telnetClient = telnetClient;
  }

  @Override
  public Supplier<Response> connect(String hostname) throws IOException {
    LOGGER.log(Level.FINE, CONNECT);
    return connect(hostname, telnetClient.getDefaultPort());
  }

  @Override
  public Supplier<Response> connect(String hostname, int port) throws IOException {
    LOGGER.log(Level.FINE, CONNECT);

    if (responseSupplier == null)
      responseSupplier = createResponseSupplier(hostname, port, telnetClient);
    else
      LOGGER.log(
          Level.WARNING,
          "The client is already connected. Please disconnect before connect again.");

    return responseSupplier;
  }

  @Override
  public void disconnect() throws IOException {
    LOGGER.log(Level.FINE, "Disconnect");

    if (responseSupplier != null) {
      responseSupplier.interrupt();
      responseSupplier = null;
    }

    telnetClient.disconnect();
  }

  @Override
  public void send(String command) throws IOException {
    LOGGER.log(Level.INFO, () -> "Request: " + command);
    sendCommand(command.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public void send(byte... commands) throws IOException {
    LOGGER.log(Level.INFO, () -> "Request: " + Arrays.toString(commands));
    sendCommand(commands);
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

  private static ResponseSupplier createResponseSupplier(
      String hostname, int port, TelnetClient telnetClient) throws IOException {
    telnetClient.connect(hostname, port);

    ResponseSupplier supplier = new ResponseSupplierImpl(telnetClient.getInputStream());
    supplier.start();

    return supplier;
  }

  private void sendCommand(byte... commands) throws IOException {
    var outputStream = telnetClient.getOutputStream();
    outputStream.write(commands);
    outputStream.flush();
  }
}

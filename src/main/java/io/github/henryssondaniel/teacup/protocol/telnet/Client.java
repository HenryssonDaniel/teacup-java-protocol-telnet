package io.github.henryssondaniel.teacup.protocol.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.SocketException;
import java.nio.charset.Charset;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

/**
 * An Telnet client.
 *
 * @since 1.0
 */
public interface Client {
  /**
   * Connect.
   *
   * @param hostname the hostname
   * @throws IOException if the socket could not be opened
   * @since 1.0
   */
  void connect(String hostname) throws IOException;

  /**
   * Connect.
   *
   * @param hostname the hostname
   * @param port the port
   * @throws IOException if the socket could not be opened
   * @since 1.0
   */
  void connect(String hostname, int port) throws IOException;

  /**
   * Disconnect.
   *
   * @throws IOException if an error occurs while disconnecting
   * @since 1.0
   */
  void disconnect() throws IOException;

  /**
   * Returns the input stream. The stream will be automatically closed in the {@link #disconnect()}
   * method.
   *
   * @return the input stream
   * @since 1.0
   */
  InputStream getInputStream();

  /**
   * Returns the output stream. The stream will be automatically closed in the {@link #disconnect()}
   * method.
   *
   * @return the output stream
   * @since 1.0
   */
  OutputStream getOutputStream();

  /**
   * Sets the charset.
   *
   * @param charset the charset.
   */
  void setCharset(Charset charset);

  /**
   * Sets the connect timeout.
   *
   * @param connectTimeout the connect timeout
   * @since 1.0
   */
  void setConnectTimeout(int connectTimeout);

  /**
   * Sets the default port.
   *
   * @param defaultPort the default port
   * @since 1.0
   */
  void setDefaultPort(int defaultPort);

  /**
   * Sets the default timeout.
   *
   * @param defaultTimeout the default timeout
   * @since 1.0
   */
  void setDefaultTimeout(int defaultTimeout);

  /**
   * Sets whether to keep alive.
   *
   * @param keepAlive whether to keep alive
   * @throws SocketException if an socket error occurs
   * @since 1.0
   */
  void setKeepAlive(boolean keepAlive) throws SocketException;

  /**
   * Sets the proxy.
   *
   * @param proxy the proxy
   * @since 1.0
   */
  void setProxy(Proxy proxy);

  /**
   * Sets whether to enable the reader thread.
   *
   * @param readerThread whether to enable the reader thread
   * @since 1.0
   */
  void setReaderThread(boolean readerThread);

  /**
   * Sets the received buffer size.
   *
   * @param receiveBufferSize the received buffer size
   * @throws SocketException if an socket error occurs
   * @since 1.0
   */
  void setReceiveBufferSize(int receiveBufferSize) throws SocketException;

  /**
   * Sets the send buffer size.
   *
   * @param sendBufferSize the send buffer size
   * @throws SocketException if an socket error occurs
   * @since 1.0
   */
  void setSendBufferSize(int sendBufferSize) throws SocketException;

  /**
   * Sets the server socket factory.
   *
   * @param serverSocketFactory the server socket factory
   * @since 1.0
   */
  void setServerSocketFactory(ServerSocketFactory serverSocketFactory);

  /**
   * Sets the so linger.
   *
   * @param on whether to enable linger
   * @param soLinger the so linger
   * @throws SocketException if an socket error occurs
   * @since 1.0
   */
  void setSoLinger(boolean on, int soLinger) throws SocketException;

  /**
   * Sets the so timeout.
   *
   * @param soTimeout the so timeout
   * @throws SocketException if an socket error occurs
   * @since 1.0
   */
  void setSoTimeout(int soTimeout) throws SocketException;

  /**
   * Sets the socket factory.
   *
   * @param socketFactory the socket factory
   * @since 1.0
   */
  void setSocketFactory(SocketFactory socketFactory);

  /**
   * Sets whether to enable TCP no delay.
   *
   * @param tcpNoDelay whether to enable TCP no delay
   * @throws SocketException if an socket error occurs
   * @since 1.0
   */
  void setTcpNoDelay(boolean tcpNoDelay) throws SocketException;
}

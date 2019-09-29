package io.github.henryssondaniel.teacup.protocol.telnet.client;

interface ResponseAdder extends Response {
  void addData(String dataAsString, byte... data);
}

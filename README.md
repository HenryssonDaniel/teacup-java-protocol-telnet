# [User Guide](https://henryssondaniel.github.io/teacup.github.io/)
[![Build Status](https://travis-ci.com/HenryssonDaniel/teacup-java-protocol-telnet.svg?branch=master)](https://travis-ci.com/HenryssonDaniel/teacup-java-protocol-telnet)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=HenryssonDaniel_teacup-java-protocol-telnet&metric=coverage)](https://sonarcloud.io/dashboard?id=HenryssonDaniel_teacup-java-protocol-telnet)
[![latest release](https://img.shields.io/badge/release%20notes-1.0.0-yellow.svg)](https://github.com/HenryssonDaniel/teacup-java-protocol-telnet/blob/master/doc/release-notes/official.md)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.henryssondaniel.teacup.protocol/telnet.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22io.github.henryssondaniel.teacup.protocol%22%20AND%20a%3A%22telnet%22)
[![Javadocs](https://www.javadoc.io/badge/io.github.henryssondaniel.teacup.protocol/telnet.svg)](https://www.javadoc.io/doc/io.github.henryssondaniel.teacup.protocol/telnet)
## What ##
Telnet support.  

There is also support for an Telnet server.
## Why ##
This makes it possible to test Telnet with the framework, both the client and the server.
## How ##
Add this repository as a dependency.  

The Client interface holds all the functionality that the Telnet client can do.  
New clients can be created with the Factory class in the client package.

The Simple interface holds all the functionality that the Telnet server can do.  
New servers can be created with the Factory class in the server package.
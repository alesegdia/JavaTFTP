package com.JTFTP;

import java.net.*;

public class TID {
    private InetAddress host;
    private int port;

    public TID (InetAddress host, int port) {
	this.host = host;
	this.port = port;
    }

    public InetAddress getInetAddress() {
	return host;
    }

    public int getPort() {
	return port;
    }
}

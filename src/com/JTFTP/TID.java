package com.JTFTP;

import java.net.*;

/**
 *
 */
public class TID {
	private InetAddress host;
	private int port;

	/**
	 *
	 * @param host
	 * @param port
	 */
	public TID(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 *
	 * @return 
	 */
	public InetAddress getInetAddress() {
		return host;
	}

	/**
	 *
	 * @return 
	 */
	public int getPort() {
		return port;
	}
}

package com.JTFTP;

import java.net.*;

/**
 * This class represents a transfer identifier of TFTP protocol (see rfc 1350).
 */
public class TID {
	private InetAddress host;
	private int port;

	/**
	 * Construct a transfer identifier.
	 * @param host is an address.
	 * @param port is a port number.
	 */
	public TID(InetAddress host, int port) {
		this.host = host;
		//see that 0 <= port <= 65535
		this.port = port;
	}

	/**
	 * Get the address.
	 * @return the InetAddress.
	 */
	public InetAddress getInetAddress() {
		return host;
	}

	/**
	 * Get the port.
	 * @return the port number.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Says if id is the same transfer identifier that this.
	 * @param id is a transfer identifier.
	 * @return if id is the same transfer identifier that this.
	 */
	public boolean equals(TID id) {
		return host.equals(id.getInetAddress()) && port == id.getPort();
	}
}

package com.JTFTP;

import java.net.*;

/**
 * This class represents a connection of protocol TFTP (see rfc 1350). See
 * that this class don't do the connection.
 */
public class Connection {
	private final static boolean OCTET = true;
	public final static boolean READ = true;
	public final static boolean WRITE = false;

	private TID currTID;
	private boolean rw;
	private String fileName;
	private String mode;

	/**
	 * Constructs a new representation of a connection of TFTP protocol.
	 * @param currTID is the transfer identifier used by TFTP.
	 * @param rw indicates if the connection is to read (true) or write (false).
	 * @param filename is the name of file to be transfered or received.
	 * @param mode is one of the possibles modes specified in rfc 1350 (TFTP). That
	 * are "netascci" (true) or "octet" (false). See that "mail" is obsolete
	 */
	public Connection (TID currTID, boolean rw, String fileName, String mode) {
		this.currTID = currTID;
		this.rw = rw;
		this.fileName = fileName;
		this.mode = mode;
	}

	/**
	 * Get the address of the transfer identifier of this connection.
	 * @return an InetAddress.
	 */
	public InetAddress getInetAddress() {
		return currTID.getInetAddress();
	}

	/**
	 * Get the port used of transfer identifier of this connection.
	 * @return the port.
	 */
	public int getPort() {
		return currTID.getPort();
	}

	/**
	 * Get the transfer identifier of this connection.
	 * @return the TID.
	 */
	public TID getTID() {
		return currTID;
	}

	/**
	 * Tells if this connection is to read (true) or write (false).
	 * @return true (read) or false (write).
	 */
	public boolean getRw() {
		return rw;
	}

	/**
	 * Get the name of the file to be transfered or received.
	 * @return the name of a file.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Get the mode of connection.
	 * @return true ("netascii") or flase ("octet").
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * Verifies is id is the transfer identifier used by this connection.
	 * @param id is the transfer identifier to comprove.
	 * @return true is id is the transfer identifier used by this connection.
	 */
	public boolean correctTID(TID id) {
		return currTID.equals(id);
	}
}

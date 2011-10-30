package com.JTFTP;

import java.net.*;

/**
 *
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
	 *
	 * @param currTID
	 * @param rw
	 * @param filename
	 * @param mode
	 */
	public Connection (TID currTID, boolean rw, String fileName, String mode) {
		this.currTID = currTID;
		this.rw = rw;
		this.fileName = fileName;
		this.mode = mode;
	}

	/**
	 *
	 * @return 
	 */
	public InetAddress getInetAddress () {
		return currTID.getInetAddress();
	}

	/**
	 *
	 * @return 
	 */
	public int getPort () {
		return currTID.getPort();
	}

	/**
	 *
	 * @return 
	 */
	public boolean getRw () {
		return rw;
	}

	/**
	 *
	 * @return 
	 */
	public String getFileName () {
		return fileName;
	}

	/**
	 *
	 * @return 
	 */
	public String getMode () {
		return mode;
	}
}

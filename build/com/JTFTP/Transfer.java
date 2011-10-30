package com.JTFTP;

import java.net.*;

/*
  Maybe change clientConnection by remoteConnection.
  Do something to use the same methods for client and server.
  Make a method for sending files, almost the same that in Server class.
  Separate send the first piece as ACK and send the entire file.
  Maybe overload the constructor, or make a switch if it's client or server.
  Maybe make this a base class and make two inherited classes for client and server.
*/

/**
 * This class is the responsable of send/receive a file.
 */
public class Transfer implements Runnable {
	private Thread t;
	private DatagramSocket socket;
	private Connection clientConnection;

	/**
	 * Construct a new transfer connection.
	 * @param socket is the socket used to transfer the file specified in clientConnection.
	 * @param clientConnection is the representation of connection used to transfer data.
	 */
	public Transfer(DatagramSocket socket, Connection clientConnection) {
		this.socket = socket;
		this.clientConnection = clientConnection;
	}

	/**
	 * Send/receive a file.
	 */
	public void run() {

		while(true) {
			// Some code here
			if(clientConnection.getRw() == true) {

				// Send first piece of code
			}
		}
	}
}

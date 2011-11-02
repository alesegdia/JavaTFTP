package com.JTFTP;

import java.net.*;
import java.io.*;

/**
 * This class wait new connections of TFTP clients and response it petitions.
 */
public class Server {
	private DatagramSocket datagram = null;
	private static final int BUFFER_SIZE = 512;
	public static final int DEFAULT_PORT = 69;

	/**	
	 * Construct a new server that wait connections at default port number.
	 * @throws SocketException if an error ocurred during the creation of socket.
	 */
	public Server() throws SocketException {
		this(DEFAULT_PORT);
	}
	
	/**	
	 * Construct a new server that wait connections at port number port.
	 * @param port is the number of port at which server wait the new connections.
	 * @throws SocketException if an error ocurred during the creation of socket.
	 */
	public Server(int port) throws SocketException {
		datagram = new DatagramSocket(port);
	}

	/**
	 * Wait for new connections of TFTP clients.
	 * @return a representation of the connection with new client.
	 * @throws IOException if an error ocurred while waiting.
	 * @throws SocketTimeException if time limit has expired.
	 */
	public Connection accept() throws IOException, SocketTimeoutException {
		byte[] tmpBuffer = new byte[BUFFER_SIZE];
		Buffer dataBuffer;

		DatagramPacket dataPacket = new DatagramPacket(tmpBuffer, BUFFER_SIZE);

		try {
			datagram.receive(dataPacket);
		} catch (SocketTimeoutException ex) {
			System.out.println("TIMEOUT!");
		}

		dataBuffer = new Buffer(dataPacket.getData());

		int opcode = dataBuffer.getShort();
		String filename = dataBuffer.getString();
		String mode = dataBuffer.getString();

		System.out.println("opcode: " + opcode);
		System.out.println("filename: " + filename);
		System.out.println("mode: \"" + mode + "\"");

		if(opcode == 1 || opcode == 2) {

			boolean rw;

			if(opcode == 1) {
				rw = Connection.READ;
			} else {
				rw = Connection.WRITE;
			}

			Connection myConn = new Connection (new TID(dataPacket.getAddress(), dataPacket.getPort()), 
				rw, filename, mode);
			return myConn;
		}
		// No valid connection, throw exception
		return null;
	}
}
	

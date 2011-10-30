package com.JTFTP;

import java.net.*;
import java.io.*;

/**
 *
 */
public class Server {
	private DatagramSocket datagram = null;
	private static final int BUFFER_SIZE = 512;
	private static final int PORT = 69;

	/**
	 *
	 * @param port
	 * @throws SocketException
	 */
	public Server (int port) throws SocketException {
		datagram = new DatagramSocket(port);
	}

	/**
	 *
	 * @param data
	 * @param myConn
	 * @throws IOException
	 */
	private void sendPacket(byte[] data, Connection myConn) throws IOException {
		DatagramPacket dataPacket = new DatagramPacket(data, data.length,
			myConn.getInetAddress(), myConn.getPort());

		datagram.send(dataPacket);
	}

	/*private void rcvPacket() throws IOException {
		byte[] tmpBuffer = new byte[BUFFER_SIZE];
		int opcode;

		DatagramPacket dataPacket = new DatagramPacket(tmpBuffer, BUFFER_SIZE);
		datagram.receive(dataPacket);
		Buffer dataBuffer = new Buffer(BUFFER_SIZE);
		dataBuffer.setBuffer(tmpBuffer);
		opcode = dataBuffer.getShort();
	}*/

	/**
	 *
	 * @return
	 * @throws IOException
	 * @throws SocketTimeException
	 */
	private Connection accept() throws IOException, SocketTimeoutException {
		byte[] tmpBuffer = new byte[BUFFER_SIZE];
		Buffer dataBuffer;

		DatagramPacket dataPacket = new DatagramPacket(tmpBuffer, BUFFER_SIZE);

		try {
			datagram.receive(dataPacket);
		} catch (SocketTimeoutException ex) {
			System.out.println("TIMEOUT!");
		}

		dataBuffer = new Buffer(dataPacket.getData());

		short opcode = dataBuffer.getShort();
		String filename = dataBuffer.getString();
		String mode = dataBuffer.getString();

		System.out.println("opcode: " + opcode);
		System.out.println("filename: " + filename);
		System.out.println("mode: \"" + mode + "\"");

		if(opcode == (short)1 || opcode == (short)2) {
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

	/*
	 * This function will be erased soon, and this functionality will be traspassed to MainClass and other
	 * parts of this class.
	 */
	public static void main(String args[]) throws SocketException, IOException {
		try {
			Server myServer = new Server(50000);

			Connection currConn;
			while(true) {

				currConn = myServer.accept();

				if(currConn != null) {
					Transfer newTransfer = new Transfer (new DatagramSocket (0), currConn);

					Thread newThread = new Thread(newTransfer);
					newThread.start();
				}
			}
		} catch(BindException ex) {
			System.err.println("Couldn't connect to " + PORT + " port.");
		}
	}
}
	

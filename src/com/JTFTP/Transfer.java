package com.JTFTP;

import java.net.*;
import java.io.*;

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
	private static final int BUFFER_SIZE = 512;

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
	 * Try to send a packet to the client specified by clientConnection.
	 * @param data is the packet to send.
	 * @param length is the length of data to send.
	 * @throws IOException if an error ocurred while packet is sent.
	 */
	public void sendPacket(byte[] data, int length) throws IOException {
		DatagramPacket dataPacket = new DatagramPacket(data, length,
			clientConnection.getInetAddress(), clientConnection.getPort());

		socket.send(dataPacket);
	}

	/**
	 * Receive a new packet send by client.
	 * @return a buffer with the data send by client.
	 * @throws IOException if an error ocurred while waiting packet receive.
	 */
	private Buffer receivePacket() throws IOException {
		byte[] tmpBuffer = new byte[BUFFER_SIZE];

		DatagramPacket dataPacket = new DatagramPacket(tmpBuffer, BUFFER_SIZE);
		socket.receive(dataPacket);
		Buffer dataBuffer = new Buffer(BUFFER_SIZE);
		dataBuffer.setBuffer(tmpBuffer);
		return dataBuffer;
		
	}

	/**
	 * Send/receive a file.
	 */
	public void run() {

		if(clientConnection.getRw() == true) {
			FileBlocksReader reader = null;
			try {
				reader = new FileBlocksReader(clientConnection.getFileName(), 512);
				Buffer buffer;
				while(reader.hasNext()) {
					buffer = new Buffer(516);
					buffer.addShort(3); //add opcode DATA
					buffer.addShort(reader.nextIndex()+1); //add block number
					int length = reader.read(buffer.dumpBuffer(), 4); //add data
					sendPacket(buffer.dumpBuffer(), length+4);

					buffer = receivePacket();
					int opcode = buffer.getShort();
					switch(opcode) {
						case 4: //ACK
							int blockNumber = buffer.getShort();
							if(blockNumber == reader.nextIndex()) {
								System.out.println("ACK: " +blockNumber);
							}else {
								System.out.println("unexpected number of ack, expected "+ 
									reader.nextIndex() +" but received "+ blockNumber);
							}
							break;
					}
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					if(reader != null) {
						reader.close();
					}
				}catch(IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
}

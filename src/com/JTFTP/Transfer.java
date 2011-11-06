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
	private static final int BUFFER_SIZE = 1024;
	private static final int DEFAULT_TIMEOUT = 5000;

	/**
	 * Construct a new transfer connection.
	 * @param socket is the socket used to transfer the file specified in clientConnection.
	 * @param clientConnection is the representation of connection used to transfer data.
	 */
	public Transfer(DatagramSocket socket, Connection clientConnection) {
		this.socket = socket;
		socket.setSoTimeout(DEFAULT_TIMEOUT);
		this.clientConnection = clientConnection;
	}

	/**
	 * Send a packet to the client specified by clientConnection.
	 * @param data is the packet to send.
	 * @param length is the length of data to send.
	 * @throws IOException if an error ocurred while packet is sent.
	 */
	private void sendPacket(byte[] data, int length) throws IOException {
		sendPacket(data, length, clientConnection.getTID());
	}

	/**
	 * Send a packet to the client specified by address and port.
	 * @param data is the packet to send.
	 * @param length is the length of data to send.
	 * @param id is the transfer identifier of the receiver.
	 * @throws IOException if an error ocurred while packet is sent.
	 */
	private void sendPacket(byte[] data, int length, TID id) throws IOException {
		DatagramPacket dataPacket = new DatagramPacket(data, length, 
			id.getInetAddress(), id.getPort());
		socket.send(dataPacket);
	}

	/**
	 * Receive a new packet send by client.
	 * @return a buffer with the data send by client.
	 * @throws IOException if an error ocurred while waiting packet receive.
	 */
	private Buffer receivePacket() throws IOException {
		byte[] tmpBuffer = new byte[BUFFER_SIZE];
		int length = 0;
		do {
			DatagramPacket dataPacket = new DatagramPacket(tmpBuffer, BUFFER_SIZE);
			socket.receive(dataPacket);
			length = dataPacket.getLength();
			TID id = new TID(dataPacket.getAddress(), dataPacket.getPort());
			if(!clientConnection.correctTID(id)) {
				sendError(5, "Unknown transfer ID.", id); //Error code and message specified in rfc 1350.
				//Fix: avoid infinite loop.
			}else {
				break;
			}
		} while(true);
		Buffer dataBuffer = new Buffer(tmpBuffer);
		dataBuffer.setLength(length);
		return dataBuffer;
		
	}

	/**
	 * Try to send an error to the client specified by clientConnection.
	 * @param code is the error code.
	 * @param message is the error message.
	 * @throws IOException if there are any problem while trying to send error.
	 */
	private void sendError(int code, String message) throws IOException { //ignore this exception?
		sendError(code, message, clientConnection.getTID());
	}

	/**
	 * Try to send an error to the client specified by id.
	 * @param code is the error code.
	 * @param message is the error message.
	 * @param id is the transfer identifier of the receiver.
	 * @throws IOException if there are any problem while trying to send error.
	 */
	private void sendError(int code, String message, TID id) throws IOException { //ignore this exception?
		int length = Buffer.length(message) + 4;
		Buffer buffer = new Buffer(length);
		buffer.addShort(5); //add opcode ERROR
		buffer.addShort(code);
		buffer.addString(message);
		sendPacket(buffer.dumpBuffer(), length, id);
	}

	/**
	 * Return a buffer with the content of the next block (including opcode and index)
	 * @param reader is the object that reads the blocks from a file.
	 * @return the next block ready for send.
	 * @throws IOException if an error ocurred while reading.
	 */
	private Buffer nextBlock(FileBlocksReader reader) throws IOException {
		Buffer buffer = new Buffer(516);
		buffer.addShort(3); //add opcode DATA
		buffer.addShort(reader.nextIndex()+1); //add block number
		byte b[] = new byte[512];
		int length = reader.read(b, 0); 
		buffer.addBlock(b, length);//add data
		return buffer;
	}

	/**
	 * Wait the next ack.
	 * @param expected is the index of the next ack expected.
	 * @return boolean if the ack expected is received and false in another case.
	 * @throws IOException if an error ocurred while waiting.
	 */
	private boolean receiveAck(int expected) throws IOException {
		Buffer buffer = receivePacket();
		int opcode = buffer.getShort();
		if(opcode == 4) {
			int blockNumber = buffer.getShort();
			if(blockNumber == expected) {
				System.out.println("ACK: " +blockNumber);
				return true;
			}
			if(blockNumber == expected - 1) {
				System.out.println("the latest has not reached"); //send the last block dispatched.
			} else {
				System.out.println("unexpected number of ack, expected "+ 
					expected +" but received "+ blockNumber);
				//exit, throw an error, continue?
			}
		}else if(opcode >= 1 && opcode <= 5) {
			sendError(0, "Expected ACK but received "+opcode); //In this case error code is 0 or 4?
			//exit, throw an error, continue?
		}else {
			sendError(4, "Illegal TFTP operation.");
			//exit, throw an error, continue?
		}
		return false;
	}

	/**
	 * Send a file to a transfer id, all of this specified in clientConnection.
	 */
	private void sendFile() {
		FileBlocksReader reader = null;
		try {
			reader = new FileBlocksReader(clientConnection.getFileName(), 512);
			while(reader.hasNext()) {
				Buffer buffer = nextBlock(reader);
				do {
					sendPacket(buffer.dumpBuffer(), buffer.getOffset());
				} while(!receiveAck(reader.nextIndex())); //Fix: avoid infinite loop.
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

	/**
	 * Receive the next block of data.
	 * @param expected is the number of the next block of data expected.
	 * @throws IOException if an error occurs.
	 */
	private byte[] receiveData(int expected) throws IOException {
		Buffer buffer = receivePacket();
		int opcode = buffer.getShort();
		if(opcode == 3) {
			int blockNumber = buffer.getShort();
			if(blockNumber == expected) {
				System.out.println("block number: " +blockNumber);
				return buffer.getBlock(buffer.getLength() - buffer.getOffset()); //buffer.getLength() - buffer.getOffset() can be bigger than 512?
			}
			if(blockNumber == expected - 1) {
				System.out.println("the latest has not reached"); //send the last block dispatched.
			} else {
				System.out.println("unexpected block number, expected "+ 
					expected +" but received "+ blockNumber);
				//exit, throw an error, continue?
			}
		}else if(opcode >= 1 && opcode <= 5) {
			sendError(0, "Expected DATA but received "+opcode); //In this case error code is 0 or 4?
			//exit, throw an error, continue?
		}else {
			sendError(4, "Illegal TFTP operation.");
			//exit, throw an error, continue?
		}
		return null;
		
	}

	/**
	 * Send the ack number.
	 * @throws IOException if an error occurs.
	 */
	private void sendAck(int ack) throws IOException {
		Buffer buffer = new Buffer(4);
		buffer.addShort(4);
		buffer.addShort(ack);
		sendPacket(buffer.dumpBuffer(), 4);
	}

	/**
	 * Receive a file from a transfer id, all of this specified in clientConnection.
	 */
	private void receiveFile() {
		FileBlocksWriter writer = null;
		try {
			writer = new FileBlocksWriter(clientConnection.getFileName(), true, 512); //overwrite file if exists
			sendAck(0);
			while(writer.hasNext()) {
				int blockNumberExpected = writer.nextIndex()+1;
				byte[] b;
				do {
					b = receiveData(blockNumberExpected);
					sendAck(blockNumberExpected);
				} while(b == null);
				writer.write(b);
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if(writer != null) {
					writer.close();
				}
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Send/receive a file.
	 */
	public void run() { //change exceptions for RuntimeException or Error to can throw them.

		if(clientConnection.getRw() == true) {
			sendFile();
		}else {
			receiveFile();
		}
	}
}

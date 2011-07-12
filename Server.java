import java.net.*;
import java.io.*;
import com.JTFTP.*;

public class Server {
    private DatagramSocket datagram = null;
    private int clientHost = null;
    private int clientPort = null;
    private final int BUFFER_SIZE = 512;
    Buffer dataBuffer;

    public Server (int port) throws SocketException {
	dataBuffer = new Buffer(BUFFER_SIZE);
	datagram = new DatagramSocket(port);
    }

    private void sendPacket(byte[] data) throws IOException {
	DatagramPacket dataPacket = new DatagramPacket(data, data.length,
						       clientHost, clientPort);
	
	datagram.send(dataPacket);
    }

    private void rcvPacket() throws IOException {
	byte[] tmpBuffer = new byte[BUFFER_SIZE];
	int opcode;

	DatagramPacket dataPacket = new DatagramPacket(tmpBuffer, BUFFER_SIZE);
	datagram.receive(dataPacket);
	
	dataBuffer.setBuffer(tmpBuffer);
	opcode = dataBuffer.getShort();

	switch(opcode) {
	case 1:
	    // etc.
	    break;
	default:
	    break;
	}
    }

    private Connection accept() {
	byte[] tmpBuffer = new byte[BUFFER_SIZE];
	Buffer dataBuffer;

	DatagramPacket dataPacket = new DatagramPacket(tmpBuffer, BUFFER_SIZE);
	datagram.receive(dataPacket);

	dataBuffer = new Buffer(dataPacket.getData().length);
	dataBuffer.setBuffer(dataPacket.getData());

	short opcode = dataBuffer.getShort();
	String filename = dataBuffer.getString();
	String mode = dataBuffer.getString();

	if(opcode == (short)1 || opcode == (short)2) {
	    return new Connection (new TID(dataPacket.getAddress(), dataPacket.getPort()), 
				   filename, mode);
	} else {
	    // No valid connection, throw exception
	    return null;
	}
    }

}
	

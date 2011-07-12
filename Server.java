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

    public void sendPacket(byte[] data) throws IOException {
	DatagramPacket dataPacket = new DatagramPacket(data, data.length,
						       clientHost, clientPort);
	
	datagram.send(dataPacket);
    }

    public void rcvPacket() throws IOException {
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
}
	

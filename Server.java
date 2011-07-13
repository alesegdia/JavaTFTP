import java.net.*;
import java.io.*;
import com.JTFTP.*;

public class Server {
    private DatagramSocket datagram = null;
    private int clientHost;
    private int clientPort;
    private static final int BUFFER_SIZE = 512;
    private static final int PORT = 69;

    public Server (int port) throws SocketException {
	datagram = new DatagramSocket(port);
    }

    // private void sendPacket(byte[] data) throws IOException {
    // 	DatagramPacket dataPacket = new DatagramPacket(data, data.length,
    // 						       clientHost, clientPort);
	
    // 	datagram.send(dataPacket);
    // }

    private void rcvPacket() throws IOException {
	byte[] tmpBuffer = new byte[BUFFER_SIZE];
	int opcode;

	DatagramPacket dataPacket = new DatagramPacket(tmpBuffer, BUFFER_SIZE);
	datagram.receive(dataPacket);
	Buffer dataBuffer = new Buffer(BUFFER_SIZE);
	dataBuffer.setBuffer(tmpBuffer);
	opcode = dataBuffer.getShort();
    }

    private Connection accept() throws IOException {
	byte[] tmpBuffer = new byte[BUFFER_SIZE];
	Buffer dataBuffer;

	System.out.println("KABOOM");

	DatagramPacket dataPacket = new DatagramPacket(tmpBuffer, BUFFER_SIZE);

	System.out.println("asdfg");

	datagram.receive(dataPacket);

	System.out.println("BLEHG");

	dataBuffer = new Buffer(dataPacket.getData());

	System.out.println("HUH");

	short opcode = dataBuffer.getShort();
	String filename = dataBuffer.getString();
	String mode = dataBuffer.getString();

	System.out.println("opcode: " + opcode);
	System.out.println("filename: " + filename);
	System.out.println("mode" + mode);

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
	} else {
	    // No valid connection, throw exception
	    return null;
	}
    }
    
    public static void main (String args[]) throws SocketException, IOException {
	try {
	    Server myServer = new Server(50000);
	    System.out.println("SAAA");
	    Connection currConn;
	    while(true) {
		System.out.println("WEEE");
		currConn = myServer.accept();
		System.out.println("SADAS");
		if(currConn != null) {
		    System.out.println("BLEH!!");
		}
	    }
	} catch (BindException ex) {
	    System.err.println("Couldn't connect to " + PORT + " port.");
	}
    }
}
	

package com.JTFTP;

import java.net.*;

/*
 * The parsing method used by this class is only temporal. To improve see
 * http://stackoverflow.com/questions/367706/is-there-a-good-command-line-argument-parser-for-java
 */

/**
 * This class is the main class of the project. The aim of it is receive the user 
 * arguments, process them and init the server or client of TFTP.
 */
public class MainClass {

	/**
	 * This is the start function of the project. It receives the arguments and
	 * inits a server or client of TFTP depending on it.
	 */
	public static void main(String args[]) {
		if(args.length == 0) {
			System.err.println("This application needs some arguments, use -h to see them");
			System.exit(-1);
		}

		if(args[0].equalsIgnoreCase("-h")) {
			showHelp();
		}else if(args[0].equalsIgnoreCase("-s")) {
			try {
				createServer(args);
			}catch(Exception e) {
				System.err.println(e.getMessage());
			}
		}else if(args[0].equalsIgnoreCase("-c")) {
			try {
				createClient(args);
			}catch(Exception e) {
				System.err.println(e.getMessage());
			}
		}else {
			System.err.println("The first argument of this application must be: -h, -s or -c");
			System.exit(-2);
		}
	}

	/**
	 * Show the command line arguments used by this applications.
	 */
	public static void showHelp() {
		System.out.println("JTFTP needs, in the order specified, the following arguments:");
		System.out.println("-h|-s -p port_number|-c -h host -p port_number (-r|-w) -f filename");
	}

	public static int getPort(String args[], int offset) throws Exception {
		if(!args[offset].equalsIgnoreCase("-p")) {
			throw new Exception("Expected -p but received "+ args[offset]);
		}
		offset++;
		int port = 0;
		try {
			port = Integer.parseInt(args[offset]);
		}catch(NumberFormatException e) {
			throw new Exception("Expected a port number but received "+args[offset]);
		}
		return port;
	}

	public static void createServer(String args[]) throws Exception {
		//parse arguments
		if(args.length != 3) {
			throw new Exception("Incorrect number of arguments for server, use -h to see the correct format.");
		}
		int port = getPort(args, 1);

		//SocketException, IOException have to be catch
		try {
			Server myServer = new Server(port);

			while(true) {
				Connection newConnection = myServer.accept();
				if(newConnection != null) {
					Transfer newTransfer = new Transfer(new DatagramSocket(0), newConnection);
					Thread newThread = new Thread(newTransfer);
					newThread.start();
				}
			}
		} catch(BindException ex) {
			throw new Exception("Couldn't connect to " + port + " port.");
		}
		
	}

	public static void createClient(String args[]) throws Exception {
		throw new RuntimeException("Transfer is not prepared to be used as client.");
		//parse arguments
		if(args.length != 8) {
			throw new RuntimeException("Incorrect number of arguments for server, use -h to see the correct format.");
		}
		if(!args[1].equalsIgnoreCase("-h")) {
			throw new RuntimeException("Expected -h but received "+ args[1]);
		}
		InetAddress server = InetAddress.getByName(args[0]);

		int port = getPort(args, 3);

		if(!(args[5].equalsIgnoreCase("-r") || args[5].equalsIgnoreCase("-w"))) {
			throw new RuntimeException("Expected -r or -w but received "+ args[5]);
		}
		boolean read = args[5].equalsIgnoreCase("-r");
		
		if(!args[6].equalsIgnoreCase("-f")) {
			throw new RuntimeException("Expected -f but received "+ args[1]);
		}

		String filename = args[7];

		Connection connection = new Connection(new TID(server, port), read, filename, "octet");
		Transfer transfer = new Transfer(new DatagramSocket(0), connection);
		Thread thread = new Thread(transfer);
		thread.start();
	}
}

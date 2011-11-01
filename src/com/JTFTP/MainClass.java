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
		System.out.println("-h|-s -p port_number|-c");
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
		throw new Exception("I do nothing");
	}
}

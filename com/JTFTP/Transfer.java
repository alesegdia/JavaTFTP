package com.JTFTP;

import java.lang.*;
import java.net.*;
import com.JTFTP.*;

/*
  Maybe change clientConnection by remoteConnection.
  Do something to use the same methods for client and server.
  Make a method for sending files, almost the same that in Server class.
  Separate send the first piece as ACK and send the entire file.
  Maybe overload the constructor, or make a switch if it's client or server.
  Maybe make this a base class and make two inherited classes for client and server.
*/

public class Transfer implements Runnable {
    private Thread t;
    private DatagramSocket datagram = null;
    private Connection clientConnection;

    public Transfer (DatagramSocket datagram, Connection clientConnection) {
	this.datagram = datagram;
	this.clientConnection = clientConnection;
    }

    public void run () {
	while(true) {
	    // Some code here
	    if(clientConnection.getRw() == true) {

		// Send first piece of code
	    }
	}
    }
}

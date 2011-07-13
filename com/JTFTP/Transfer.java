package com.JTFTP;

import java.lang.*;
import java.net.*;

public class Transfer implements Runnable {
    private Thread t;
    private DatagramSocket datagram = null;

    public Transfer (DatagramSocket datagram) {
	this.datagram = datagram;
	t = new Thread (this);
	t.start();
    }

    public void run () {
	while(true) {
	    // Some code here
	}
    }
}

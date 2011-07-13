package com.JTFTP;

public class Connection {
    private final static boolean OCTET = true;
    public final static boolean READ = true;
    public final static boolean WRITE = false;

    private TID currTID;
    private boolean rw;
    private String fileName;
    private String mode;

    public Connection (TID currTID, boolean rw, String fileName, String mode) {
	this.currTID = currTID;
	this.rw = rw;
	this.fileName = fileName;
	this.mode = mode;
    }
}

package com.JTFTP;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Buffer {

    private byte buffer[];
    private short offset;

    public Buffer (int length) {
	buffer = new byte[length];
	reset();
    }
    
    public void reset () {
	Arrays.fill(buffer, (byte)0);
	offset = 0;
    }

    public void addString (String data) throws UnsupportedEncodingException {
	boolean status;
	byte[] tmpByteArray;

	tmpByteArray = data.getBytes("US-ASCII");

	System.arraycopy(tmpByteArray, 0, buffer, offset, tmpByteArray.length);

	offset += tmpByteArray.length;
	buffer[offset] = 0;

	offset++;
    }

    public String getString () throws UnsupportedEncodingException {
	byte[] tmpByteArray = new byte[512];
	int i = 0;

	while(buffer[offset] != 0) {
	    tmpByteArray[i] = buffer[offset];

	    i++;
	    offset++;
	}

	return new String(tmpByteArray, "US-ASCII");
    }

    public boolean addShort (short data) {
	boolean status;

	if(offset + 2 > buffer.length) {
	    status = false;
	} else {
	    buffer[offset] = (byte) ((data & 0xFF00) >> 8);
	    offset++;
	    buffer[offset] = (byte) (data & 0x00FF);
	    offset++;

	    status = true;
	}

	return status;
    }

    public short getShort () {
	byte b1 = buffer[offset++];
	byte b0 = buffer[offset++];
	
	short sb1 = (short) b1;
	short sb0 = (short) b0;

	return (short) (((sb1 << 8)) | sb0); 
    }

    public byte[] dumpBuffer () {
	return buffer;
    }

    public void setBuffer (byte[] tempBuff) {
	this.buffer = tempBuff;
    }

    public void printBuffer (int howMuch, boolean toString) {
	if(howMuch <= buffer.length) {
	    for (int i = 0; i < howMuch; i++) {
		if(toString) {
		    System.out.print(String.valueOf(buffer[i]) + ", ");
		} else {
		    System.out.print(buffer[i] + ", ");
		}
	    }
	} else {
	    System.out.println("Overflow!!");
	}
    }

    public void copyBuffer (Buffer buff) {
	buffer = buff.dumpBuffer();
    }
}

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
	Arrays.fill(buffer, (byte) 0);
	offset = 0;
    }

    public boolean addString (String data) {
	boolean status;
	byte[] tmpByteArray;

	try {
	    tmpByteArray = data.getBytes("US-ASCII");
	} catch (UnsupportedEncodingException ex) {
	    tmpByteArray = null;
	    System.err.println("Unsupported encoding.");
	}

	if(data.length() + offset > buffer.length) {
	    System.err.println("Buffer overflow because a string.");
	    status = false;
	} else {
	    // Encoding is US-ASCII but US-ASCII extended would be
	    // a bit more compatible and standard. Check this!

	    for(int i = 0; i < tmpByteArray.length; i++) {
		buffer[offset] = tmpByteArray[i];
		offset++;
	    }

	    buffer[offset] = 0;
	    offset++;

	    status = true;
	}
	return status;
    }

    public String getString () {
	byte[] tmpByteArray = new byte[512];
	int i = 0;

	while(buffer[offset] != 0) {
	    tmpByteArray[i] = buffer[offset];

	    i++;
	    offset++;
	}

	return tmpByteArray.toString();
    }

    public boolean addShort (short data) {
	boolean status;

	if(offset + 2 > buffer.length) {
	    status = false;
	} else {
	    buffer[offset++] = (byte) ((data & 0xFF00) >> 8);
	    buffer[offset++] = (byte) (data & 0x00FF);

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

}

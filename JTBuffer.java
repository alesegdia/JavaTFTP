import java.util.Arrays;

public class JTBuffer {

    private byte buffer[];
    private short offset;

    public JTBuffer (int length) {
	buffer = new byte[length];
	reset();
    }
    
    public reset () {
	Arrays.fill(buffer, 0);
	offset = 0;
    }

    public boolean addString (String data) {
	byte[] tmpByteArray = data.getBytes("US-ASCII");

	if(data.length() + offset > buffer.length()) {
	    System.err.println("Buffer overflow because a string.");
	    return false;
	} else {
	    // Encoding is US-ASCII but US-ASCII extended would be
	    // a bit more compatible and standard. Check this!
	    byte[] tmpByteArray = data.getBytes("US-ASCII");

	    for(int i = 0; i < tmpByteArray.length(); i++) {
		buffer[offset] = tmpByteArray[i];
		offset++;
	    }

	    return true;
	}
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
    }

    public short getShort () {
    }

    public byte[] dumpBuffer () {
	return buffer;
    }

    public setBuffer (byte[] tempBuff) {
	this.buffer = tempBuff;
    }

}

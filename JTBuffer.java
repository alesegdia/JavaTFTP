import java.util.Arrays;

public class JTBuffer {

    private byte buffer[];
    private short offset;

    public JTBuffer (int length) {
	buffer = new byte[length];
	offset = 0;
    }
    
    public reset () {
	Arrays.fill(buffer, 0);
	offset = 0;
    }

    public addString (String data) {
    }

    public String getString () {
    }

    public addShort (short data) {
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

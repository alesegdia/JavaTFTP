import com.JTFTP.Buffer;

public class BufferTest {
    public static void main (String args[]) {
	Buffer buff = new Buffer(512);

	//buff.addString("Fuck the world");
	//buff.printBuffer(20, false);
	//System.out.println("");
	buff.addShort((short)257);
	buff.printBuffer(20, false);
	System.out.println("");
    }
}

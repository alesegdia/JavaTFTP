import com.JTFTP.Buffer;
import java.io.*;

public class BufferTest {
    public static void main (String args[]) throws UnsupportedEncodingException {
	Buffer buff = new Buffer(512);

	buff.addString("oh yes, this works");
	buff.printBuffer(25, false);
	System.out.println();
	buff.addShort((short)257);
	buff.printBuffer(25, false);
	buff.addString("oh fuck!");
	buff.printBuffer(40, false);
	System.out.println();
	Buffer buff2 = new Buffer(512);
	buff2.copyBuffer(buff);
	System.out.println("1." + buff2.getString());
	System.out.println("2." + buff2.getShort());
	System.out.println("3." + buff2.getString());
    }
}

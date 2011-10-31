import java.io.*;

/**
 * This class reads blocks of a file.
 */
public class FileBlocksReader {
	private int index;
	private FileInputStream input;
	private long length;
	private int blockLength;

	/**
	 * Creates a FileBlocksReader of file specified in filename and read blocks of length blockLength
	 * @param filename is the name of the file.
	 * @param blockLength is the length of blocks.
	 * @throws FileNotFoundException if file don't exists.
	 * @throws SecurityException if can read to file specified in filename.
	 */
	public FileBlocksReader(String filename, int blockLength) throws FileNotFoundException, SecurityException {
		File file = new File(filename);
		if(!file.exists()) {
			throw new FileNotFoundException("File " + filename + "not found.");
		}
		if(!file.canRead()) {
			throw new SecurityException("Can read file " + filename);
		}
		this.blockLength = blockLength;
		index = 0;
		length = file.length();
		input = new FileInputStream(file);
	}

	/**
	 * Free all resources used.
	 * @throws IOException if there are any problem.
	 */
	public void close() throws IOException {
		input.close();
	}

	/**
	 * Tells if the last block already was readed.
	 * @return if the last block already was readed.
	 */
	public boolean hasNext() {
		return (index+1)*blockLength > length;
	}

	/**
	 * Return the next bloc index.
	 * @return the next index.
	 */
	public int nextIndex() {
		return index;
	}

	/**
	 * Read a block and save it in b.
	 * @param b is the buffer into which the data readed will be write.
	 * @param off is the offset at which data readed will be start to write in b.
	 * @throws EOFException if the last block of file already was writted.
	 * @throws IOException if a general error ocurred while reading.
	 * @throws ArrayIndexOutOfBoundsException if available bytes in b are less than next block length.
	 * @return the length of block readed.
	 */
	public int read(byte[] b, int off) throws IOException, ArrayIndexOutOfBoundsException {
		if(!hasNext()) {
			throw new EOFException("Last block was readed.");
		}
		long tmp = (index+1)*blockLength - length;
		int nextBlockLength = (int) ((tmp > 0) ? tmp : blockLength);
		if(nextBlockLength > b.length - off) {
			throw new ArrayIndexOutOfBoundsException("Array b only have "+ 
				(b.length-off) +" available bytes and block have length " +nextBlockLength);
		}
		input.read(b, off, nextBlockLength);
		index++;
		return nextBlockLength;
	}

}

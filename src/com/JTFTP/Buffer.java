package com.JTFTP;

import java.util.*;
import java.io.*;

/**
 * This class provides a buffer in which we can save short's, strings and blocks of bytes.
 */
public class Buffer {

	private byte buffer[];
	private int offset;

	/**
	 * Creates an empty buffer with length bytes capacity (including separators of strings).
	 */
	public Buffer(int length) {
		buffer = new byte[length];
		reset();
	}

	/**
	 * Creates a buffer from byte array.
	 */
	public Buffer(byte[] buffer) {
		this.buffer = buffer;
	}

	/**
	 * Deletes the content of buffer.
	 */
	public void reset() {
		Arrays.fill(buffer, (byte)0);
		offset = 0;
	}

	/**
	 * Save the string data into buffer.
	 * @param data is the string to save.
	 * @throws UnsupportedEncodingException if US-ASCII encoding is not supported.
	 * @throws ArrayIndexOutOfBoundsException if data not fit in the buffer.
	 */
	public int addString(String data) throws UnsupportedEncodingException, ArrayIndexOutOfBoundsException {
		boolean status;
		byte[] tmpByteArray;

		tmpByteArray = data.getBytes("US-ASCII");
		if(buffer.length < offset + tmpByteArray.length + 1) {
			throw new ArrayIndexOutOfBoundsException("The string \""+data+"\" doesn't fit in the buffer.");
		}

		System.arraycopy(tmpByteArray, 0, buffer, offset, tmpByteArray.length);

		offset += tmpByteArray.length;
		buffer[offset] = 0;

		offset++;
		return tmpByteArray.length+1;
	}

	/**
	 * Says the number of bytes that would occupy data plus 1 (the separator).
	 * @param data is the string whose length representation we want to know.
	 * @return the number of bytes that would occupy data plus 1 (the separator).
	 * @throws UnsupportedEncodingException if US-ASCII encoding is not supported.
	 */
	public static int length(String data) throws UnsupportedEncodingException {
		return data.getBytes("US-ASCII").length+1;
	}

	/**
	 * Gets a string of the buffer.
	 * @throws UnsupportedEncodingException if US-ASCII encoding is not supported.
	 * @return the next string of the buffer.
	 * @throws ArrayIndexOutOfBoundsException if there is no string to read in this buffer.
	 */
	public String getString() throws UnsupportedEncodingException, ArrayIndexOutOfBoundsException {
		int tmpOffset = offset;
		byte[] tmpByteArray = new byte[buffer.length-tmpOffset];
		int i = 0;

		while(tmpOffset < buffer.length && buffer[tmpOffset] != 0) {
			tmpByteArray[i] = buffer[tmpOffset];

			i++;
			tmpOffset++;
		}

		if(tmpOffset == buffer.length) {
			throw new ArrayIndexOutOfBoundsException("There is no string to read in this buffer.");
		}

		offset = tmpOffset + 1;

		return new String(tmpByteArray, "US-ASCII");
	}

	/**
	 * Save a short into the buffer.
	 * @param data is the integer to save.
	 * @throws ArrayIndexOutOfBoundsException if data not fit in the buffer.
	 */
	public void addShort(int data) throws ArrayIndexOutOfBoundsException {
		if(buffer.length < offset + 2) {
			throw new ArrayIndexOutOfBoundsException("The short not fit in the buffer.");
		}
		buffer[offset] = (byte) ((data & 0x0000FF00) >> 8);
		offset++;
		buffer[offset] = (byte)  (data & 0x000000FF);
		offset++;
	}

	/**
	 * Gets a short of the buffer.
	 * @return the next short of the buffer.
	 * @throws ArrayIndexOutOfBoundsException if there aren't 2 or more bytes to read. 
	 */
	public int getShort() throws ArrayIndexOutOfBoundsException {
		if(buffer.length < offset + 2) {
			throw new ArrayIndexOutOfBoundsException("Threre are less than 2 bytes to read and short can't be obtained.");
		}
                
                int tmp = buffer[offset++];
                tmp = (tmp < 0) ? 256 + tmp : tmp;
                int tmp2 = buffer[offset++];
                tmp2 = (tmp2 < 0) ? 256 + tmp2 : tmp2;
                return tmp << 8 | tmp2;
	}

	/**
	 * Gets buffer's content.
	 * @return byte array with buffer's content.
	 */
	public byte[] dumpBuffer() {
		return buffer;
	}

	/**
	 * Set buffer as content of Buffer and reset the offset.
	 * @param buffer is a byte array with the new content of Buffer.
	 */
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
		offset = 0;
	}

	/**
	 * Prints min(max(howMuch, 0), offset) bytes of buffer.
	 * @param howMuch indicates how many bytes have to print.
	 */
	public void printBuffer(int howMuch, boolean toString) {
		int bytesToPrint = (howMuch > 0) ? howMuch : 0;
		bytesToPrint = (bytesToPrint < offset) ? bytesToPrint : offset;
		for (int i = 0; i < bytesToPrint; i++) {
			System.out.print(buffer[i] + ", ");
		}
	}

	/**
	 * Add length bytes of the block b to the buffer.
	 * @param b is the block of bytes.
	 * @param length is the quantity of bytes to add.
	 * @throws ArrayIndexOutOfBoundsException if there are any problem with length.
	 */
	public void addBlock(byte b[], int length) throws ArrayIndexOutOfBoundsException {
		if(buffer.length < length + offset) {
			throw new ArrayIndexOutOfBoundsException("Buffer don't have " +length +" bytes available");
		}else if(length <= 0) {
			throw new ArrayIndexOutOfBoundsException("Recevied length "+length+" but length can't be negative or zero.");
		}else if(length > b.length) {
			throw new ArrayIndexOutOfBoundsException("Recevied length "+length+" is bigger than b length "+ b.length);
		}
		System.arraycopy(b, 0, buffer, offset, length);
		offset += length;
	}

	/**
	 * Get a block of length bytes of the buffer.
	 * @param length is the quantity of bytes to get.
	 * @throws ArrayIndexOutOfBoundsException if there are any problem with length.
	 */
	public byte[] getBlock(int length) throws ArrayIndexOutOfBoundsException {
		if(buffer.length < length + offset) {
			throw new ArrayIndexOutOfBoundsException("Buffer don't have " +length +" bytes.");
		}else if(length <= 0) {
			throw new ArrayIndexOutOfBoundsException("Recevied size "+length+" but size can't be negative or zero.");
		}
		byte b[] = new byte[length];
		System.arraycopy(buffer, offset, b, 0, length);
		offset += length;
		return b;
	}

	/**
	 * Copy the content of buffer and reset the offset.
	 * @param buffer is the Buffer to copy.
	 */
	public void copyBuffer(Buffer buffer) {
		this.buffer = buffer.dumpBuffer();
		offset = 0;
	}

	/**
	 * Return the offset at which next element will be put which coincides with
	 * the number of bytes occupied.
	 * @return the offset.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Resize the buffer to the new length. If length is 0 or less throws an
	 * IllegalArgumentException
	 */
	public void setLength(int length) {
		if(length <= 0) {
			throw new IllegalArgumentException("length can't be 0 or less");
		}
		if(length < buffer.length) {
			byte[] tmp = new byte[length];
			System.arraycopy(buffer, 0, tmp, 0, tmp.length);
			offset = (length < offset) ? length : offset;
			buffer = tmp;
		}else if(length > buffer.length) {
			byte[] tmp = new byte[length];
			System.arraycopy(buffer, 0, tmp, 0, buffer.length);
			buffer = tmp;
		}
	}

	/**
	 * Return the length of the buffer.
	 * @return the length of the buffer.
	 */
	public int getLength() {
		return buffer.length;
	}
}

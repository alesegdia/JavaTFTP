import java.io.*;

/**
 * This exception is thrown when file exists and overwrite is not desired.
 */
public class FileFoundException extends IOException {

	/**
	 * Constructs a FileFoundException with no message.
	 */
	public FileFoundException() {
		super();
	}

	/**
	 * Constructs a FileFoundException with the specified message.
	 * @param message is the description of problem.
	 */
	public FileFoundException(String message) {
		super(message);
	}
}

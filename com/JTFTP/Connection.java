public class Connection {
    private final static boolean OCTET = true;
    private final static boolean READ = true;

    private TID currTID;
    private bool rw;
    private String fileName;
    private String mode;

    public Connection (TID currTID, bool rw, String fileName, String mode) {
	this.currTID = currTID;
	this.rw = rw;
	this.fileName = fileName;
	this.mode = mode;
    }
}

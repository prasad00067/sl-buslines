package om.sl.sllonglines.exception;

public class BusServiceException extends Exception{

	private static final long serialVersionUID = 1L;
	private int errorCode;
	
	public BusServiceException(final String message, final int error_code) {
        super(message);
        this.errorCode = error_code;
    }
	
	public int getErrorCode() {
		return this.errorCode;
	}


}

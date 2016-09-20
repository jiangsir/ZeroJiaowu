/**
 * idv.jiangsir.Exceptions - ServerException.java
 * 2011/7/31 下午1:27:15
 * nknush-001
 */
package tw.jiangsir.Utils.Exceptions;

/**
 * @author nknush-001
 * 
 */
public class DataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataException() {
		super();
	}

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataException(Throwable cause) {
		super(cause);
	}

	public DataException(String message) {
		super(message);
	}

	public DataException(String session_account, String message) {
		super(message);
		System.out.println("session_account=" + session_account);
	}

	public DataException(Throwable cause, String session_account) {
		super(cause);
	}
}

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
public class AlertException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlertException() {
		super();
	}

	public AlertException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlertException(Throwable cause) {
		super(cause);
	}

	public AlertException(String message) {
		super(message);
	}

	public AlertException(String session_account, String message) {
		super(message);
		System.out.println("session_account=" + session_account);
	}

	public AlertException(Throwable cause, String session_account) {
		super(cause);
	}
}

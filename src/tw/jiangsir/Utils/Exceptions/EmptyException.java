/**
 * idv.jiangsir.utils - ZjException.java
 * 2010/9/15 下午1:30:49
 * nknush-001
 */
package tw.jiangsir.Utils.Exceptions;

/**
 * 當原本要取出的資料為空的時候拋出 EmptyException <br>
 * 1. SQL 取資料時 size==0
 * 
 * @author nknush-001
 * 
 */
public class EmptyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptyException() {
		super();
	}

	public EmptyException(Throwable cause) {
		super(cause.getLocalizedMessage(), cause);
	}

	public EmptyException(AccessCause cause) {
		super(cause.getText_message() + ": debug:" + cause.getDebug_message(),
				cause);
	}

	/**
	 * @param string
	 */
	public EmptyException(String session_account, String string) {
		super(string);
		// Logger logger = Logger.getLogger(this.getClass().getName());
		// logger.log(Level.WARNING, "ZjException: " + string);
	}

}

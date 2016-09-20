/**
 * idv.jiangsir.utils - ZjException.java
 * 2010/9/15 下午1:30:49
 * nknush-001
 */
package tw.jiangsir.Utils.Exceptions;

import tw.jiangsir.Utils.CurrentUser;

/**
 * @author nknush-001
 * 
 */
public class AccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4800234202163628817L;

	public AccessException() {
		super();
	}

	// public AccessException(Throwable throwable) {
	// super(throwable.getLocalizedMessage(), new AccessCause(
	// AccessCause.TYPE.INFORMATION, UserFactory.getNullOnlineUser(),
	// ""));
	// }

	public AccessException(String message, AccessCause accessCause) {
		super(message, accessCause);
	}

	// /**
	// * @param string
	// */
	// public AccessException(String session_account, String string) {
	// super(string);
	// // Logger logger = Logger.getLogger(this.getClass().getName());
	// // logger.log(Level.WARNING, "ZjException: " + string);
	// }

	/**
	 * @param message
	 */
	public AccessException(CurrentUser currentUser, String message) {
		super(message, new AccessCause(AccessCause.TYPE.ERROR, currentUser, ""));
	}

}

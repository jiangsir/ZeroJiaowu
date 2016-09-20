/**
 * idv.jiangsir.Objects - Message.java
 * 2008/12/19 下午 01:23:42
 * jiangsir
 */
package tw.jiangsir.Utils;

import java.util.HashMap;

/**
 * @author jiangsir
 * 
 */
public class AlertBean {
	public final static String Type_INFOR = "INFOR";
	public final static String Type_ALERT = "ALERT";
	public final static String Type_ERROR = "ERROR";
	private String Type = AlertBean.Type_ALERT;
	private String Title = "None!";
	private String Subtitle = "";
	private String PlainText = "";
	private String ResourceMessage = "";
	private String stacktrace = "";
	private CurrentUser currentUser;

	private HashMap<String, String> Links = new HashMap<String, String>();

	public AlertBean() {
	}

	public AlertBean(Throwable t) {
		this.setTitle(t.getLocalizedMessage());
		this.setStacktrace(t);
	}

	public AlertBean(String title) {
		this.Title = title;
	}

	public HashMap<String, String> getLinks() {
		return Links;
	}

	public void setLinks(HashMap<String, String> links) {
		Links = links;
	}

	public String getPlainText() {
		return PlainText;
	}

	public void setPlainText(String plainText) {
		PlainText = plainText;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getSubtitle() {
		return Subtitle;
	}

	public void setSubtitle(String subtitle) {
		Subtitle = subtitle;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getResourceMessage() {
		return ResourceMessage;
	}

	public void setResourceMessage(String resourceMessage) {
		ResourceMessage = resourceMessage;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(Throwable t) {
		StringBuffer sb = new StringBuffer(5000);
		StackTraceElement[] st = t.getStackTrace();
		for (int i = 0; i < st.length; i++) {
			sb.append(st[i] + "\n");
		}
		this.stacktrace = sb.toString();
	}

	// public String getSession_account() {
	// return session_account;
	// }
	//
	// public void setSession_account(String sessionAccount) {
	// session_account = sessionAccount;
	// }

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

}

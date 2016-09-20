/**
 * idv.jiangsir.Objects - Message.java
 * 2008/12/19 下午 01:23:42
 * jiangsir
 */
package tw.jiangsir.ZeroJiaowu.Objects;

import java.util.HashMap;

/**
 * @author jiangsir
 *
 */
public class Message {

	private final static String MessageType_ALERT = "ALERT";

	private final static String MessageType_ERROR = "ERROR";

	private String Type = Message.getMessageType_ALERT();

	private String Title = "";

	private String PlainText = "";

	private String ResourceMessage = "";

	private HashMap<String, String> Links = new HashMap<String, String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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

	public static String getMessageType_ALERT() {
		return MessageType_ALERT;
	}

	public static String getMessageType_ERROR() {
		return MessageType_ERROR;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
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

}

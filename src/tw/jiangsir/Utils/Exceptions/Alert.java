package tw.jiangsir.Utils.Exceptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.Persistent;

public class Alert extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum TYPE {
		INFO, // 處理一些一般訊息的呈現。
		DATAERROR, // 資料錯誤，各種欄位資料的檢查。
		ROLEERROR, // 角色權限不正確。
		IPERROR, // IP 來源被拒。
		WARNING, // 顯示使用者填報錯誤，或違規行為。
		EXCEPTION, // 系統丟出的例外。
		ERROR // 顯示一些未知、未處理的錯誤訊息。
	};

	@Persistent(name = "type")
	private TYPE type = TYPE.INFO;
	@Persistent(name = "title")
	private String title = "";
	@Persistent(name = "subtitle")
	private String subtitle = "";
	@Persistent(name = "content")
	private String content = "";
	@Persistent(name = "list")
	private ArrayList<String> list = new ArrayList<String>();
	@Persistent(name = "map")
	private HashMap<String, String> map = new HashMap<String, String>();
	@Persistent(name = "stacktrace")
	private StackTraceElement[] stacktrace = new StackTraceElement[]{};
	@Persistent(name = "urls")
	private HashMap<String, URI> uris = new HashMap<String, URI>();
	@Persistent(name = "debugs")
	private HashSet<String> debugs = new HashSet<String>();
	@Persistent(name = "currentuser")
	private CurrentUser currentUser = new CurrentUser();

	public Alert() {
	}

	public Alert(TYPE type, String title, String subtitle, String content, HashMap<String, URI> uris) {
		this.setType(type);
		this.setTitle(title);
		this.setSubtitle(subtitle);
		this.setContent(content);
		this.appendUris(uris);
	}

	public Alert(String title, Throwable throwable) {
		this.setType(TYPE.EXCEPTION);
		this.setTitle(title);
		this.setSubtitle(throwable.getClass().getName());
		this.setStacktrace(throwable.getStackTrace());
	}

	public Alert(Throwable throwable) {
		this(throwable.getLocalizedMessage(), throwable);
	}

	// public Alert(Cause cause) {
	// this.setType(TYPE.EXCEPTION);
	// this.setTitle(cause.getTitle());
	// this.setSubtitle(cause.getSubtitle());
	// this.setList(cause.getContentlist());
	// this.setStacktrace(cause.getStackTrace());
	// this.setDebugs(cause.getDebugs());
	// this.setOnlineUser(cause.getOnlineUser());
	// }

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

	public StackTraceElement[] getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(StackTraceElement[] stacktrace) {
		this.stacktrace = stacktrace;
	}

	public HashMap<String, URI> getUris() {
		return uris;
	}

	public void setUris(HashMap<String, URI> uris) {
		this.uris = uris;
	}

	public void appendUris(HashMap<String, URI> uris) {
		if (uris != null && uris.size() > 0) {
			this.getUris().putAll(uris);
		}
	}

	public HashSet<String> getDebugs() {
		return debugs;
	}

	public void setDebugs(HashSet<String> debugs) {
		this.debugs = debugs;
	}

	public CurrentUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}

}

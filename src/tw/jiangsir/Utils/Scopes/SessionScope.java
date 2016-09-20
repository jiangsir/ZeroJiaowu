package tw.jiangsir.Utils.Scopes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.Utils;
import tw.jiangsir.Utils.Servlets.LoginServlet;
import tw.jiangsir.Utils.Servlets.LogoutServlet;
import tw.jiangsir.Utils.Servlets.ShowSessionsServlet;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.jiangsir.ZeroJiaowu.Objects.IpAddress;

public class SessionScope implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -780144410265117353L;
	private HttpSession session = null;
	private String sessionid = "";
	private ArrayList<IpAddress> session_ipset = new ArrayList<IpAddress>();
	private LinkedHashSet<String> session_privilege = new LinkedHashSet<String>();
	private Locale session_locale = new Locale("zh", "tw");
	private String session_useragent = "";
	private HashMap<String, String> session_requestheaders = new HashMap<String, String>();
	private CurrentUser onlineUser = null;
	private long idle = 0;
	private ArrayList<String> histories = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7989893466104807011L;

		{
			add("");
			add("");
		}
	};

	public SessionScope(HttpServletRequest request) {
		this(request.getSession(false));
	}

	@SuppressWarnings("unchecked")
	public SessionScope(HttpSession session) {
		if (session == null) {
			return;
		}

		try {
			session.getCreationTime();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return;
		}

		this.session = session;
		this.setSessionid(session.getId());
		this.setSession_ipset(
				(ArrayList<IpAddress>) session.getAttribute("session_ipset"));
		this.setSession_privilege((LinkedHashSet<String>) session
				.getAttribute("session_privilege"));
		this.setSession_locale((Locale) session.getAttribute("session_locale"));
		this.setSession_useragent(
				(String) session.getAttribute("session_useragent"));
		this.setSession_requestheaders((HashMap<String, String>) session
				.getAttribute("session_requestheaders"));
		this.setOnlineUser(session.getAttribute("onlineUser"));
		// this.setReturnPages(session.getAttribute("returnPages"));
		this.setHistories(
				(ArrayList<String>) session.getAttribute("histories"));
		this.setIdle(
				System.currentTimeMillis() - session.getLastAccessedTime());
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		session.setAttribute("sessionid", sessionid);
		this.sessionid = sessionid;
	}

	// public LinkedHashSet<IpAddress> getSession_ipset() {
	// return session_ipset;
	// }
	//
	// public void setSession_ipset(LinkedHashSet<IpAddress> session_ipset) {
	// if (session_ipset == null) {
	// return;
	// }
	// session.setAttribute("session_ipset", session_ipset);
	// this.session_ipset = session_ipset;
	// }

	public void setSession_ipset(String session_ipset) {
		if (session_ipset == null) {
			return;
		}
		// session.setAttribute("session_ipset", session_ipset);
		this.setSession_ipset(StringTool.String2IpAddressList(session_ipset));
	}

	public ArrayList<IpAddress> getSession_ipset() {
		return session_ipset;
	}

	public void setSession_ipset(ArrayList<IpAddress> session_ipset) {
		if (session_ipset == null) {
			return;
		}
		session.setAttribute("session_ipset", session_ipset);
		this.session_ipset = session_ipset;
	}

	public void setSession_ipset(HttpServletRequest request) {
		if (request == null) {
			return;
		}
		// session.setAttribute("session_ipset", session_ipset);
		this.setSession_ipset(new Utils().getIpList(request));
	}

	public LinkedHashSet<String> getSession_privilege() {
		return session_privilege;
	}

	public void setSession_privilege(LinkedHashSet<String> session_privilege) {
		if (session_privilege == null) {
			return;
		}
		session.setAttribute("session_privilege", session_privilege);
		this.session_privilege = session_privilege;
	}

	public Locale getSession_locale() {
		return session_locale;
	}

	public void setSession_locale(Locale session_locale) {
		if (session_locale == null) {
			return;
		}
		session.setAttribute("session_locale", session_locale);
		this.session_locale = session_locale;
	}

	public String getSession_useragent() {
		return session_useragent;
	}

	public void setSession_useragent(String session_useragent) {
		if (session_useragent == null) {
			return;
		}
		session.setAttribute("session_useragent", session_useragent);
		this.session_useragent = session_useragent;
	}

	public HashMap<String, String> getSession_requestheaders() {
		return session_requestheaders;
	}

	public void setSession_requestheaders(
			HashMap<String, String> session_requestheaders) {
		if (session_requestheaders == null) {
			return;
		}
		session.setAttribute("session_requestheaders", session_requestheaders);
		this.session_requestheaders = session_requestheaders;
	}

	/**
	 * 若不曾登入，回傳 null
	 * 
	 * @return
	 */
	public CurrentUser getOnlineUser() {
		return onlineUser;
	}

	public void setOnlineUser(Object onlineUser) {
		if (onlineUser == null || !(onlineUser instanceof CurrentUser)) {
			return;
		}
		this.removeOnlineUser(); // 先移除再加入。 上線人數才會正確
		session.setAttribute("onlineUser", onlineUser);
		this.onlineUser = (CurrentUser) onlineUser;
	}

	public void removeOnlineUser() {
		session.removeAttribute("onlineUser");
		this.onlineUser = null;
	}

	// @SuppressWarnings("unchecked")
	// public ArrayList<String> getReturnPages() {
	// if (session.getAttribute("returnPages") != null) {
	// this.returnPages = (ArrayList<String>) session
	// .getAttribute("returnPages");
	// }
	// return this.returnPages;
	// }
	//
	// @SuppressWarnings("unchecked")
	// public void setReturnPages(Object returnPages) {
	// if (returnPages == null || !(returnPages instanceof ArrayList)) {
	// this.returnPages = new ArrayList<String>();
	// this.returnPages.add("/");
	// this.returnPages.add("/");
	// session.setAttribute("returnPages", this.returnPages);
	// return;
	// }
	// this.returnPages = (ArrayList<String>) returnPages;
	// session.setAttribute("returnPages", this.returnPages);
	// }
	//
	// public void setReturnPage(String servletPath, String querystring) {
	// if (servletPath.startsWith(LoginServlet.class.getAnnotation(
	// WebServlet.class).urlPatterns()[0])
	// || servletPath.startsWith(LogoutServlet.class.getAnnotation(
	// WebServlet.class).urlPatterns()[0])
	// || servletPath.startsWith(ShowSessionsServlet.class
	// .getAnnotation(WebServlet.class).urlPatterns()[0])
	// || servletPath.startsWith("/Update")
	// || servletPath.startsWith("/Insert")
	// || servletPath.startsWith("/api/")
	// || servletPath.endsWith(".ajax")
	// || servletPath.endsWith(".api")) {
	// return;
	// }
	// ArrayList<String> returnPages = this.getReturnPages();
	// String returnPage = servletPath
	// + (querystring == null ? "" : "?" + querystring);
	// // System.out.println("1returnPages=" + returnPages);
	// if (!returnPages.get(0).equals(returnPage)) {
	// returnPages.remove(returnPages.size() - 1);
	// // System.out.println("2returnPages=" + returnPages);
	// returnPages.add(0, servletPath
	// + (querystring == null ? "" : "?" + querystring));
	// // System.out.println("3returnPages=" + returnPages);
	// this.setReturnPages(returnPages);
	// }
	// }

	@SuppressWarnings("unchecked")
	public ArrayList<String> getHistories() {
		if (session != null && session.getAttribute("histories") != null) {
			this.histories = (ArrayList<String>) session
					.getAttribute("histories");
		}
		return this.histories;
	}

	public void setHistories(ArrayList<String> histories) {
		if (histories == null || session == null) {
			return;
		}
		this.histories = histories;
		session.setAttribute("histories", histories);
	}

	public void addHistory(String servletPath, String querystring) {
		if (servletPath
				.startsWith(LoginServlet.class.getAnnotation(WebServlet.class)
						.urlPatterns()[0])
				|| servletPath.equals(LogoutServlet.class
						.getAnnotation(WebServlet.class).urlPatterns()[0])
				|| servletPath.equals(ShowSessionsServlet.class
						.getAnnotation(WebServlet.class).urlPatterns()[0])
				|| servletPath.startsWith("/Update")
				|| servletPath.startsWith("/Insert")
				|| servletPath.startsWith("/api/")
				|| servletPath.endsWith(".ajax")
				|| servletPath.endsWith(".api")) {
			return;
		}
		ArrayList<String> histories = this.getHistories();
		String history = servletPath
				+ (querystring == null ? "" : "?" + querystring);
		System.out.println("histories1=" + histories);
		if (!history.equals(histories.get(0))) {
			histories.remove(histories.size() - 1);
			System.out.println("histories2=" + histories);
			histories.add(0, history);
			System.out.println("histories3=" + histories);
			this.setHistories(histories);
		}
	}

	/**
	 * 回到前一頁
	 * 
	 * @return
	 */
	public String getPreviousPage() {
		return "." + this.getHistories().get(1);
	}

	/**
	 * 回到同一頁。
	 * 
	 * @return
	 */
	public String getCurrentPage() {
		return "." + this.getHistories().get(0);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(5000);
		// for (Method method : this.getClass().getDeclaredMethods()) {
		//
		// }
		buffer.append("sessionid=" + this.getSessionid() + "\n");
		buffer.append("session_ipset=" + this.getSession_ipset() + "\n");
		buffer.append("onlineUser=" + this.getOnlineUser() + "\n");
		buffer.append("hiestories=" + this.getHistories() + "\n");
		buffer.append(
				"session_useragent=" + this.getSession_useragent() + "\n");
		buffer.append("session_requestheaders="
				+ this.getSession_requestheaders() + "\n");

		return buffer.toString();
	}

	public long getIdle() {
		return idle;
	}

	public int getIdle_min() {
		return (int) idle / 60 / 1000;
	}

	public void setIdle(long idle) {
		this.idle = idle;
	}

}

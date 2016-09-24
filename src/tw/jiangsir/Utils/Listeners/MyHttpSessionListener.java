package tw.jiangsir.Utils.Listeners;

import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.ENV;
import tw.jiangsir.Utils.Scopes.SessionScope;

@WebListener
public class MyHttpSessionListener implements javax.servlet.http.HttpSessionListener, ServletRequestListener {

	private HttpServletRequest request;

	public void requestDestroyed(ServletRequestEvent event) {

	}

	public void requestInitialized(ServletRequestEvent event) {
		request = (HttpServletRequest) event.getServletRequest();
	}

	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		// TODO 20090210 在此分析 IP 連線數
		String ipfrom = request.getRemoteAddr();
		session.setAttribute("remoteAddr", ipfrom);
		session.setAttribute("Locale", request.getLocale().toString());

		synchronized (ENV.OnlineSessions) {
			if (session != null && session.getId() != null) {
				ENV.OnlineSessions.put(session.getId(), session);
			}
		}
		// qx request.getHeader 一定得在 Listener 裡
		HashMap<String, String> loggingmap = new HashMap<String, String>();
		Enumeration<String> enu = request.getHeaderNames();
		while (enu.hasMoreElements()) {
			String HeaderName = enu.nextElement().toString();
			// qx 觀察 getHeader
			// System.out.println(ENV.logHeader() + HeaderName + " = "
			// + request.getHeader(HeaderName));
			loggingmap.put(HeaderName, request.getHeader(HeaderName));
		}

	}

	public void sessionDestroyed(HttpSessionEvent event) {
		String ipfrom = request.getRemoteAddr();
		if (ENV.IP_CONNECTION != null && ENV.IP_CONNECTION.containsKey(ipfrom) && ENV.IP_CONNECTION.get(ipfrom) > 1) {
			ENV.IP_CONNECTION.put(ipfrom, ENV.IP_CONNECTION.get(ipfrom) - 1);
		} else {
			ENV.IP_CONNECTION.remove(ipfrom);
		}
		HttpSession session = event.getSession();
		String sessionid = session.getId();
		CurrentUser currentUser = new SessionScope(session).getCurrentUser();
		currentUser.doLogout();
		synchronized (ENV.OnlineSessions) {
			ENV.OnlineSessions.remove(sessionid);
		}
	}

}

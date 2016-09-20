package tw.jiangsir.Utils.Listeners;

import java.util.Hashtable;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;

import tw.jiangsir.Utils.ENV;

@WebListener
public class MyHttpSessionAttributeListener implements
		javax.servlet.http.HttpSessionAttributeListener {

	@SuppressWarnings("unchecked")
	public void attributeAdded(HttpSessionBindingEvent event) {
		String name = event.getName();
		// 20081021 if (name.equals("Userinfo")) {
		if (name.equals("currentUser")) {
			HttpSession session = event.getSession();
			System.out.println("Listener 加入 sessionid=" + session.getId()
					+ ", session=" + session);
			synchronized (ENV.context) {
				Hashtable<String, HttpSession> OnlineUsers = (Hashtable<String, HttpSession>) ENV.context
						.getAttribute("OnlineUsers");
				OnlineUsers.put(session.getId(), session);
				ENV.context.setAttribute("OnlineUsers", OnlineUsers);
				// 20090520 改由 applicationScope 來處理
				// ENV.OnlineUsers.put(session.getId(), session);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void attributeRemoved(HttpSessionBindingEvent event) {
		String name = event.getName();
		// 20081021 if (name.equals("Userinfo")) {
		if (name.equals("currentUser")) {
			HttpSession session = event.getSession();
			System.out.println("Listener 移除 sessionid=" + session.getId()
					+ ", session=" + session);
			Hashtable<String, HttpSession> OnlineUsers = (Hashtable<String, HttpSession>) ENV.context
					.getAttribute("OnlineUsers");
			OnlineUsers.remove(session.getId());
			ENV.context.setAttribute("OnlineUsers", OnlineUsers);
		}
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {
		// System.out.println("attribute Listener 置換!!!");
	}
}

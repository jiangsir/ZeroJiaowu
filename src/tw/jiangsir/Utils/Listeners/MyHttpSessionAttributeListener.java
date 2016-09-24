package tw.jiangsir.Utils.Listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class MyHttpSessionAttributeListener implements javax.servlet.http.HttpSessionAttributeListener {

	@SuppressWarnings("unchecked")
	public void attributeAdded(HttpSessionBindingEvent event) {
		// 20160920
		// String name = event.getName();
		// if (name.equals("currentUser")) {
		// HttpSession session = event.getSession();
		// System.out.println("Listener 加入 sessionid=" + session.getId() + ",
		// session=" + session);
		// synchronized (ENV.context) {
		// // Hashtable<String, HttpSession> OnlineUsers =
		// // (Hashtable<String, HttpSession>) ENV.context
		// // .getAttribute("OnlineUsers");
		// // OnlineUsers.put(session.getId(), session);
		// // ENV.context.setAttribute("OnlineUsers", OnlineUsers);
		// }
		// }
	}

	@SuppressWarnings("unchecked")
	public void attributeRemoved(HttpSessionBindingEvent event) {
		// 20160920
		// String name = event.getName();
		// if (name.equals("currentUser")) {
		// HttpSession session = event.getSession();
		// System.out.println("Listener 移除 sessionid=" + session.getId() + ",
		// session=" + session);
		// Hashtable<String, HttpSession> OnlineUsers = (Hashtable<String,
		// HttpSession>) ENV.context
		// .getAttribute("OnlineUsers");
		// OnlineUsers.remove(session.getId());
		// ENV.context.setAttribute("OnlineUsers", OnlineUsers);
		// }
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {
		// System.out.println("attribute Listener 置換!!!");
	}
}

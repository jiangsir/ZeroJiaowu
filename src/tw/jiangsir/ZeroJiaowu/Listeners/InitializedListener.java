package tw.jiangsir.ZeroJiaowu.Listeners;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Daemon;
import tw.jiangsir.Utils.ENV;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.ZeroJiaowu.DAOs.UserDAO;

@WebListener
public class InitializedListener implements ServletContextListener {
	Daemon daemon = null;

	/**
	 *
	 */
	public void contextInitialized(ServletContextEvent event) {
		// this.daemon = new Daemon();
		// Thread daemonthread = new Thread(daemon);
		// daemonthread.start();
		// qx 暫不進行應用程式初始化<br>
		// qx 可考慮在此將 properties.xml 全部讀入 Context Initialized
		ServletContext context = event.getServletContext();

		ApplicationScope.setAllAttributes(context);

		Map<String, ? extends ServletRegistration> registrations = context.getServletRegistrations();
		for (String key : registrations.keySet()) {
			String servletClassName = registrations.get(key).getClassName();
			WebServlet webServlet;
			try {
				if (Class.forName(servletClassName).newInstance() instanceof HttpServlet) {
					HttpServlet httpServlet = (HttpServlet) Class.forName(servletClassName).newInstance();
					webServlet = httpServlet.getClass().getAnnotation(WebServlet.class);
					if (webServlet != null) {
						for (String urlpattern : webServlet.urlPatterns()) {
							ApplicationScope.getUrlpatterns().put(urlpattern, httpServlet);
						}
					}
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		for (String urlpattern : ApplicationScope.getUrlpatterns().keySet()) {
			System.out.println(
					"urlpattern=" + urlpattern + ", servlet=" + ApplicationScope.getUrlpatterns().get(urlpattern));
		}
		context.setAttribute("urlpatterns", ApplicationScope.getUrlpatterns());

		ENV.context = context;
		ENV.LastContextRestart = new Date();
		ENV.setAPP_REAL_PATH(new File(context.getRealPath("/")));
		// ENV.setMyPropertiesPath(ENV.APP_REAL_PATH +
		// "META-INF/properties.xml");
		// ENV.resource = ResourceBundle.getBundle("resource");
		context.setAttribute("OnlineUsers", new Hashtable<String, HttpSession>());
		// new DataBase(event).initConnection(context);

		new UserDAO().insertInitUsers();

	}

	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		while (!ENV.ThreadPool.isEmpty()) {
			Thread thread = ENV.ThreadPool.get(ENV.ThreadPool.firstKey());
			thread.interrupt();
			// System.out.println("關閉 thread: " + thread);
			ENV.ThreadPool.remove(ENV.ThreadPool.firstKey());
		}
		// TODO_DONE! qx 結束連結

		Connection conn = (Connection) context.getAttribute("conn");
		context.removeAttribute("conn");
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
			System.out.println(ENV.logHeader() + "資料庫連結關閉");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

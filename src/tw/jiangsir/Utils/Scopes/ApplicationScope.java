package tw.jiangsir.Utils.Scopes;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;

import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.ZeroJiaowu.Objects.User;

public class ApplicationScope {
	public static ServletContext servletContext = null;
	private static String built = null;
	private static File appRoot = null;
	private static String version = null;
	private static boolean isReleased = false;
	private static ConcurrentHashMap<String, HttpSession> onlineSessions = new ConcurrentHashMap<String, HttpSession>();
	private static TreeMap<String, CurrentUser> onlineUsers = new TreeMap<String, CurrentUser>();
	private static HashMap<String, HttpServlet> urlpatterns = new HashMap<String, HttpServlet>();
	private static HashMap<User.ROLE, HashSet<Class<? extends HttpServlet>>> roleMap = new HashMap<User.ROLE, HashSet<Class<? extends HttpServlet>>>();
	private static TreeMap<Long, Thread> threadPool = new TreeMap<Long, Thread>();
	private static ArrayList<User> generalManagers = new ArrayList<User>();
	private static String initException = "";

	public static void setAllAttributes(ServletContext servletContext) {
		ApplicationScope.servletContext = servletContext;
		ApplicationScope.setAppRoot();
		ApplicationScope.setVersion();
		ApplicationScope.setBuilt();
		ApplicationScope.setOnlineSessionScopes(onlineSessions);
		ApplicationScope.setUrlpatterns(urlpatterns);
		ApplicationScope.setRoleMap(roleMap);
		ApplicationScope.setThreadPool(threadPool);
	}

	public static ConcurrentHashMap<String, HttpSession> getOnlineSessions() {
		return onlineSessions;
	}

	public static void setOnlineSessionScopes(ConcurrentHashMap<String, HttpSession> onlineSessions) {
		ApplicationScope.onlineSessions = onlineSessions;
		ApplicationScope.servletContext.setAttribute("onlineSessions", onlineSessions);
	}

	public static TreeMap<String, CurrentUser> getOnlineUsers() {
		return onlineUsers;
	}

	public static void setOnlineUsers(TreeMap<String, CurrentUser> onlineUsers) {
		ApplicationScope.onlineUsers = onlineUsers;
		ApplicationScope.servletContext.setAttribute("onlineUsers", onlineUsers);
	}

	public static HashMap<String, HttpServlet> getUrlpatterns() {
		return urlpatterns;
	}

	public static void setUrlpatterns(HashMap<String, HttpServlet> urlpatterns) {
		ApplicationScope.urlpatterns = urlpatterns;
		ApplicationScope.servletContext.setAttribute("urlpatterns", urlpatterns);
	}

	public static HashMap<User.ROLE, HashSet<Class<? extends HttpServlet>>> getRoleMap() {
		return roleMap;
	}

	public static void setRoleMap(HashMap<User.ROLE, HashSet<Class<? extends HttpServlet>>> roleMap) {
		ApplicationScope.roleMap = roleMap;
		ApplicationScope.servletContext.setAttribute("roleMap", roleMap);
	}

	public static TreeMap<Long, Thread> getThreadPool() {
		return threadPool;
	}

	public static void setThreadPool(TreeMap<Long, Thread> threadPool) {
		ApplicationScope.threadPool = threadPool;
		ApplicationScope.servletContext.setAttribute("threadPool", threadPool);
	}

	public static String getBuilt() {
		if (ApplicationScope.built == null) {
			setBuilt();
		}
		return ApplicationScope.built;
	}

	public static void setBuilt() {
		ApplicationScope.built = new SimpleDateFormat("yyMMdd")
				.format(new Date(ApplicationScope.getAppRoot().lastModified()));
		servletContext.setAttribute("built", ApplicationScope.built);
	}

	public static File getAppRoot() {
		if (ApplicationScope.appRoot == null) {
			setAppRoot();
		}
		return ApplicationScope.appRoot;
	}

	public static void setAppRoot() {
		ApplicationScope.appRoot = new File(servletContext.getRealPath("/"));
		ApplicationScope.servletContext.setAttribute("appRoot", appRoot);
	}

	/**
	 * 直接指定 AppRoot，在單機直接執行的時候使用。因此不具備 serveltContext
	 * 
	 * @param appRoot
	 */
	public static void setAppRoot(File appRoot) {
		ApplicationScope.appRoot = appRoot;
		// ApplicationScope.servletContext.setAttribute("appRoot", appRoot);
	}

	/**
	 * 取得目前系統的版本。
	 */
	public static String getVersion() {
		if (ApplicationScope.version == null) {
			setVersion();
		}
		return ApplicationScope.version;
	}

	/**
	 * 取得目前系統的版本。
	 */
	public static void setVersion() {
		try {
			ApplicationScope.version = FileUtils
					.readFileToString(
							new File(ApplicationScope.getAppRoot() + File.separator + "META-INF", "Version.txt"))
					.trim();
		} catch (IOException e) {
			e.printStackTrace();
			// ApplicationScope.version = "";
		}
		servletContext.setAttribute("version", ApplicationScope.version);
	}

	public static boolean getIsReleased() {
		return isReleased;
	}

	// public static AppConfig getAppConfig() {
	// if (appConfig == null) {
	// ApplicationScope.appConfig = new AppConfigService().getAppConfig();
	// }
	// return appConfig;
	// }

	// public static void setAppConfig(AppConfig appConfig) {
	// ApplicationScope.appConfig = appConfig;
	// ApplicationScope.servletContext.setAttribute("appConfig", appConfig);
	// }

	public static void setHashUsers(Hashtable<Integer, User> hashUsers) {
		// HashUsers = hashUsers;
		ApplicationScope.servletContext.setAttribute("CacheUsers", hashUsers);
	}

	public static String getInitException() {
		return initException;
	}

	public static void setInitException(String initException) {
		ApplicationScope.initException = initException;
		ApplicationScope.servletContext.setAttribute("initException", initException);
	}

	public static ArrayList<User> getGeneralManagers() {
		return generalManagers;
	}

	public static void setGeneralManagers(ArrayList<User> generalManagers) {
		ApplicationScope.generalManagers = generalManagers;
		ApplicationScope.servletContext.setAttribute("generalManagers", generalManagers);
	}

}

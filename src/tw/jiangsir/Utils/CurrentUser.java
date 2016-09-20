package tw.jiangsir.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpSession;
import tw.jiangsir.ZeroJiaowu.Objects.User;

public class CurrentUser extends User {
	/**
	 * 紀錄該 SessionUser 所設定的 locale 語系。
	 */
	// private Locale locale = Problem.locale_en_US;
	// private String returnPage = "";
	private int loginid = 0;
	private String session_ip = "";
	private String sessionid = "";
	private HttpSession session = null;

	public CurrentUser() {
	}

	/**
	 * 將 user 轉成 SessionUserBean 進行向下轉型。
	 * 
	 * @param user
	 */
	public CurrentUser(HttpSession session, User user) {
		if (session != null) {
			this.setSession(session);
			this.setSessionid(session.getId());
			// 這裡如果讀取 session_locale 會導致問題。因為系統一開始啓動的時候，根本還沒有 session_locale
			// 但是如果不在這邊幫 sessionUserBean 將 locale 訊息設定上去，後面登入測驗 ShowContest
			// 就會出問題。
			// String session_locale = (String) session
			// .getAttribute("session_locale");
			// if (session_locale != null) {
			// this.setLocale(new Locale(session_locale.split("_")[0],
			// session_locale.split("_")[1]));
			// }
		}

		Object value;
		// for (Field field : this.getFields().values()) {
		for (Field field : User.class.getDeclaredFields()) {
			if (field.getAnnotation(Persistent.class) == null) {
				continue;
			}
			Method getter;
			try {
				getter = User.class.getMethod(
						"get" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1));
				value = getter.invoke((User) user);
				// logger.info("getter=" + getter.getName() + ", value=" +
				// value);
				Method setter = User.class.getMethod(
						"set" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1),
						new Class[]{value.getClass()});
				setter.invoke(this, new Object[]{value});
				// logger.info("setter=" + setter.getName() + ", parameter="
				// + value);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public String getSessionid() {
		if (sessionid == null || "null".equals(sessionid.toLowerCase())) {
			return "";
		}
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		if (sessionid == null || "null".equals(sessionid.toLowerCase())) {
			return;
		}
		this.sessionid = sessionid;
	}

	public int getLoginid() {
		return loginid;
	}

	public void setLoginid(int loginid) {
		this.loginid = loginid;
	}

	/**
	 * 登出相關動作, 包含 session 逾時也執行 doLogout <br>
	 * 
	 */
	public void doLogout() {

		// qx 因為 PreviousPage Filter 把 doChain 放到前面, 因此此處 session invalidate
		// 後就會出錯, 解法, 把 request.getSession() 拿到 doChain 前面就好了
		// if (SessionFactory.getSessionById(sessionid) != null) {
		// SessionFactory.getSessionById(sessionid).invalidate();
		// }
		if (session != null) {
			session.invalidate();
		}
		this.setSessionid("");
		this.setSession(null);
	}

	/**
	 *
	 *
	 */
	public void doRelogin(HttpSession session) {
		// 在 context restart 後，上線人數會歸零，這時可以透過其他 servlet 讓這些人慢慢重新加入
		// session.setAttribute("session_account", this.getAccount());
		// session.setAttribute("session_usergroup", this.getUsergroup());
		session.setAttribute("passed", "true");
		session.setAttribute("CurrentUser", this);
	}

	public String getSession_ip() {
		return session_ip;
	}

	public void setSession_ip(String session_ip) {
		this.session_ip = session_ip;
	}

	public boolean getIsIpdenied() {
		// return ENV.IP_DENIED.contains(session_ip);
		return false;
	}

	public long getIdle() {
		// HttpSession session = SessionFactory.getSessionById(sessionid);
		if (session == null) {
			return -1;
		}
		return System.currentTimeMillis() - session.getLastAccessedTime();
	}

	@Override
	public String toString() {
		return "account=" + this.getAccount() + ", userid=" + this.getId();
	}

	public String getReturnPage() {
		if (session == null) {
			return "";
		}
		return (String) session.getAttribute("returnPage");
	}

	/**
	 * * 根據 User 的群組權限、整站模式、參賽模式等計算使用者的權限。 servletPath 如 "/Index"
	 * 
	 * 20130111 改用 LinkedHashSet 來處理 privilege <br>
	 * 判斷 User 的 privilege <br>
	 * <br>
	 * 判定規則為：<br>
	 * 1. 越靠右邊優先權越高，蓋過左邊的設定 <br>
	 * 2. 允許 regex 寫法。<br>
	 * 3. null, "" 均回覆 NOTDEFINE
	 * 
	 * @param privilegeSet
	 * @param servletPath
	 * @return
	 * @throws AccessException
	 */
	// public boolean isAccessible(String servletPath) throws AccessException {
	// LinkedHashSet<String> privileges = this.getPrivilege();
	//
	// Resource.MESSAGE result = Resource.MESSAGE.Privilege_NOTDEFINE;
	// if (privileges == null || privileges.isEmpty()) {
	// result = Resource.MESSAGE.Privilege_NOTDEFINE;
	// }
	// for (String privilege : privileges) {
	// if (privilege.contains("#")) {
	// privilege = privilege.substring(0, privilege.indexOf("#"));
	// }
	//
	// if (privilege.startsWith("!")) {
	// if (privilege.equals("!" + servletPath)
	// || new String("!" + servletPath).matches(privilege)) {
	// result = Resource.MESSAGE.Privilege_FORBIDDEN;
	// }
	// } else {
	// if (privilege.equals(servletPath)
	// || servletPath.matches(privilege)) {
	// result = Resource.MESSAGE.Privilege_ALLOWED;
	// }
	// }
	// }
	//
	// AccessCause accessCause = new AccessCause(AccessCause.TYPE.WARNING,
	// this, "");
	// if (result == Resource.MESSAGE.Privilege_ALLOWED) {
	// return true;
	// } else if (result == Resource.MESSAGE.Privilege_NOTDEFINE) {
	// // 沒有允許存取該頁
	// // accessCause.setResource_message(result);
	// throw new AccessException("您沒有存取本頁的權限(" + servletPath + ")",
	// accessCause);
	// } else if (result == Resource.MESSAGE.Privilege_FORBIDDEN) {
	// // 明確禁止存取該頁
	// // accessCause.setResource_message(result);
	// throw new AccessException("您已被禁止存取本頁(" + servletPath + ")",
	// accessCause);
	// } else {
	// // accessCause.setResource_message(result);
	// throw new AccessException("因某些原因，您無法存取本頁(" + servletPath + ")",
	// accessCause);
	// }
	// }

	/**
	 * 這個 privilege 結合了 Group 裡的權限加上 extraprvilege
	 * 
	 * @return
	 */
	// public LinkedHashSet<String> getPrivilege() {
	// LinkedHashSet<String> privileges = new LinkedHashSet<String>();
	// String s = ENV.context.getInitParameter(this.getUsergroup().name());
	// for (String privilege : s.split(",")) {
	// privileges.add(privilege.trim());
	// }
	// return privileges;
	// }

	public boolean getIsNonLoginUser() {
		if (this.getId().intValue() == 0) {
			return true;
		}
		return false;
	}
}

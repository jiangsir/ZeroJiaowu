package tw.jiangsir.ZeroJiaowu.Objects;

import java.util.Hashtable;
import tw.jiangsir.Utils.Persistent;
import tw.jiangsir.ZeroJiaowu.DAOs.JobDAO;

/**
 *  - User.java
 * 2008/4/29 下午 05:41:54
 * jiangsir
 */

/**
 * @author jiangsir
 * 
 */
public class User {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "account")
	private String account = "";
	@Persistent(name = "username")
	private String username = "";
	@Persistent(name = "passwd")
	private String passwd = "";
	@Persistent(name = "number")
	private Integer number = 0;
	@Persistent(name = "comment")
	private String comment = "";

	// @Persistent(name = "usergroup")
	// private GROUP usergroup = GROUP.GroupGuest;
	//
	// public enum GROUP {
	// GroupGuest, GroupUser, GroupAdmin
	// }
	public enum ROLE {
		// ADMIN, // 除錯帳號
		DEBUGGER, // 除錯帳號
		MANAGER, // 站務管理員
		USER, // 一般使用者
		// CONTESTANT, // 競賽參與者
		GUEST; // 訪客，或未登入者
		public boolean isHigherEqualThan(ROLE role) {
			return this.ordinal() <= role.ordinal();
		}

		public boolean isLowerEqualThan(ROLE role) {
			return this.ordinal() >= role.ordinal();
		}
	}

	@Persistent(name = "role")
	private ROLE role = ROLE.GUEST;

	// public static final String GROUP_ADMIN = "GroupAdmin";
	// public static final String GROUP_USER = "GroupUser";
	// public static final String GROUP_GUEST = "GroupGuest";

	public User() {
	}

	// public User(String account) {
	// if (account == null || "".equals(account)) {
	// return;
	// }
	// String sql = "SELECT * FROM users WHERE useraccount='"
	// + Utils.intoSQL(account) + "'";
	// ArrayList<?> list = new DataBase().executeQuery(sql);
	// if (list.size() == 0) {
	// return;
	// }
	// this.init((HashMap<?, ?>) list.get(0));
	// }
	//
	// public User(HashMap<?, ?> map) {
	// if (map == null) {
	// return;
	// }
	// this.init(map);
	// }
	//
	// private void init(Map<?, ?> map) {
	// this.setUserid((Integer) map.get("userid"));
	// this.setUseraccount((String) map.get("useraccount"));
	// this.setSessionid((String) map.get("sessionid"));
	// this.setUsername((String) map.get("username"));
	// this.setNumber((Integer) map.get("number"));
	// this.setComment((String) map.get("comment"));
	// this.setUsergroup((String) map.get("usergroup"));
	// }

	public static String parseAccount(String account) {
		if (account != null && islegalAccount(account)) {
			return account;
		}
		return new User().getAccount();
	}

	/**
	 * islegalXXXXXX 檢查是否符合特定規則
	 * 
	 * @param account
	 * @return
	 */
	public static boolean islegalAccount(String account) {
		if (account != null && account.length() >= 3
				&& (account.matches("[a-zA-Z]+\\w+") || account.matches("[0-9]{7}") || account.matches("[0-9]{6}"))) {
			return true;
		}
		return false;
	}

	// =====================================================================

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	// public GROUP getUsergroup() {
	// return usergroup;
	// }
	//
	// public void setUsergroup(GROUP usergroup) {
	// this.usergroup = usergroup;
	// }
	//
	// public void setUsergroup(String usergroup) {
	// this.setUsergroup(User.GROUP.valueOf(usergroup));
	// }

	public ROLE getRole() {
		return role;
	}

	public void setRole(ROLE role) {
		this.role = role;
	}

	public void setRole(String role) {
		if (role == null) {
			return;
		}
		this.setRole(ROLE.valueOf(role));
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	// /**
	// * 這個 privilege 結合了 Group 裡的權限加上 extraprvilege
	// *
	// * @return
	// */
	// public String getPrivilege() {
	// return ENV.context.getInitParameter(usergroup);
	// }

	// /**
	// *
	// *
	// */
	// public static void relogin(User user, HttpSession session) {
	// // 在 context restart 後，上線人數會歸零，這時可以透過其他 servlet 讓這些人慢慢重新加入
	// session.setAttribute("session_account", user.getUseraccount());
	// session.setAttribute("session_usergroup", user.getUsergroup());
	// session.setAttribute("session_privilege", user.getPrivilege());
	// session.setAttribute("passed", "true");
	// session.setAttribute("UserObject", user);
	// // ENV.OnlineUsers.put(session.getId(), session);
	// }

	// ===================================================================
	public Hashtable<Integer, Job> getVisibleJobs() {
		return new JobDAO().getVisibleJobs(this.account);
	}

	public boolean getIsNullUser() {
		if (this.getId().equals(new User().getId()) && this.getAccount().equals(new User().getAccount())) {
			return true;
		}
		return false;
	}

	public boolean getIsAdmin() {
		return (this.getRole() == ROLE.MANAGER || this.getRole() == ROLE.DEBUGGER);
	}

}

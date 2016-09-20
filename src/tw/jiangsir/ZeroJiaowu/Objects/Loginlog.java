package tw.jiangsir.ZeroJiaowu.Objects;

import java.util.Date;

/**
 *  - User.java
 * 2008/4/29 下午 05:41:54
 * jiangsir
 */

/**
 * @author jiangsir
 * 
 */
public class Loginlog {
	private Integer id = 0;
	private Integer userid = 0;
	private String account = "";
	private String ipaddr = "";
	private String message = "";
	private Date logintime = new Date();
	private Date logouttime = new Date();

	public Loginlog() {

	}

	// public Loginlog(Integer userid) {
	// if (userid == null || userid.equals(0)) {
	// return;
	// }
	// String sql = "SELECT * FROM loginlogs WHERE userid=" + userid;
	// ArrayList list = new DataBase().executeQuery(sql);
	// if (list.size() == 0) {
	// return;
	// }
	// this.init((HashMap) list.get(0));
	// }
	//
	// public Loginlog(String useraccount) {
	// if (useraccount == null || "".equals(useraccount)) {
	// return;
	// }
	// String sql = "SELECT * FROM loginlogs WHERE useraccount='"
	// + useraccount + "'";
	// ArrayList list = new DataBase().executeQuery(sql);
	// if (list.size() == 0) {
	// return;
	// }
	// this.init((HashMap) list.get(0));
	// }
	//
	// public Loginlog(HashMap map) {
	// if (map == null) {
	// return;
	// }
	// this.init(map);
	// }
	//
	// private void init(Map map) {
	// this.setId((Integer) map.get("id"));
	// this.setUserid((Integer) map.get("userid"));
	// this.setAccount((String) map.get("useraccount"));
	// this.setIpaddr((String) map.get("ipaddr"));
	// this.setMessage((String) map.get("message"));
	// this.setLogintime((Date) map.get("logintime"));
	// this.setLogouttime((Date) map.get("logouttime"));
	// }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public Date getLogintime() {
		return logintime;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}

	public Date getLogouttime() {
		return logouttime;
	}

	public void setLogouttime(Date logouttime) {
		this.logouttime = logouttime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	// =====================================================================

}

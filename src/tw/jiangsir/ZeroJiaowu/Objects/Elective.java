package tw.jiangsir.ZeroJiaowu.Objects;

import java.sql.Timestamp;
import java.util.Date;
import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.Persistent;
import tw.jiangsir.ZeroJiaowu.DAOs.ElectiveDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.JobDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.UserDAO;
import tw.jiangsir.ZeroJiaowu.utils.Utils;

/**
 *  - User.java
 * 2008/4/29 下午 05:41:54
 * jiangsir
 */

/**
 * @author jiangsir
 * 
 */
public class Elective {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "jobid")
	private Integer jobid = 0;
	@Persistent(name = "account")
	private String account = "";
	@Persistent(name = "courseid1")
	private Integer courseid1 = 0;
	@Persistent(name = "courseid2")
	private Integer courseid2 = 0;
	@Persistent(name = "courseid3")
	private Integer courseid3 = 0;
	@Persistent(name = "courseid4")
	private Integer courseid4 = 0;
	@Persistent(name = "course1")
	private String course1 = "";
	@Persistent(name = "course2")
	private String course2 = "";
	@Persistent(name = "course3")
	private String course3 = "";
	@Persistent(name = "course4")
	private String course4 = "";
	@Persistent(name = "selected")
	private String selected = "";
	@Persistent(name = "nth")
	private Integer nth = 0;
	public static final int LOCK_LOCKED = 1;
	public static final int LOCK_UNLOCKED = 0;
	@Persistent(name = "lock")
	private Integer lock = LOCK_UNLOCKED;
	@Persistent(name = "submittime")
	private Timestamp submittime = new Timestamp(new Date().getTime());
	@Persistent(name = "ipfrom")
	private String ipfrom = "";

	// =================================================
	private CurrentUser currentUser = null;

	public Elective() {

	}

	// public Elective(Integer id) {
	// if (id == null || id.equals(0)) {
	// return;
	// }
	// String sql = "SELECT * FROM electives WHERE id=" + id;
	// ArrayList<?> list = new DataBase().executeQuery(sql);
	// if (list.size() == 0) {
	// return;
	// }
	// this.init((HashMap<?, ?>) list.get(0));
	// }
	//
	// public Elective(int jobid, String useraccount) {
	// if (useraccount == null || "".equals(useraccount)) {
	// return;
	// }
	// String sql = "SELECT * FROM electives WHERE jobid=" + jobid
	// + " AND useraccount='" + useraccount + "'";
	// ArrayList<?> list = new DataBase().executeQuery(sql);
	// if (list.size() == 0) {
	// return;
	// }
	// this.init((HashMap<?, ?>) list.get(0));
	// }
	//
	// public Elective(HashMap<?, ?> map) {
	// if (map == null) {
	// return;
	// }
	// this.init(map);
	// }
	//
	// private void init(Map<?, ?> map) {
	// this.setId((Integer) map.get("id"));
	// this.setJobid((Integer) map.get("jobid"));
	// this.setUseraccount((String) map.get("useraccount"));
	// this.setCourse1((String) map.get("course1"));
	// this.setCourse2((String) map.get("course2"));
	// this.setCourse3((String) map.get("course3"));
	// this.setCourse4((String) map.get("course4"));
	// this.setSelected((String) map.get("selected"));
	// this.setNth((Integer) map.get("nth"));
	// this.setLock((Integer) map.get("lock"));
	// this.setSubmittime((Date) map.get("submittime"));
	// this.setIpfrom((String) map.get("ipfrom"));
	// }

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public int getNth() {
		return nth;
	}

	public void setNth(Integer nth) {
		this.nth = nth;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(Integer lock) {
		this.lock = lock;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getJobid() {
		return jobid;
	}

	public void setJobid(Integer jobid) {
		this.jobid = jobid;
	}

	public String getCourse1() {
		return course1;
	}

	public void setCourse1(String course1) {
		if (course1 == null) {
			return;
		}
		this.course1 = course1;
	}

	public String getCourse2() {
		return course2;
	}

	public void setCourse2(String course2) {
		if (course2 == null) {
			return;
		}
		this.course2 = course2;
	}

	public String getCourse3() {
		return course3;
	}

	public void setCourse3(String course3) {
		if (course3 == null) {
			return;
		}
		this.course3 = course3;
	}

	public String getCourse4() {
		return course4;
	}

	public void setCourse4(String course4) {
		if (course4 == null) {
			return;
		}
		this.course4 = course4;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Timestamp getSubmittime() {
		return submittime;
	}

	public void setSubmittime(Timestamp submittime) {
		this.submittime = submittime;
	}

	public String getIpfrom() {
		return ipfrom;
	}

	public void setIpfrom(String ipfrom) {
		this.ipfrom = ipfrom;
	}

	// =====================================================================

	public CurrentUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}

	public User getUser() {
		return new UserDAO().getUserByAccount(this.getAccount());
	}

	public String getResult() {
		String text = "恭喜您完成志願選填作業！<br>";
		text += "如有疑問請將本畫面列印下來，並洽教務處。<br><br>";
		text += "您已經於 " + Utils.parseDatetime(this.getSubmittime().getTime()) + " 完成選填作業！(#" + this.getId() + ")<br>";
		text += "第一志願： " + this.getCourse1() + "<br>";
		if (this.getJob().getMax_choose() >= 2) {
			text += "第二志願： " + this.getCourse2() + "<br>";
		}
		if (this.getJob().getMax_choose() >= 3) {
			text += "第三志願： " + this.getCourse3() + "<br>";
		}
		if (this.getJob().getMax_choose() >= 4) {
			text += "第四志願： " + this.getCourse4() + "<br>";
		}
		return text;
	}

	public boolean getIsFinish() {
		return new ElectiveDAO().getElectiveByJobidAccount(jobid, account) != null;
	}

	public boolean getIsReserved() {
		return new ElectiveDAO().isReserved(jobid, account);
	}
	public Job getJob() {
		return new JobDAO().getJobById(this.getJobid());
	}

}

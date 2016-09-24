package tw.jiangsir.ZeroJiaowu.Objects;

import java.sql.Timestamp;
import java.util.Date;
import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.Persistent;
import tw.jiangsir.ZeroJiaowu.DAOs.CourseDAO;
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
	@Persistent(name = "selectedid")
	private Integer selectedid = 0;
	// @Persistent(name = "course1")
	// private String course1 = "";
	// @Persistent(name = "course2")
	// private String course2 = "";
	// @Persistent(name = "course3")
	// private String course3 = "";
	// @Persistent(name = "course4")
	// private String course4 = "";
	// @Persistent(name = "selected")
	// private String selected = "";
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

	// public String getSelected() {
	// return selected;
	// }
	//
	// public void setSelected(String selected) {
	// this.selected = selected;
	// }

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

	public Integer getCourseid1() {
		return courseid1;
	}

	public void setCourseid1(Integer courseid1) {
		this.courseid1 = courseid1;
	}

	public Integer getCourseid2() {
		return courseid2;
	}

	public void setCourseid2(Integer courseid2) {
		this.courseid2 = courseid2;
	}

	public Integer getCourseid3() {
		return courseid3;
	}

	public void setCourseid3(Integer courseid3) {
		this.courseid3 = courseid3;
	}

	public Integer getCourseid4() {
		return courseid4;
	}

	public void setCourseid4(Integer courseid4) {
		this.courseid4 = courseid4;
	}

	public Integer getSelectedid() {
		return selectedid;
	}

	public void setSelectedid(Integer selectedid) {
		this.selectedid = selectedid;
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

	public Course getCourse1() {
		return new CourseDAO().getCourseById(this.getCourseid1());
	}
	public Course getCourse2() {
		return new CourseDAO().getCourseById(this.getCourseid2());
	}
	public Course getCourse3() {
		return new CourseDAO().getCourseById(this.getCourseid3());
	}
	public Course getCourse4() {
		return new CourseDAO().getCourseById(this.getCourseid4());
	}
	public Course getSelected() {
		return new CourseDAO().getCourseById(this.getSelectedid());
	}

	public String getResult() {
		String text = "恭喜您完成志願選填作業！<br>";
		text += "如有疑問請將本畫面列印下來，並洽教務處。<br><br>";
		text += "您已經於 " + Utils.parseDatetime(this.getSubmittime().getTime()) + " 完成選填作業！(#" + this.getId() + ")<br>";
		text += "第一志願： " + this.getCourse1().getName() + "<br>";
		if (this.getJob().getMax_choose() >= 2) {
			text += "第二志願： " + this.getCourse2().getName() + "<br>";
		}
		if (this.getJob().getMax_choose() >= 3) {
			text += "第三志願： " + this.getCourse3().getName() + "<br>";
		}
		if (this.getJob().getMax_choose() >= 4) {
			text += "第四志願： " + this.getCourse4().getName() + "<br>";
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

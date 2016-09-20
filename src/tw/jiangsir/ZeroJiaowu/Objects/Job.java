package tw.jiangsir.ZeroJiaowu.Objects;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.Persistent;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.jiangsir.ZeroJiaowu.DAOs.CourseDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.ElectiveDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.JobDAO;
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
public class Job {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "semester")
	private Integer semester = 0;
	@Persistent(name = "title")
	private String title = "";
	@Persistent(name = "content")
	private String content = "";
	@Persistent(name = "allowedusers")
	private String allowedusers = ".*";
	@Persistent(name = "ipset")
	private LinkedHashSet<IpAddress> ipset = new LinkedHashSet<IpAddress>();
	@Persistent(name = "max_choose")
	private Integer max_choose = 4;
	@Persistent(name = "visible")
	private Boolean visible = true;
	@Persistent(name = "starttime")
	private Timestamp starttime = new Timestamp(System.currentTimeMillis());
	@Persistent(name = "finishtime")
	private Timestamp finishtime = new Timestamp(System.currentTimeMillis());

	// =============================================================================
	private CurrentUser currentUser = null;

	public Job() {

	}

	// public Job(Integer id) {
	// String sql = "SELECT * FROM jobs WHERE id=" + id;
	// ArrayList<?> list = new DataBase().executeQuery(sql);
	// if (list.size() == 0) {
	// return;
	// }
	// this.init((HashMap<?, ?>) list.get(0));
	// }
	//
	// public Job(HashMap<?, ?> map) {
	// if (map == null) {
	// return;
	// }
	// this.init(map);
	// }
	//
	// private void init(Map<?, ?> map) {
	// this.setId((Integer) map.get("id"));
	// this.setSemester((Integer) map.get("semester"));
	// this.setTitle((String) map.get("title"));
	// this.setContent((String) map.get("content"));
	// this.setAllowedusers((String) map.get("allowedusers"));
	// this.setMax_choose((Integer) map.get("max_choose"));
	// this.setVisible((Integer) map.get("visible"));
	// this.setStarttime((Date) map.get("starttime"));
	// this.setFinishtime((Date) map.get("finishtime"));
	// }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAllowedusers() {
		return allowedusers;
	}

	public void setAllowedusers(String allowedusers) {
		this.allowedusers = allowedusers;
	}

	public LinkedHashSet<IpAddress> getIpset() {
		return ipset;
	}

	public void setIpset(LinkedHashSet<IpAddress> ipset) {
		this.ipset = ipset;
	}

	public void setIpset(String ipset) {
		if (ipset == null) {
			return;
		}
		this.ipset = StringTool.String2IpAddressSet(ipset);
	}

	public Integer getMax_choose() {
		return max_choose;
	}

	public void setMax_choose(Integer maxChoose) {
		max_choose = maxChoose;
	}

	public Boolean getIsVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Timestamp getStarttime() {
		return starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public void setStarttime(String starttime) {
		try {
			this.setStarttime(Timestamp.valueOf(starttime));
		} catch (IllegalArgumentException e) {
		}
	}

	public Timestamp getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Timestamp finishtime) {
		this.finishtime = finishtime;
	}

	public void setFinishtime(String finishtime) {
		try {
			this.setFinishtime(Timestamp.valueOf(finishtime));
		} catch (IllegalArgumentException e) {
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	// ===================================================================
	public CurrentUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isAllowedUser() {
		return Utils.isAllowed(this.getAllowedusers(), currentUser.getAccount());
	}

	public ArrayList<Elective> getFinishElectives() {
		return new ElectiveDAO().getElectivesByJobid(this.getId());
	}

	public Hashtable<String, User> getAllowedUsers() {
		return new JobDAO().getAllowedUsers(this.getId());
	}

	public Hashtable<String, User> getNonSubmitUsers() {
		Hashtable<String, User> allusers = this.getAllowedUsers();
		for (Elective elective : this.getFinishElectives()) {
			allusers.remove(elective.getAccount());
		}
		// Iterator<?> it = this.getFinishElectives().iterator();
		// while (it.hasNext()) {
		// allusers.remove(((Elective) it.next()).getAccount());
		// // students.add(new User(((Elective) it.next()).getUseraccount()));
		// }
		return allusers;
	}

	public boolean getIsRunning() {
		if (this.getIsVisible() && starttime.before(new Date()) && finishtime.after(new Date())) {
			return true;
		}
		return false;
	}

	/**
	 * 取得某 job 內的所有 courses
	 * 
	 * @return
	 */
	public ArrayList<Course> getCourses() {
		return new CourseDAO().getCoursesByJobid(this.getId());
	}

	public String getStatus() {
		Timestamp now = new Timestamp(System.currentTimeMillis());

		if (now.before(this.getStarttime())) {
			return "準備中...";
		} else if (now.after(starttime) && now.before(finishtime)) {
			return "進行中...";
		} else {
			return "已結束...";
		}

	}

}

package tw.jiangsir.ZeroJiaowu.Objects;

import java.util.ArrayList;
import tw.jiangsir.Utils.Persistent;
import tw.jiangsir.ZeroJiaowu.DAOs.CourseDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.ElectiveDAO;

/**
 *  - User.java
 * 2008/4/29 下午 05:41:54
 * jiangsir
 */

/**
 * @author jiangsir
 * 
 */
public class Course {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "jobid")
	private Integer jobid = 0;
	public static final String TYPE_SENIOR = "senior";
	public static final String TYPE_JUNIOR = "junior";
	public static final String TYPE_CLUB = "club";
	@Persistent(name = "type")
	private String type = "";
	@Persistent(name = "name")
	private String name = "";
	@Persistent(name = "teacher")
	private String teacher = "";
	@Persistent(name = "capacity")
	private Integer capacity = 0;
	@Persistent(name = "content")
	private String content = "";

	// ============================================================
	// private boolean isFull = false;

	public Course() {

	}

	// public Course(Integer id) {
	// String sql = "SELECT * FROM courses WHERE id=" + id;
	// ArrayList<?> list = new DataBase().executeQuery(sql);
	// if (list.size() == 0) {
	// return;
	// }
	// this.init((HashMap<?, ?>) list.get(0));
	// }
	//
	// public Course(Integer jobid, String type, String name) {
	// String sql = "SELECT * FROM courses WHERE jobid=" + jobid
	// + " AND type='" + type + "' AND name='" + name + "'";
	// ArrayList<?> list = new DataBase().executeQuery(sql);
	// if (list.size() == 0) {
	// return;
	// }
	// this.init((HashMap<?, ?>) list.get(0));
	// }
	//
	// public Course(HashMap map) {
	// if (map == null) {
	// return;
	// }
	// this.init(map);
	// }
	//
	// private void init(Map<?, ?> map) {
	// this.setId((Integer) map.get("id"));
	// this.setJobid((Integer) map.get("jobid"));
	// this.setType((String) map.get("type"));
	// this.setName((String) map.get("name"));
	// this.setTeacher(((String) map.get("teacher")));
	// this.setCapacity((Integer) map.get("capacity"));
	// this.setContent((String) map.get("content"));
	// }

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			return;
		}
		name = name.replaceAll(":", "：");
		name = name.replaceAll("/", "｜");
		this.name = name;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isFull() {
		return this.getElectives().size() <= this.getCapacity() ? false : true;
	}

	// public void setFull(boolean isFull) {
	// this.isFull = isFull;
	// }

	// =====================================================================

	/**
	 * 取出在選中本課程的學生
	 */
	public ArrayList<Elective> getElectives() {
		return new ElectiveDAO().getElectivesBySelectedJobid(this.getName(), this.getJobid());
	}

	/**
	 * 依據志願順序來撈學生, 並且依照指定順序來取
	 * 
	 * @return
	 */
	private ArrayList<Elective> getnthElectives(int nth, String orderby) {
		return new ElectiveDAO().getnthElectives(this.getName(), this.getJobid(), nth, orderby);
		// ArrayList<Elective> electives = new ArrayList<Elective>();
		// String sql = "SELECT * FROM electives WHERE course" + nth + "='"
		// + this.getName() + "' AND selected='' AND jobid="
		// + this.getJobid() + " " + orderby;
		// Iterator<?> it = new DataBase().executeQuery(sql).iterator();
		// while (it.hasNext()) {
		// electives.add(new Elective((HashMap<?, ?>) it.next()));
		// }
		// return electives;
	}

	/**
	 * 進行分發。將選定的課程，寫入到 Elective 的 selected 欄位當中
	 */
	public void doFenfa(int nth) {
		ArrayList<Elective> electives = this.getElectives();
		// String orderby = "ORDER BY submittime ASC";
		// String orderby = "ORDER BY SECOND(submittime) ASC";
		String orderby = "ORDER BY RAND() ASC";
		ArrayList<Elective> nthelectives = this.getnthElectives(nth, orderby);
		System.out.println("electives.size=" + electives.size());
		System.out.println("nthelectives.size=" + nthelectives.size());
		ElectiveDAO electiveDao = new ElectiveDAO();
		if (electives.size() + nthelectives.size() <= this.getCapacity()) { // 人數還不足時
			// Iterator<Elective> it = nthelectives.iterator();
			// while (it.hasNext()) {
			// Elective elective = (Elective) it.next();
			// elective.UpdateSelected(this.getName());
			// elective.UpdateNth(nth);
			// }
			for (Elective elective : nthelectives) {
				electiveDao.UpdateSelected(elective.getId(), this.getName());
				electiveDao.UpdateNth(elective.getId(), nth);
			}
		} else { // 超過人數時
			// 在同一個志願順序的前提下，依照登錄順序來決定，應該改成其他方式來決定哪些人選中
			for (int i = 0; i < this.getCapacity() - electives.size(); i++) {
				electiveDao.UpdateSelected(nthelectives.get(i).getId(), this.getName());
				electiveDao.UpdateNth(nthelectives.get(i).getId(), nth);
				// nthelectives.get(i).UpdateSelected(this.getName());
				// nthelectives.get(i).UpdateNth(nth);
			}
		}
	}

	/**
	 * 取得已經選上的學生
	 * 
	 * @return
	 */
	public ArrayList<User> getStudents() {
		ArrayList<User> students = new ArrayList<User>();
		for (Elective elective : this.getElectives()) {
			students.add(elective.getUser());
		}
		// Iterator<?> it = new Course(this.courseid).getElectives().iterator();
		// while (it.hasNext()) {
		// students.add(new User(((Elective) it.next()).getUseraccount()));
		// }
		return students;
	}

	/**
	 * 讀取同一學期裡的所有課程
	 * 
	 * @return
	 */
	public ArrayList<Course> getCourses() {
		return new CourseDAO().getCoursesByJobid(jobid);
	}

}

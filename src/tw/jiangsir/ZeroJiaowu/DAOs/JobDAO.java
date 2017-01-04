/**
 * idv.jiangsir.DAOs - UserDAO.java
 * 2008/4/29 下午 05:46:51
 * jiangsir
 */
package tw.jiangsir.ZeroJiaowu.DAOs;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import tw.jiangsir.Utils.SuperDAO;
import tw.jiangsir.ZeroJiaowu.Objects.Course;
import tw.jiangsir.ZeroJiaowu.Objects.Elective;
import tw.jiangsir.ZeroJiaowu.Objects.Job;
import tw.jiangsir.ZeroJiaowu.Objects.User;
import tw.jiangsir.ZeroJiaowu.utils.Utils;

/**
 * @author jiangsir
 * 
 */
public class JobDAO extends SuperDAO<Job> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see idv.jiangsir.DAOs.IUserDAO#insert(idv.jiangsir.Objects.User)
	 */
	public synchronized int insert(Job job) throws SQLException {
		String sql = "INSERT INTO jobs (semester, title, content, allowedusers, ipset, max_choose, "
				+ "visible, starttime, finishtime) VALUES" + "(?,?,?,?,?, ?,?,?,?);";
		int id = 0;
		PreparedStatement pstmt = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstmt.setInt(1, job.getSemester());
		pstmt.setString(2, job.getTitle());
		pstmt.setString(3, job.getContent());
		pstmt.setString(4, job.getAllowedusers());
		pstmt.setString(5, job.getIpset().toString());
		pstmt.setInt(6, job.getMax_choose());
		pstmt.setBoolean(7, job.getIsVisible());
		pstmt.setTimestamp(8, new Timestamp(job.getStarttime().getTime()));
		pstmt.setTimestamp(9, new Timestamp(job.getFinishtime().getTime()));
		this.executeUpdate(pstmt);
		ResultSet rs = pstmt.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		rs.close();
		pstmt.close();
		return id;

	}

	public synchronized int update(Job job) throws SQLException {
		// String sql = "UPDATE jobs SET semester=" + job.getSemester()
		// + ", title='" + job.getTitle() + "', content='"
		// + job.getContent() + "', allowedusers='"
		// + job.getAllowedusers() + "', max_choose="
		// + job.getMax_choose() + ", visible=" + job.getVisible()
		// + ", starttime='"
		// + Utils.parseDatetime(job.getStarttime().getTime())
		// + "', finishtime='"
		// + Utils.parseDatetime(job.getFinishtime().getTime())
		// + "' WHERE id=" + job.getId();
		// new DataBase().executeUpdate(sql);
		String sql = "UPDATE jobs SET semester=?, title=?, content=?, allowedusers=?, ipset=?,"
				+ " max_choose=?, visible=?, starttime=?, finishtime=? WHERE id=?";
		int result = -1;
		PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
		pstmt.setInt(1, job.getSemester());
		pstmt.setString(2, job.getTitle());
		pstmt.setString(3, job.getContent());
		pstmt.setString(4, job.getAllowedusers());
		pstmt.setString(5, job.getIpset().toString());
		pstmt.setInt(6, job.getMax_choose());
		pstmt.setBoolean(7, job.getIsVisible());
		pstmt.setTimestamp(8, new Timestamp(job.getStarttime().getTime()));
		pstmt.setTimestamp(9, new Timestamp(job.getFinishtime().getTime()));
		pstmt.setInt(10, job.getId());
		result = this.executeUpdate(pstmt);
		pstmt.close();
		return result;

	}

	// /**
	// * 由身份去判定目前可以進行的 job , 目前暫時只允許一個 job , 不可以同時兩個 job 給同一個人同時進行 <br>
	// * 如果沒有任何一個可以操作的 job 則回傳 0
	// *
	// * @return
	// */
	// public int getVisibleJobid_OLD(String useraccount) {
	//
	// String sql = "SELECT * FROM jobs WHERE visible=" + Job.visible_TRUE;
	// Iterator<?> it = new DataBase().executeQuery(sql).iterator();
	// while (it.hasNext()) {
	// Job job = new Job((HashMap<?, ?>) it.next());
	// String[] allowedusers = job.getAllowedusers().split(",");
	// for (int i = allowedusers.length - 1; i >= 0; i--) {
	// if (useraccount.matches(allowedusers[i].trim())) {
	// return job.getId();
	// }
	// }
	// }
	// return 0;
	// }

	/**
	 * visible job 允許一個以上，國中、高中就要同時進行了。<br>
	 * 既已有了開始及結束時間，為何還需要有 visibleJob?<br>
	 * > 該課程如果指定為 visibleJob 則使用者進來後可以看到相關的說明，比如 選填作業開始之前可以先提示<br>
	 * > 選填作業結束後進來可以看到自己填的志願, 以及提示選填已經結束。<br>
	 * > starttime, finishtime 只純粹表示開始與結束
	 * 
	 * @param useraccount
	 * @return
	 */
	public Hashtable<Integer, Job> getVisibleJobs(String useraccount) {
		String sql = "SELECT * FROM jobs WHERE visible=true" + " ORDER BY finishtime DESC";
		Hashtable<Integer, Job> jobs = new Hashtable<Integer, Job>();
		for (Job job : this.executeQuery(sql, Job.class)) {
			String[] allowedusers = job.getAllowedusers().split(",");
			for (int i = allowedusers.length - 1; i >= 0; i--) {
				System.out.println("useraccount=" + useraccount + " matches " + allowedusers[i].trim() + " !? ");
				if (useraccount.matches(allowedusers[i].trim())) {
					System.out.println("true");
					jobs.put(job.getId(), job);
				}
			}
		}
		// Iterator<?> it = new DataBase().executeQuery(sql).iterator();
		// while (it.hasNext()) {
		// Job job = new Job((HashMap<?, ?>) it.next());
		// }
		return jobs;
	}

	/**
	 * visible job 允許一個以上，國中、高中就要同時進行了。<br>
	 * 既已有了開始及結束時間，為何還需要有 visibleJob?<br>
	 * > 該課程如果指定為 visibleJob 則使用者進來後可以看到相關的說明，比如 選填作業開始之前可以先提示<br>
	 * > 選填作業結束後進來可以看到自己填的志願, 以及提示選填已經結束。<br>
	 * > starttime, finishtime 只純粹表示開始與結束
	 * 
	 * @param useraccount
	 * @return
	 */
	public Hashtable<Integer, Job> getRunningJobs_OLD() {
		String sql = "SELECT * FROM jobs WHERE visible=true"
				+ " AND starttime<NOW() AND finishtime>NOW() ORDER BY finishtime DESC";
		Hashtable<Integer, Job> jobs = new Hashtable<Integer, Job>();
		for (Job job : this.executeQuery(sql, Job.class)) {
			String[] allowedusers = job.getAllowedusers().split(",");
			for (int i = allowedusers.length - 1; i >= 0; i--) {
				jobs.put(job.getId(), job);
			}
		}
		// Iterator<?> it = new DataBase().executeQuery(sql).iterator();
		// while (it.hasNext()) {
		// Job job = new Job((HashMap<?, ?>) it.next());
		// String[] allowedusers = job.getAllowedusers().split(",");
		// for (int i = allowedusers.length - 1; i >= 0; i--) {
		// jobs.put(job.getId(), job);
		// }
		// }
		return jobs;
	}

	public ArrayList<Job> getRunningJobs() {
		String sql = "SELECT * FROM jobs WHERE visible=true"
				+ " AND starttime<NOW() AND finishtime>NOW() ORDER BY finishtime DESC";
		return this.executeQuery(sql, Job.class);
	}

	public Hashtable<Integer, Job> getSuspendingJobs_OLD() {
		String sql = "SELECT * FROM jobs WHERE visible=true" + " AND starttime>NOW() ORDER BY finishtime DESC";
		Hashtable<Integer, Job> jobs = new Hashtable<Integer, Job>();
		for (Job job : this.executeQuery(sql, Job.class)) {
			jobs.put(job.getId(), job);
		}
		// Iterator<?> it = new DataBase().executeQuery(sql).iterator();
		// Hashtable<Integer, Job> jobs = new Hashtable<Integer, Job>();
		// while (it.hasNext()) {
		// Job job = new Job((HashMap<?, ?>) it.next());
		// jobs.put(job.getId(), job);
		// }
		return jobs;
	}

	public ArrayList<Job> getSuspendingJobs() {
		String sql = "SELECT * FROM jobs WHERE visible=true" + " AND starttime>NOW() ORDER BY finishtime DESC";
		return this.executeQuery(sql, Job.class);
	}

	public ArrayList<Job> getJobs() {
		String sql = "SELECT * FROM jobs ORDER BY id DESC";
		return this.executeQuery(sql, Job.class);
	}

	/**
	 * 取得該 job 所允許的所有 user
	 * 
	 * @return
	 */
	public Hashtable<String, User> getAllowedUsers(int jobid) {
		// String rules = new Job(jobid).getAllowedusers();
		String rules = this.getJobById(jobid).getAllowedusers();
		Hashtable<String, User> result = new Hashtable<String, User>();
		for (User user : new UserDAO().getUsers()) {
			if (Utils.isAllowed(rules, user.getAccount())) {
				result.put(user.getAccount(), user);
			}
		}
		// String sql = "SELECT * FROM users";
		// Iterator<?> it = new DataBase().executeQuery(sql).iterator();
		// while (it.hasNext()) {
		// User user = new User((HashMap<?, ?>) it.next());
		// if (Utils.isAllowed(rules, user.getUseraccount())) {
		// result.put(user.getUseraccount(), user);
		// }
		// }
		return result;
	}

	@Override
	public boolean delete(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	public Job getJobById(int jobid) {
		String sql = "SELECT * FROM jobs WHERE id=" + jobid;
		for (Job job : this.executeQuery(sql, Job.class)) {
			return job;
		}
		return null;
	}

	/**
	 * 匯出 exam 的 applications 到 xls 檔
	 * 
	 * @return
	 * @throws IOException
	 */
	public void exportResults_XLS(int jobid, OutputStream out) throws IOException {
		Job job = new JobDAO().getJobById(jobid);
		HSSFWorkbook workbook = new HSSFWorkbook();// 建立一個Excel活頁簿
		for (Course course : job.getCourses()) {
			String sheetname = course.getName() + "(" + course.getElectives().size() + "|" + course.getCapacity() + ")";
			sheetname = sheetname.replaceAll(":", "：");
			sheetname = sheetname.replaceAll("\\\\", "|");
			sheetname = sheetname.replaceAll("\\*", "＊");
			sheetname = sheetname.replaceAll("\\?", "？");
			sheetname = sheetname.replaceAll("/", "／");
			sheetname = sheetname.replaceAll("\\[", "「");
			sheetname = sheetname.replaceAll("\\]", "」");

			HSSFSheet sheet = workbook.createSheet(sheetname); // 在活頁簿中建立一個Sheet
			HSSFRow row = sheet.createRow(0);
			int col = 0;
			row.createCell(col++).setCellValue("志願序");
			row.createCell(col++).setCellValue("選中課程");
			row.createCell(col++).setCellValue("學號");
			row.createCell(col++).setCellValue("姓名");
			row.createCell(col++).setCellValue("班級座號");
			row.createCell(col++).setCellValue("第一志願");
			row.createCell(col++).setCellValue("第二志願");
			row.createCell(col++).setCellValue("第三志願");
			row.createCell(col++).setCellValue("第四志願");

			int rindex = 0;
			for (Elective elective : course.getElectives()) {
				row = sheet.createRow(++rindex);
				int colindex = 0;
				row.createCell(colindex++).setCellValue(elective.getNth());
				row.createCell(colindex++).setCellValue(elective.getSelected().getName());
				row.createCell(colindex++).setCellValue(elective.getAccount());
				row.createCell(colindex++).setCellValue(elective.getUser().getUsername());
				row.createCell(colindex++)
						.setCellValue(elective.getUser().getComment() + elective.getUser().getNumber());
				row.createCell(colindex++).setCellValue(elective.getCourse1().getName());
				if (elective.getJob().getMax_choose() >= 2) {
					row.createCell(colindex++).setCellValue(elective.getCourse2().getName());
				}
				if (elective.getJob().getMax_choose() >= 3) {
					row.createCell(colindex++).setCellValue(elective.getCourse3().getName());
				}
				if (elective.getJob().getMax_choose() >= 4) {
					row.createCell(colindex++).setCellValue(elective.getCourse4().getName());
				}
			}
		}
		HSSFSheet sheet = workbook.createSheet("無法分發名單"); // 在活頁簿中建立一個Sheet
		HSSFRow row = sheet.createRow(0);
		int col = 0;
		row.createCell(col++).setCellValue("學號");
		row.createCell(col++).setCellValue("姓名");
		row.createCell(col++).setCellValue("班級座號");
		row.createCell(col++).setCellValue("第一志願");
		row.createCell(col++).setCellValue("第二志願");
		row.createCell(col++).setCellValue("第三志願");
		row.createCell(col++).setCellValue("第四志願");

		int rindex = 0;
		for (Elective elective : new ElectiveDAO().NonFenfaedElectives(jobid)) {
			row = sheet.createRow(++rindex);
			int colindex = 0;
			row.createCell(colindex++).setCellValue(elective.getAccount());
			row.createCell(colindex++).setCellValue(elective.getUser().getUsername());
			row.createCell(colindex++).setCellValue(elective.getUser().getComment() + elective.getUser().getNumber());
			row.createCell(colindex++).setCellValue(elective.getCourse1().getName());
			if (elective.getJob().getMax_choose() >= 2) {
				row.createCell(colindex++).setCellValue(elective.getCourse2().getName());
			}
			if (elective.getJob().getMax_choose() >= 3) {
				row.createCell(colindex++).setCellValue(elective.getCourse3().getName());
			}
			if (elective.getJob().getMax_choose() >= 4) {
				row.createCell(colindex++).setCellValue(elective.getCourse4().getName());
			}
		}

		HSSFSheet sheet1 = workbook.createSheet("未上網填報"); // 在活頁簿中建立一個Sheet
		HSSFRow row1 = sheet1.createRow(0);
		int col1 = 0;
		row1.createCell(col1++).setCellValue("學號");
		row1.createCell(col1++).setCellValue("姓名");
		row1.createCell(col1++).setCellValue("班級座號");

		int rindex1 = 0;
		for (User user : job.getNonSubmitUsers().values()) {
			row1 = sheet1.createRow(++rindex1);
			int colindex = 0;
			row1.createCell(colindex++).setCellValue(user.getAccount());
			row1.createCell(colindex++).setCellValue(user.getUsername());
			row1.createCell(colindex++).setCellValue(user.getComment() + user.getNumber());
		}

		// byte[] bytes = workbook.getBytes();
		// workbook.close();
		// return bytes;
		workbook.write(out);
		workbook.close();
	}

}

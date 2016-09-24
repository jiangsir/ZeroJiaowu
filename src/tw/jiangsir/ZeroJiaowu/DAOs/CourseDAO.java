package tw.jiangsir.ZeroJiaowu.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import tw.jiangsir.Utils.SuperDAO;
import tw.jiangsir.ZeroJiaowu.Objects.*;

public class CourseDAO extends SuperDAO<Course> {

	public CourseDAO() {

	}

	public synchronized int insert(Course course) throws SQLException {
		String sql = "INSERT INTO courses (jobid, name, capacity, teacher, content) VALUES" + "(?,?,?,?,?);";
		int id = 0;
		PreparedStatement pstmt = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstmt.setInt(1, course.getJobid());
		pstmt.setString(2, course.getName());
		pstmt.setInt(3, course.getCapacity());
		pstmt.setString(4, course.getTeacher());
		pstmt.setString(5, course.getContent());
		this.executeUpdate(pstmt);
		ResultSet rs = pstmt.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		rs.close();
		pstmt.close();
		return id;
	}

	public boolean delete(int courseid) {
		String sql = "DELETE FROM courses WHERE id=" + courseid;
		return this.execute(sql);
	}

	public synchronized int update(Course course) throws SQLException {
		String sql = "UPDATE courses SET jobid=?, name=?, capacity=?, teacher=?, content=? WHERE id=?";
		int result = -1;
		PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
		pstmt.setInt(1, course.getJobid());
		pstmt.setString(2, course.getName());
		pstmt.setInt(3, course.getCapacity());
		pstmt.setString(4, course.getTeacher());
		pstmt.setString(5, course.getContent());
		pstmt.setInt(6, course.getId());
		result = this.executeUpdate(pstmt);
		pstmt.close();
		return result;
	}

	public ArrayList<Course> getCoursesByJobid(Integer jobid) {
		String sql = "SELECT * FROM courses WHERE jobid=" + jobid + " ORDER BY id ASC";
		return this.executeQuery(sql, Course.class);
	}

	public Course getCourseById(int courseid) {
		String sql = "SELECT * FROM courses WHERE id=" + courseid + " ORDER BY id ASC";
		for (Course course : this.executeQuery(sql, Course.class)) {
			return course;
		}
		return null;
	}

	public void clearFenfa(Integer jobid) {
		new ElectiveDAO().cleanFenfa(jobid);
	}

	public void doFenfa(Integer jobid) {
		for (int i = 0; i < 4; i++) {
			for (Course course : this.getCoursesByJobid(jobid)) {
				if (!course.isFull()) {
					course.doFenfa(i + 1); // 先做第一輪
				}
			}
			// Iterator<Course> it = this.getCoursesByJobid(jobid).iterator();
			// while (it.hasNext()) {
			// Course course = it.next();
			// if (!course.isFull()) {
			// course.doFenfa(i + 1); // 先做第一輪
			// }
			// }
		}
	}

}

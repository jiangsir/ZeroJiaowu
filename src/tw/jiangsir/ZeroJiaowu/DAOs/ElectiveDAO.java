package tw.jiangsir.ZeroJiaowu.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import tw.jiangsir.Utils.SuperDAO;
import tw.jiangsir.ZeroJiaowu.Objects.Elective;
import tw.jiangsir.ZeroJiaowu.utils.Utils;

public class ElectiveDAO extends SuperDAO<Elective> {

	public ElectiveDAO() {

	}

	public int insert(Elective elective) throws SQLException {
		// String sql = "INSERT INTO electives (jobid, useraccount, "
		// + "course1, course2, course3, course4, selected, "
		// + "nth, `lock`, submittime, ipfrom) VALUES("
		// + elective.getJobid()
		// + ", '"
		// + elective.getUseraccount()
		// + "', '"
		// + elective.getCourse1()
		// + "', '"
		// + elective.getCourse2()
		// + "', '"
		// + elective.getCourse3()
		// + "', '"
		// + elective.getCourse4()
		// + "', '"
		// + elective.getSelected()
		// + "', "
		// + elective.getNth()
		// + ", "
		// + elective.getLock()
		// + ", '"
		// + Utils.parseDatetime(elective.getSubmittime().getTime())
		// + "', '" + elective.getIpfrom() + "')";
		String sql = "INSERT INTO electives (jobid, account, courseid1, courseid2, "
				+ "courseid3, courseid4, selectedid, nth, `lock`, submittime, ipfrom) "
				+ "VALUES(?,?,?,?,?, ?,?,?,?,?, ?);";
		int id = 0;
		PreparedStatement pstmt = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstmt.setInt(1, elective.getJobid());
		pstmt.setString(2, elective.getAccount());
		pstmt.setInt(3, elective.getCourseid1());
		pstmt.setInt(4, elective.getCourseid2());
		pstmt.setInt(5, elective.getCourseid3());
		pstmt.setInt(6, elective.getCourseid4());
		pstmt.setInt(7, elective.getSelectedid());
		pstmt.setInt(8, elective.getNth());
		pstmt.setInt(9, elective.getLock());
		pstmt.setTimestamp(10, new Timestamp(elective.getSubmittime().getTime()));
		pstmt.setString(11, elective.getIpfrom());
		this.executeUpdate(pstmt);
		ResultSet rs = pstmt.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		rs.close();
		pstmt.close();
		return id;

	}

	public int update(Elective elective) throws SQLException {
		// String sql = "UPDATE electives SET course1='" + elective.getCourse1()
		// + "', course2='" + elective.getCourse2() + "', course3='"
		// + elective.getCourse3() + "', course4='"
		// + elective.getCourse4() + "', selected='"
		// + elective.getSelected() + "', nth=" + elective.getNth()
		// + ", `lock`=" + elective.getLock() + ", submittime='"
		// + Utils.parseDatetime(elective.getSubmittime().getTime())
		// + "', ipfrom='" + elective.getIpfrom()
		// + "' WHERE useraccount='" + elective.getUseraccount()
		// + "' AND jobid=" + elective.getJobid();
		String sql = "UPDATE electives SET courseid1=?, courseid2=?, courseid3=?, courseid4=?, selectedid=?, "
				+ "nth=?, `lock`=?, submittime=?, ipfrom=? WHERE account=? AND jobid=?";
		int result = -1;
		PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
		pstmt.setInt(1, elective.getCourseid1());
		pstmt.setInt(2, elective.getCourseid2());
		pstmt.setInt(3, elective.getCourseid3());
		pstmt.setInt(4, elective.getCourseid4());
		pstmt.setInt(5, elective.getSelectedid());
		pstmt.setInt(6, elective.getNth());
		pstmt.setInt(7, elective.getLock());
		pstmt.setTimestamp(8, new Timestamp(elective.getSubmittime().getTime()));
		pstmt.setString(9, elective.getIpfrom());
		pstmt.setString(10, elective.getAccount());
		pstmt.setInt(11, elective.getJobid());
		result = this.executeUpdate(pstmt);
		pstmt.close();
		return result;
	}

	public ArrayList<Elective> getElectivesByJobid(int jobid) {
		String sql = "SELECT * FROM electives WHERE jobid=" + jobid;
		return this.executeQuery(sql, Elective.class);
	}

	/**
	 * 沒有任何刪除的需要！
	 * 
	 * @deprecated
	 * @param jobid
	 */
	private void deleteElectives(int jobid) {
		String sql = "DELETE FROM electives WHERE jobid=" + jobid;
		this.execute(sql);
	}

	private String printElective(Elective elective) {
		String text = "";
		text += "id=" + elective.getId();
		text += "jobid=" + elective.getJobid() + "\n";
		text += "account=" + elective.getAccount() + "\n";
		text += "course1=" + elective.getCourse1().getName() + "\n";
		text += "course2=" + elective.getCourse2().getName() + "\n";
		text += "course3=" + elective.getCourse3().getName() + "\n";
		text += "course4=" + elective.getCourse4().getName() + "\n";
		text += "submittime=" + Utils.parseDatetime(elective.getSubmittime().getTime()) + "\n";
		return text;
	}

	/**
	 * 是否已經選填完畢
	 * 
	 * @return
	 */
	// public boolean isFinish(int jobid, String useraccount) {
	// String sql = "SELECT * FROM electives WHERE jobid=" + jobid
	// + " AND useraccount='" + useraccount + "'";
	// return new DataBase().executeQuery(sql).size() > 0 ? true : false;
	// }

	/**
	 * 取得某人的 elective
	 * 
	 * @return
	 */
	public Elective getElectiveByJobidAccount(int jobid, String account) {
		String sql = "SELECT * FROM electives WHERE jobid=" + jobid + " AND account='" + account + "'";
		// if (new DataBase().executeQuery(sql).size() == 0) {
		// return null;
		// }
		// return new Elective(new DataBase().executeQuery(sql).get(0));
		for (Elective elective : this.executeQuery(sql, Elective.class)) {
			return elective;
		}
		return null;
	}

	/**
	 * 用 id 取得一個 elective
	 * 
	 * @return
	 */
	public Elective getElectiveById(int id) {
		String sql = "SELECT * FROM electives WHERE id=" + id;
		for (Elective elective : this.executeQuery(sql, Elective.class)) {
			return elective;
		}
		return null;
	}

	/**
	 * 是否是保障名額，舊生優先留下
	 * 
	 * @return
	 */
	public boolean isReserved(int jobid, String account) {
		String sql = "SELECT * FROM electives WHERE courseid1=0 AND "
				+ "courseid2=0 AND courseid3=0 AND courseid4=0 AND selectedid!=0 AND jobid=" + jobid + " AND account='"
				+ account + "'";
		return this.executeCount(sql) > 0 ? true : false;
	}

	@Override
	public boolean delete(int id) {
		String sql = "DELETE FROM electives WHERE id=" + id;
		return this.execute(sql);
	}

	/**
	 * 依據志願順序來撈學生, 並且依照指定順序來取
	 * 
	 * @return
	 */
	public ArrayList<Elective> getnthElectives(int courseid, int jobid, int nth, String orderby) {
		String sql = "SELECT * FROM electives WHERE courseid" + nth + "=" + courseid + " AND selectedid=0 AND jobid="
				+ jobid + " " + orderby;
		return this.executeQuery(sql, Elective.class);
	}

	public ArrayList<Elective> getElectivesBySelectedJobid(int selectedid, int jobid) {
		String sql = "SELECT * FROM electives WHERE selectedid=" + selectedid + " AND jobid=" + jobid
				+ " ORDER BY nth, submittime ASC";
		return this.executeQuery(sql, Elective.class);
	}

	/**
	 * 已經做了選填動作的人，但尚未分發
	 * 
	 * @return
	 */
	public ArrayList<Elective> SubmittedElectives(int jobid) {
		String sql = "SELECT * FROM electives WHERE courseid1!=0 "
				+ "AND courseid2!=0 AND courseid3!=0 AND courseid4!=0 AND jobid=" + jobid + " ORDER BY submittime DESC";
		return this.executeQuery(sql, Elective.class);
	}

	/**
	 * 尚未分發的人
	 * 
	 * @param jobid
	 * @return
	 */
	public ArrayList<Elective> NonFenfaedElectives(int jobid) {
		String sql = "SELECT * FROM electives WHERE selectedid=0 AND jobid=" + jobid;
		return this.executeQuery(sql, Elective.class);
	}

	/**
	 * 舊生保留名額
	 * 
	 * @param jobid
	 * @return
	 */
	public ArrayList<Elective> reservedSelections(int jobid) {
		String sql = "SELECT * FROM electives WHERE `lock`=" + Elective.LOCK_LOCKED + " AND jobid=" + jobid;
		return this.executeQuery(sql, Elective.class);
	}

	/**
	 * 清除某 jobid 的分發結果。
	 * 
	 * @param jobid
	 */
	public void cleanFenfa(int jobid) {
		String sql = "UPDATE electives SET selectedid=0 WHERE `lock`!=" + Elective.LOCK_LOCKED + " AND jobid=" + jobid;
		this.execute(sql);
	}

	public void UpdateNth(int jobid, int nth) {
		String sql = "UPDATE electives SET nth=" + nth + " WHERE id=" + jobid;
		try {
			this.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void UpdateSelected(int jobid, int courseid) {
		String sql = "UPDATE electives SET selectedid=" + courseid + " WHERE id=" + jobid;
		try {
			this.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

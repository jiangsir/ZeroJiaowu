package tw.jiangsir.ZeroJiaowu.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import tw.jiangsir.Utils.SuperDAO;
import tw.jiangsir.ZeroJiaowu.Objects.Loginlog;

public class LoginlogDAO extends SuperDAO<Loginlog> {

	public LoginlogDAO() {

	}

	public int insert(Loginlog loginlog) throws SQLException {
		// String sql = "INSERT INTO loginlogs (userid, account, ipaddr, "
		// + "message, logintime, logouttime)VALUES('"
		// + loginlog.getUserid() + "', '" + loginlog.getAccount()
		// + "', '" + loginlog.getIpaddr() + "', '"
		// + loginlog.getMessage() + "', '"
		// + Utils.parseDatetime(loginlog.getLogintime().getTime())
		// + "', '"
		// + Utils.parseDatetime(loginlog.getLogouttime().getTime())
		// + "')";
		// return this.insert(sql);

		String sql = "INSERT INTO loginlogs (userid, account, ipaddr, "
				+ "message, logintime, logouttime) VALUES" + "(?,?,?,?,?, ?);";
		int id = 0;
		PreparedStatement pstmt = this.getConnection().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstmt.setInt(1, loginlog.getUserid());
		pstmt.setString(2, loginlog.getAccount());
		pstmt.setString(3, loginlog.getIpaddr());
		pstmt.setString(4, loginlog.getMessage());
		pstmt.setTimestamp(5, new Timestamp(loginlog.getLogintime().getTime()));
		pstmt.setTimestamp(6,
				new Timestamp(loginlog.getLogouttime().getTime()));
		this.executeUpdate(pstmt);
		ResultSet rs = pstmt.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		rs.close();
		pstmt.close();
		return id;

	}

	@Override
	public int update(Loginlog t) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean delete(int i) {
		// TODO Auto-generated method stub
		return false;
	}
}

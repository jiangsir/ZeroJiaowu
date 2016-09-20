/**
 * idv.jiangsir.DAOs - UserDAO.java
 * 2008/4/29 下午 05:46:51
 * jiangsir
 */
package tw.jiangsir.ZeroJiaowu.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

import tw.jiangsir.Utils.SuperDAO;
import tw.jiangsir.ZeroJiaowu.Objects.User;
import tw.jiangsir.ZeroJiaowu.Objects.User.ROLE;

/**
 * @author jiangsir
 * 
 */
public class UserDAO extends SuperDAO<User> {

	@Override
	public synchronized int insert(User user) throws SQLException {
		// "INSERT INTO users (useraccount, username, passwd, "
		// "sessionid, number, comment, usergroup)
		String sql = "INSERT INTO users (account, username, passwd, number, comment, role) " + "VALUES (?,?,?,?,?,?);";
		int id = 0;
		PreparedStatement pstmt = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, user.getAccount());
		pstmt.setString(2, user.getUsername());
		pstmt.setString(3, user.getPasswd());
		pstmt.setInt(4, user.getNumber());
		pstmt.setString(5, user.getComment());
		pstmt.setString(6, user.getRole().name());
		this.executeUpdate(pstmt);
		ResultSet rs = pstmt.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		rs.close();
		pstmt.close();
		return id;
	}

	/**
	 * 取得所有 user
	 * 
	 * @return
	 */
	public ArrayList<User> getUsers() {
		String sql = "SELECT * FROM users";
		return this.executeQuery(sql, User.class);
	}

	public synchronized int update(User user) throws SQLException {
		// String sql = "UPDATE users SET username='" + user.getUsername()
		// + "', passwd='" + user.getPasswd() + "', sessionid='"
		// + user.getSessionid() + "', number='" + user.getNumber()
		// + "', comment='" + user.getComment() + "', usergroup='"
		// + user.getUsergroup() + "'" + " WHERE useraccount='"
		// + user.getUseraccount() + "'";

		String sql = "UPDATE users SET username=?, passwd=?, number=?, comment=?, role=? WHERE id=?";
		int result = -1;
		PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
		pstmt.setString(1, user.getUsername());
		pstmt.setString(2, user.getPasswd());
		pstmt.setInt(3, user.getNumber());
		pstmt.setString(4, user.getComment());
		pstmt.setString(5, user.getRole().name());
		pstmt.setInt(6, user.getId());
		result = this.executeUpdate(pstmt);
		pstmt.close();
		return result;

	}

	public int getSeniorCount() {
		String sql = "SELECT * FROM users WHERE LENGTH(account)=6";
		return this.executeCount(sql);
	}

	public int getJuniorCount() {
		String sql = "SELECT * FROM users WHERE LENGTH(account)=7";
		return this.executeCount(sql);
	}

	public User getUserById(int userid) {
		String sql = "SELECT * FROM users WHERE id=" + userid;
		for (User user : this.executeQuery(sql, User.class)) {
			return user;
		}
		return null;
	}

	public User getUserByAccount(String account) {
		String sql = "SELECT * FROM users WHERE account='" + account + "'";
		for (User user : this.executeQuery(sql, User.class)) {
			return user;
		}
		return null;
	}

	/**
	 * 用 account, passwd 取得 User, 找不到的話，則回傳 NullUser
	 * 
	 * @param account
	 * @param passwd
	 * @return
	 */
	public User getUserByAccountPasswd(String account, String passwd) {
		String sql = "SELECT * FROM users WHERE account=? AND passwd=?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, account);
			pstmt.setString(2, passwd);
			for (User user : this.executeQuery(pstmt, User.class)) {
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// return UserFactory.getNullUser();
		return null;
	}

	@Override
	public boolean delete(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getCountByAllUsers() {
		// String sql = "SELECT * FROM users";
		return this.executeCount("users", new TreeMap<String, Object>());
	}

	/**
	 * 插入預設的 user
	 */
	public void insertInitUsers() {
		if (this.getCountByAllUsers() > 0) {
			return;
		}
		User user = new User();
		user.setAccount("admin");
		user.setPasswd("DBPASSWORD");
		user.setUsername("管理員");
		user.setRole(ROLE.DEBUGGER);
		try {
			this.insert(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

package tw.jiangsir.ZeroJiaowu.utils;

import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.sql.DataSource;

import tw.jiangsir.Utils.ENV;
import tw.jiangsir.ZeroJiaowu.DAOs.ExceptionDAO;

public class DataBase {

	public static Connection conn = null;

	private ServletContext sc = null;

	public String SQLexception = null;

	private String JDBC = null;

	private DataSource ds = null;

	/**
	 * 
	 * @param config
	 */
	public DataBase(Connection conn) {
		DataBase.conn = conn;
	}

	/**
	 * 只有 ContextListener 能夠呼叫
	 * 
	 * @param event
	 */
	public DataBase(ServletContextEvent event) {
		this.sc = event.getServletContext();
	}

	public DataBase(ServletContext context) {
		this.sc = context;
	}

	public DataBase() {
		this.sc = ENV.context;
	}

	/**
	 * 供 Local application 使用
	 * 
	 * @param driver
	 *            Example: com.mysql.jdbc.Driver
	 * @param jdbc
	 *            Example: jdbc:mysql://localhost:3306/myDB
	 * @param dbaccount
	 * @param dbpasswd
	 */
	public DataBase(String driver, String jdbc, String dbaccount,
			String dbpasswd) {
		// //連結驅動程式
		try {
			// example String driver = "com.mysql.jdbc.Driver"; // 設定驅動程式
			Class.forName(driver); // 連結驅動程式
			// example String jdbc = "jdbc:mysql://localhost:3306/myDB"; //
			System.out.println("Local conn = " + DataBase.conn);
			DataBase.conn = DriverManager.getConnection(jdbc, dbaccount,
					dbpasswd);
			System.out.println("Local conn = " + DataBase.conn);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 連結資料庫
	}

	/**
	 * 處理進入資料庫的字串資料
	 * 
	 * @param text
	 * @return
	 */
	public static String parseString_NODEFAULT(String defaulttext, String text,
			int MAX_LENGTH) {
		String result = defaulttext;
		if (text != null) {
			text = Utils.intoSQL(text);
			if (text.length() > MAX_LENGTH) {
				result = text.substring(0, MAX_LENGTH);
			} else {
				result = text;
			}
		}
		return result;
	}

	public static String parseString(String text, int MAX_LENGTH) {
		text = Utils.intoSQL(text);
		// 20090423 getBytes 在計算中文時會有誤差，導致 getBytes.length >= MAX_LENGTH 成立
		// 但是實際上進行 substring 的時候卻過長，導致 exception
		// if (text.getBytes().length >= MAX_LENGTH) {
		if (text.length() >= MAX_LENGTH) {
			text = text.substring(0, MAX_LENGTH);
		}
		return text;
	}

	public static Integer parseInteger_NODEFAULT(Integer defaulttext,
			String text) {
		Integer result = defaulttext;
		if (text != null && text.matches("[0-9]+")) {
			result = Integer.valueOf(text);
		}
		return result;
	}

	public static Long parseLong(Long defaulttext, String text) {
		Long result = defaulttext;
		if (text != null && text.matches("[0-9]+")) {
			result = Long.valueOf(text);
		}
		return result;
	}

	public static Date parseDate_NOUSE(Date defaultvalue, String value) {
		Date result = defaultvalue;
		if (value != null && Utils.isLegalDatestring(value)) {
			result = ENV.datetime(value);
		}
		return result;
	}

	private String stackTrace(SQLException e, String sql) {
		StringBuffer sb = new StringBuffer(5000);
		sb.append("SQL ERROR: " + sql + "\n\n");
		StackTraceElement[] st = e.getStackTrace();
		for (int i = 0; i < st.length; i++) {
			sb.append(st[i] + "\n");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() {
		try {
			// System.out.println(ENV.logHeader() + "this.conn=" +
			// DataBase.conn);
			// System.out.println("this.conn.isClosed()="
			// + DataBase.conn.isClosed());
			if (DataBase.conn != null && !DataBase.conn.isClosed()) {
				return DataBase.conn;
				// } else if
				// (config.getServletContext().getInitParameter("conn") != null)
				// {
				// System.out.println(ENV.logHeader() + "資料庫連結失效， case 1");
				// return (Connection) config.getServletContext().getAttribute(
				// "conn");
				// } else if (ENV.context != null) {
				// System.out.println(ENV.logHeader()
				// + "資料庫連結失效，到 initConnection 重抓 case 2");
				// this.initConnection(ENV.context);
				// return conn;
			} else {
				System.out.println(ENV.logHeader()
						+ "資料庫連結失效，到 initConnection 重抓");
				// this.initConnection(config.getServletContext());
				this.initConnection(ENV.context);
				return conn;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 由 Initialized Listener 來取得 connection <br>
	 * 資料庫在應用程式初始化中放入 connection
	 * 
	 * @param event
	 * @return
	 */
	public void initConnection(ServletContext context) {
		// /* qx
		// * String strDriver = "com.mysql.jdbc.Driver"; // 設定驅動程式 String strDB
		// =
		// * "jdbc:mysql://localhost:3306/myDB"; // 設定資料庫的位置，myDB 要改成您的資料庫名稱，
		// * String strDBAccount = "root"; // 資料庫帳號 3306 則是 MySQL 的預設 Port
		// String
		// * strDBPassword = "1234"; // 資料庫密碼 Class.forName(strDriver); //連結驅動程式
		// * Connection con = DriverManager.getConnection(strDB, strDBAccount,
		// * strDBPassword); // 連結資料庫
		// */
		if (context == null) {
			context = this.sc;
		}
		// String JDBC = context.getInitParameter("JDBC");
		String JDBC = "mysql";

		try {
			if (ds == null) {
				InitialContext icontext = new InitialContext();
				ds = (DataSource) icontext.lookup("java:comp/env/" + JDBC);
				try {
					DataBase.conn = ds.getConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				synchronized (context) {
					context.setAttribute("conn", DataBase.conn);
					// ENV.setConnection(this.conn);
					System.out.println(ENV.logHeader() + "取得conn並放入 "
							+ "ServletContext & ENV. conn=" + DataBase.conn);
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public HashMap getObject(ResultSet rs) {
		ResultSetMetaData rsmd;
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		try {
			rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				hashmap.put(rsmd.getColumnName(i), rs.getObject(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashmap;
	}

	/**
	 * 執行 SQL 查詢 SELECT
	 * 
	 * @param sql
	 * @return 回報一個 ArrayList 的 iterator
	 * @throws Exception
	 */
	public ArrayList<HashMap> executeQuery(String sql) {
		long starttime = System.currentTimeMillis();
		ArrayList<HashMap> list = new ArrayList<HashMap>();
		Connection conn = this.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				list.add(map);
			}
			rs.close();
			stmt.close();
			// conn.close();
		} catch (SQLException e) {
			this.SQLexception = e.getLocalizedMessage();
			System.out.println(ENV.logHeader() + "SQL ERROR:" + sql);
			new ExceptionDAO().insert("DataBase.java", "unknown", "unknown IP",
					e.getLocalizedMessage(), this.stackTrace(e, sql));
			e.printStackTrace();
			return new ArrayList<HashMap>();
		}
		System.out.println(ENV.logHeader() + "SQL=" + sql + ", 共耗時 "
				+ (System.currentTimeMillis() - starttime) + " ms");
		return list;
	}

	/**
	 * 執行 SQL 指令 DELETE
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean execute(String sql) {
		boolean result = false;
		Connection conn = this.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			result = stmt.execute(sql);
			stmt.close();
			// conn.close();
		} catch (SQLException e) {
			this.SQLexception = e.getLocalizedMessage();
			System.out.println(ENV.logHeader() + "SQL ERROR:" + sql);
			new ExceptionDAO().insert("DataBase.java", "unknown", "unknown IP",
					e.getLocalizedMessage(), this.stackTrace(e, sql));
			e.printStackTrace();
			return false;
		}
		return result;
	}

	/**
	 * UPDATE 專用
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public synchronized int executeUpdate(String sql) {
		int result = 0;
		Connection conn = this.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			stmt.close();
			// conn.close();
		} catch (SQLException e) {
			this.SQLexception = e.getLocalizedMessage();
			System.out.println(ENV.logHeader() + "SQL ERROR:" + sql);
			new ExceptionDAO().insert("DataBase.java", "unknown", "unknown IP",
					e.getLocalizedMessage(), this.stackTrace(e, sql));
			e.printStackTrace();
		}
		System.out.println(ENV.logHeader() + "SQL=" + sql);
		return result;
	}

	/**
	 * 專門提供 INSERT 使用, 並回傳新增最後一筆的 ID
	 * 
	 * @return
	 */
	public synchronized int executeInsert(String sql) {
		Connection conn = this.getConnection();
		Statement stmt = null;
		int result = 0;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();
				do {
					for (int i = 1; i <= colCount; i++) {
						String key = rs.getString(i);
						result = Integer.parseInt(key);
					}
				} while (rs.next());
			} else {
				System.out.println(ENV.logHeader() + "錯誤!! 沒有找到 自動遞增的 key");
				result = -1;
			}
		} catch (SQLException e) {
			this.SQLexception = e.getLocalizedMessage();
			System.out.println(ENV.logHeader() + "SQL ERROR:" + sql);
			new ExceptionDAO().insert("DataBase.java", "unknown", "unknown IP",
					e.getLocalizedMessage(), this.stackTrace(e, sql));
			e.printStackTrace();
			return -1;
		}
		return result;
	}

	/**
	 * 所傳入的 sql 必須是 SELECT COUNT(*) AS COUNT ...才會被接受 <br>
	 * 只用來獲取總筆數
	 * 
	 * @param sql
	 * @return
	 */
	public int executeCount(String sql) {
		// 原始 sql 裡如果含有 GROUP BY 的，這個方式只能得到第一筆資料的 COUNT,
		// 而無法得到總筆數，SchoolRankList 為一例
		Connection conn = this.getConnection();
		Statement stmt = null;
		int result = 0;
		if (sql.matches("^SELECT.+FROM.*")) {
			sql = sql.replaceFirst("^SELECT.+FROM",
					"SELECT COUNT(*) AS COUNT FROM");
		} else {
			return -1;
		}
		try {
			stmt = conn.createStatement();
			if (sql.contains("ORDER")) {
				sql = sql.substring(0, sql.indexOf("ORDER") - 1);
			}
			if (!sql.toUpperCase().startsWith("SELECT COUNT(*) AS COUNT FROM")) {
				return -1;
			}
			// System.out.println("executeCount sql=" + sql);
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			result = rs.getInt("COUNT");
			rs.close();
			stmt.close();
			// conn.close();
		} catch (SQLException e) {
			this.SQLexception = e.getLocalizedMessage();
			System.out.println(ENV.logHeader() + "SQL ERROR:" + sql);
			new ExceptionDAO().insert("DataBase.java", "unknown", "unknown IP",
					e.getLocalizedMessage(), this.stackTrace(e, sql));
			e.printStackTrace();
			return -1;
		}
		return result;
	}

	/**
	 * 取得某 table 的總筆數, 做分頁時使用
	 * 
	 * @deprecated
	 * @param table
	 * @return
	 */
	public int getCountbyTable(String table) {
		Connection conn = this.getConnection();
		Statement stmt = null;
		int result = 0;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM "
					+ table);
			rs.next();
			result = rs.getInt("count");
			rs.close();
			stmt.close();
			// conn.close();
		} catch (SQLException e) {
			this.SQLexception = e.getLocalizedMessage();
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @deprecated
	 * @param config
	 * @return
	 */
	@SuppressWarnings("unused")
	private Connection getConnection(ServletConfig config) {
		DataSource ds = null;
		ServletContext sc = null;
		Connection conn = null;
		if (config != null) { // qx 代表由 servlet 呼叫, 具備 serveltconfig
			sc = config.getServletContext();
			conn = (Connection) sc.getAttribute("conn");
			if (conn != null) {
				return conn;
			}
			System.out.println(ENV.logHeader() + "conn 不存在, 新增!!");
			this.JDBC = sc.getInitParameter("JDBC");
		} else { // qx 由 POJO 呼叫, 沒有 servletconfig 時, 沒有辦法取得 connection
			this.JDBC = "mysql";
			return null;
		}

		try {
			InitialContext context = new InitialContext();
			ds = (DataSource) context.lookup("java:comp/env/" + JDBC);
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			this.SQLexception = e.getLocalizedMessage();
			e.printStackTrace();
		}
		synchronized (sc) {
			sc.setAttribute("conn", conn);
			System.out.println(ENV.logHeader()
					+ "conn 不存在, 重新取得conn並放入 context attribute = " + conn);
		}
		return conn;
	}
}

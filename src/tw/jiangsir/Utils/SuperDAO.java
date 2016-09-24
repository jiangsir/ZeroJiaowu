package tw.jiangsir.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import tw.jiangsir.Utils.Exceptions.DataException;

abstract public class SuperDAO<T> {
	private static Connection conn = null;
	private static DataSource source = null;

	abstract public int insert(T t) throws SQLException;

	abstract public int update(T t) throws SQLException;

	abstract public boolean delete(int i);

	Logger logger = Logger.getLogger(this.getClass().getName());
	HashMap<String, Field> fields = new HashMap<String, Field>();
	int PAGESIZE = 20;

	public Connection getConnection() {
		try {
			if (conn == null || conn.isClosed()) {
				if (source == null) {
					InitialContext icontext = new InitialContext();
					source = (DataSource) icontext.lookup("java:comp/env/mysql");
				}
				// logger.info("資料庫連結不存在或關閉了。嘗試重新取得連線");
				conn = source.getConnection();
			} else {
				return conn;
			}
		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataException("資料庫連線有誤，請通知管理員！");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException("資料庫連線有誤，請通知管理員！");
		}
		return conn;
	}

	public void setConnection(String driver, String jdbc, String dbaccount, String dbpasswd) {
		try {
			Class.forName(driver); // 連結驅動程式
			SuperDAO.conn = DriverManager.getConnection(jdbc, dbaccount, dbpasswd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getSetMethodName(String columnName) {
		String firstchar = columnName.substring(0, 1);
		firstchar = firstchar.toUpperCase();
		return "set" + firstchar + columnName.substring(1);
	}

	/**
	 * 取得某個 class 內某個 Persistent name 的 Field
	 * 
	 * @param theclass
	 * @return
	 */
	private Field getField(Class<T> theclass, String persistentname) {
		if (this.fields.size() == 0) {
			for (Field field : theclass.getDeclaredFields()) {
				Persistent persistent = field.getAnnotation(Persistent.class);
				if (persistent != null) {
					fields.put(persistent.name(), field);
				}
			}
		}
		return fields.get(persistentname);
	}

	public ArrayList<T> executeQueryByAnnotations(PreparedStatement pstmt, Class<T> clazz) {
		// FIXME 用 annotation 來存取有點問題。比如 getSolutions 系列。通常只 query 一個
		// solutionid,
		// 然後才再用 solutionid 去取完整的 solution. 但是如果只有一個 solutionid, annotation 仍然需要
		// 掃描過所有的 field
		// FIXME 20130326 另一個問題是，因為 executeQuery 為了 Downcasting 所有的 theclass 都是用
		// xxxxxxBean, 但是 Persistent annotation 都是宣告在 xxxxx 裡面。導致這裡抓不到
		// annotation 的訊息。
		long starttime = System.currentTimeMillis();
		ResultSet rs = null;
		ArrayList<T> list = new ArrayList<T>(); // 準備用 ArrayList 來回傳查詢結果。
		try {
			rs = pstmt.executeQuery(); // 執行查詢
			ResultSetMetaData rsmd = rs.getMetaData(); // 取得資料表的 metadata
			int columnCount = rsmd.getColumnCount(); // 有幾個 column
			String columnName;
			Field field;
			Object value;
			while (rs.next()) {
				T t = (T) clazz.newInstance(); // 實體化一個 T
				for (int i = 1; i <= columnCount; i++) {
					columnName = rsmd.getColumnName(i);

					field = this.getField(clazz, columnName);
					if (field == null) {
						// logger.info("資料庫欄位 columnName=" + columnName
						// + " 找不到相對應的 Field, Class name="
						// + clazz.getName());
						continue;
					}
					if (rsmd.getColumnType(i) == Types.BOOLEAN || rsmd.getColumnType(i) == Types.TINYINT) {
						value = rs.getBoolean(columnName);
					} else if (rsmd.getColumnType(i) == Types.INTEGER) {
						value = rs.getInt(columnName);
					} else if (rsmd.getColumnType(i) == Types.BIGINT) {
						value = rs.getLong(columnName);
					} else if (rsmd.getColumnType(i) == Types.LONGVARBINARY) {
						continue;
					} else if (rsmd.getColumnType(i) == Types.BLOB) {
						continue;
					} else {
						value = rs.getObject(columnName);
					}

					try {
						String settername = "set" + field.getName().toUpperCase().substring(0, 1)
								+ field.getName().substring(1);
						Method settermethod = clazz.getMethod(settername, new Class[]{value.getClass()});
						settermethod.invoke(t, new Object[]{value});
					} catch (SecurityException e) {
						e.printStackTrace();
						continue;
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						continue;
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
				list.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			System.out.println(
					"PSTMT_SQL=" + pstmt.toString() + " 共耗時 " + (System.currentTimeMillis() - starttime) + " ms");
			// logger.info("PSTMT_SQL=" + pstmt.toString() + " 共耗時 "
			// + (System.currentTimeMillis() - starttime) + " ms");
			try {
				if (rs != null) {
					rs.close();
				}
				pstmt.close();
				// conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public ArrayList<T> executeQuery(PreparedStatement pstmt, Class<T> theclass) {
		long starttime = System.currentTimeMillis();
		ResultSet rs = null;
		ArrayList<T> list = new ArrayList<T>(); // 準備用 ArrayList 來回傳查詢結果。
		try {
			rs = pstmt.executeQuery(); // 執行查詢
			ResultSetMetaData rsmd = rs.getMetaData(); // 取得資料表的 metadata
			int columnCount = rsmd.getColumnCount(); // 有幾個 column
			while (rs.next()) {
				T t = (T) theclass.newInstance(); // 實體化一個 T
				for (int i = 1; i <= columnCount; i++) {
					String settername = this.getSetMethodName(rsmd.getColumnName(i));
					Object value;
					if (rsmd.getColumnType(i) == Types.BOOLEAN || rsmd.getColumnType(i) == Types.TINYINT) {
						value = rs.getBoolean(rsmd.getColumnName(i));
					} else if (rsmd.getColumnType(i) == Types.LONGVARBINARY) {
						continue;
					} else if (rsmd.getColumnType(i) == Types.BLOB) {
						continue;
						// } else if (rsmd.getColumnType(i) == Types.DATE) {
						// value = rs.getDate(rsmd.getColumnName(i));
					} else {
						value = rs.getObject(rsmd.getColumnName(i));
					}

					try {
						Method m = t.getClass().getMethod(settername, new Class[]{value.getClass()});
						m.invoke(t, new Object[]{value});
					} catch (InvocationTargetException e1) {
						e1.printStackTrace();
						continue;
					} catch (NoSuchMethodException e1) {
						e1.printStackTrace();
						continue;
					}
				}
				list.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			System.out.println(
					"PSTMT_SQL=" + pstmt.toString() + " 共耗時 " + (System.currentTimeMillis() - starttime) + " ms");
			// logger.info("PSTMT_SQL=" + pstmt.toString() + " 共耗時 "
			// + (System.currentTimeMillis() - starttime) + " ms");
			try {
				rs.close();
				pstmt.close();
				// conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 僅回傳該 table 的第一個欄位，並且回傳 long, 通常第一個欄位都是一個 table 的 id.
	 * 
	 * @param sql
	 * @return
	 */
	public ArrayList<Long> executeQueryId(String sql) {
		long starttime = System.currentTimeMillis();
		ResultSet rs = null;
		PreparedStatement pstmt;
		ArrayList<Long> list = new ArrayList<Long>();
		try {
			pstmt = this.getConnection().prepareStatement(sql);
			rs = pstmt.executeQuery(); // 執行查詢
			ResultSetMetaData rsmd = rs.getMetaData(); // 取得資料表的 metadata
			while (rs.next()) {
				String idname = rsmd.getColumnName(1);
				Long id = rs.getLong(idname);
				list.add(id);
			}
			// System.out.println("PSTMT_SQL=" + pstmt.toString() + " 共耗時 "
			// + (System.currentTimeMillis() - starttime) + " ms");
			logger.info("PSTMT_SQL=" + pstmt.toString() + " 共耗時 " + (System.currentTimeMillis() - starttime) + " ms");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	protected ArrayList<T> executeQuery(String sql, Class<T> theclass) {
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			// ArrayList<T> t = this.executeQuery(pstmt, theclass);
			ArrayList<T> t = this.executeQueryByAnnotations(pstmt, theclass);
			return t;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}

	public synchronized int executeUpdate(PreparedStatement pstmt) throws SQLException {
		long starttime = System.currentTimeMillis();

		int result = -1;
		// try {
		result = pstmt.executeUpdate();
		// pstmt.close(); // 不能在這裡 close, 後面還需要用 pstmt 來取得 key
		System.out
				.println("PSTMT_SQL=" + pstmt.toString() + " 共耗時 " + (System.currentTimeMillis() - starttime) + " ms");
		// } catch (SQLException e) {
		// logger.warning("SQL_ERROR=" + pstmt.toString());
		// e.printStackTrace();
		//
		// }
		return result;
	}

	/**
	 * 執行 SQL 指令
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean execute(String sql) {
		long starttime = System.currentTimeMillis();
		boolean result = false;
		Connection conn = this.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			result = stmt.execute(sql);
			System.out.println(
					"PSTMT_SQL=" + stmt.toString() + " 共耗時 " + (System.currentTimeMillis() - starttime) + " ms");
			// logger.info("PSTMT_SQL=" + stmt.toString() + " 共耗時 "
			// + (System.currentTimeMillis() - starttime) + " ms");
			stmt.close();
			// conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return result;
	}

	/**
	 * 只用來獲取總筆數
	 * 
	 * @param sql
	 * @return
	 */
	public int executeCount(String sql) {
		int result = 0;
		if (sql.matches("^SELECT.+FROM.*")) {
			sql = sql.replaceFirst("^SELECT.+FROM", "SELECT COUNT(*) AS COUNT FROM");
		} else {
			return -1;
		}
		try {
			PreparedStatement pstmt;
			pstmt = this.getConnection().prepareStatement(sql);
			if (sql.contains("ORDER")) {
				sql = sql.substring(0, sql.indexOf("ORDER") - 1);
			}
			if (!sql.toUpperCase().startsWith("SELECT COUNT(*) AS COUNT FROM")) {
				return -1;
			}
			System.out.println(pstmt.toString());
			ResultSet rs = pstmt.executeQuery(sql);
			rs.next();
			result = rs.getInt("COUNT");
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return result;
	}

	/**
	 * 新的 executeCount 不再去更改原始 sql 字串，而是直接製作 count 語法，並利用 fields 來加入條件。
	 * 
	 * @param tablename
	 * @param fields
	 * @return
	 */
	public int executeCount(String tablename, TreeMap<String, Object> fields) {
		long starttime = System.currentTimeMillis();
		String sql = "SELECT COUNT(*) AS COUNT FROM " + tablename + this.makeFields(fields, null, 0);
		int count = 0;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			int i = 1;
			if (fields != null)
				for (String key : fields.keySet()) {
					pstmt.setObject(i++, fields.get(key));
				}

			ResultSet rs = pstmt.executeQuery();
			logger.info(this.getClass().getSimpleName() + ".executeCount()=" + pstmt.toString() + " 共耗時 "
					+ (System.currentTimeMillis() - starttime) + " ms");
			rs.next();
			count = rs.getInt("COUNT");
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return count;
	}

	/**
	 * 將 fields 組合成 sql 語法。 <br>
	 * 注意：只有 WHERE 以後的部分進行組合。
	 * 
	 * @param fields
	 * @param orderby
	 * @param page
	 * @return
	 */
	public String makeFields(TreeMap<String, Object> fields, String orderby, int page) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append(" ");
		if (fields != null) {
			Iterator<String> keys = fields.keySet().iterator();
			if (keys.hasNext()) {
				sql.append("WHERE ");
			}
			while (keys.hasNext()) {
				sql.append(" (" + keys.next() + "=?)");
				if (keys.hasNext()) {
					sql.append(" AND ");
				}
			}
		}
		if (orderby != null && !"".equals(orderby.trim())) {
			sql.append(" ORDER BY " + orderby);
		}
		// 不論 page 是多少，都必須 LIMIT ?? 為什麼？會導致 UserStatistic 裏面的題目列表都只有 20 個。
		if (page > 0) {
			sql.append(" LIMIT " + ((page > 1 ? page : 1) - 1) * PAGESIZE + "," + PAGESIZE);
		}
		// System.out.println("makeFields=" + sql.toString());
		return sql.toString();
	}

}

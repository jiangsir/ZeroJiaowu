package tw.jiangsir.ZeroJiaowu.DAOs;

import java.util.ArrayList;

import tw.jiangsir.Utils.ENV;
import tw.jiangsir.ZeroJiaowu.utils.DataBase;
import tw.jiangsir.ZeroJiaowu.utils.Utils;

public class ExceptionDAO {

	public ExceptionDAO() {

	}

	public int getCount() {
		String SQL = "SELECT * FROM exceptions";
		return new DataBase().executeCount(SQL);
	}

	public ArrayList getErrors(int count) {
		String SQL = "SELECT * FROM exceptions ORDER BY id DESC LIMIT 0,"
				+ count;
		return new DataBase().executeQuery(SQL);
	}

	public ArrayList getErrorsByIP(String ip) {
		String SQL = "SELECT * FROM exceptions WHERE ipaddr='" + ip
				+ "' ORDER BY id DESC";
		return new DataBase().executeQuery(SQL);
	}

	public int insert(String uri, String account, String ipaddr,
			String exceptiontype, String exception) {
		if (exception.contains("INSERT INTO exceptions")) {
			return 0;
		}
		uri = Utils.intoSQL(uri);
		if (uri != null && uri.length() > 100) {
			uri = uri.substring(uri.length() - 100);
		}
		if (exceptiontype != null && exceptiontype.length() > 200) {
			exceptiontype = exceptiontype.substring(0, 200);
			exceptiontype += "訊息太長(>200)，到此省略...";
		}
		if (exception != null && exception.length() > 100000) {
			exception = exception.substring(0, 10000);
			exception += "訊息太長(>100000)，到此省略...";
		}
		exceptiontype = Utils.intoSQL(exceptiontype);
		exception = Utils.intoSQL(exception);
		String SQL = "INSERT INTO exceptions (uri, account, ipaddr, "
				+ "exceptiontype, exception, exceptiontime) VALUES('" + uri
				+ "', '" + account + "', '" + ipaddr + "', '" + exceptiontype
				+ "', '" + exception + "', '" + ENV.getNow() + "')";
		return new DataBase().executeInsert(SQL);
	}
}

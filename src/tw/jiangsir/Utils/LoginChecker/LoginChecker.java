package tw.jiangsir.Utils.LoginChecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import tw.jiangsir.Utils.ENV;
import tw.jiangsir.Utils.Utils;
import tw.jiangsir.ZeroJiaowu.DAOs.UserDAO;

public class LoginChecker {

	private int MAX_RETRY = 10;

	/**
	 * 依 account @ authhost , passwd 判斷是否存在 DB 中
	 * 
	 * @param account
	 * @param authhost
	 * @param passwd
	 * @return
	 */
	private boolean isDBuser(String account, String passwd) {
		String sql = "SELECT * FROM users WHERE account='" + account
				+ "' AND passwd='" + passwd + "'";
		if (new UserDAO().executeCount(sql) > 0) {
			return true;
		}
		return false;
	}

	public boolean isLegalUser(String account, String password, String CurrentIP) {
		return this.isDBuser(account, password);
	}

	/**
	 * 由 POP3 server 來驗證使用者
	 * 
	 * @param pop3host
	 * @param account
	 * @param passwd
	 * @return
	 */
	public String isPOP3User(String pop3host, String account, String passwd) {
		String result = null;
		Socket popsocket = null;
		BufferedReader breader = null;
		PrintWriter pwriter = null;
		try {
			String line = null;
			int count = 0;
			do {
				do {
					popsocket = new java.net.Socket(pop3host, 110);
				} while (popsocket == null);
				breader = new BufferedReader(new InputStreamReader(
						popsocket.getInputStream()));
				pwriter = new PrintWriter(popsocket.getOutputStream());
				Thread.sleep(1000);
				line = breader.readLine();
				System.out.println(ENV.logHeader() + account + ": 嘗試讀取第 "
						+ ++count + " 次");
			} while ((line == null || line.startsWith("-ERR"))
					&& count <= this.MAX_RETRY);

			System.out.println(ENV.logHeader() + account + ": " + line);
			pwriter.println("USER " + account);
			pwriter.flush();
			line = breader.readLine();
			System.out.println(ENV.logHeader() + account + ": " + line);
			pwriter.println("PASS " + passwd);
			pwriter.flush();
			line = breader.readLine();
			System.out.println(ENV.logHeader() + account + ": " + line);
			if (line != null && line.startsWith("+OK")) {
				result = "true";
			} else if (line != null && line.startsWith("-ERR")) {
				result = "POP 密碼不正確!!!";
			} else {
				result = line;
			}
			pwriter.println("STAT");
			pwriter.flush();
			line = breader.readLine();
			System.out.println(ENV.logHeader() + account + ": " + line);
			pwriter.println("QUIT");
			pwriter.flush();
			breader.close();
			popsocket.close();
		} catch (Exception e) {
			System.out.println(ENV.logHeader() + "popsocket exception="
					+ popsocket);
			e.printStackTrace();
		}
		if (result == null) {
			result = "讀取 socket 不正常，可能為網路環境不正常, 請稍後重試!!";
		}
		System.out.println(ENV.logHeader() + account
				+ ": Login.isPOP3User.result= " + result);
		return result;
	}

	public boolean isAllowedIP(String UserIP) {
		return Utils.isSubnetwork(ENV.context.getInitParameter("AllowedIP"),
				UserIP);

	}
}

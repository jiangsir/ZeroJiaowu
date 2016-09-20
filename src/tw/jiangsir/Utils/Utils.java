package tw.jiangsir.Utils;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import tw.jiangsir.ZeroJiaowu.Objects.IpAddress;

public class Utils {

	public static ArrayList<String> getDeniedIP() {
		ContextXmlParser parser = new ContextXmlParser();
		String ips[] = parser.getContextParam("Valve", "deny").split(",");
		ArrayList<String> deniedip = new ArrayList<String>();
		for (int i = 0; i < ips.length; i++) {
			deniedip.add(ips[i].trim());
		}
		return deniedip;
	}

	/**
	 * 限制大量連線的 ip
	 * 
	 * @param ipaddr
	 */
	public synchronized static void addDenyIP(String ipaddr) {
		// TODO qx 可能有同步問題, 但如何解決？ or jdom 已處理同步問題?
		ContextXmlParser parser = new ContextXmlParser();
		String denyip = parser.getContextParam("Valve", "deny");
		denyip = Utils.treemerge(denyip, ipaddr);
		parser.setContextParam("Valve", "deny", denyip);
		parser.writeContextxml();
	}

	/**
	 * 一次進行大量連線者，進行限制
	 * 
	 * @param ip
	 */
	public static void addDenyIP_OLD(String ip) {
		String path = ENV.APP_REAL_PATH + "META-INF/";
		String filename = "context.xml";
		// String path = "./META-INF/context.xml";

		BufferedReader breader = null;
		StringBuffer text = new StringBuffer(50000);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path + filename);
			breader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		// ArrayList list = new ArrayList();
		try {
			while ((line = breader.readLine()) != null) {
				if (line.matches(".*Valve className="
						+ "\"org.apache.catalina.valves.RemoteAddrValve\""
						+ ".*")) {
					while (!line.matches(".*<\\/Valve>.*")) {
						line += breader.readLine();
					}
					System.out.println("找到 Valve 行: " + line);
					String denyip = line.substring(line.indexOf("deny=\"") + 6);
					denyip = denyip.substring(0, denyip.indexOf("\""));
					System.out.println("denyip= " + denyip);
					denyip = Utils.treemerge(denyip, ip);
					line = line.replaceAll("deny=\".*\"", "deny=\"" + denyip
							+ "\"");
					System.out.println("找到 Valve 行: " + line);
				}
				text.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String text2 = text.toString();
		try {
			text2 = new String(text2.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("path=" + path);
		Utils.createfile(path, filename, text2.toString());
		try {
			breader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String treeremove(String orig, String remove) {
		orig = orig.replaceAll(" ", "");
		remove = remove.replaceAll(" ", "");
		String[] origarray = orig.split(",");
		String[] removearray = remove.split(",");

		TreeSet<String> set = new TreeSet<String>();
		for (int i = 0; i < origarray.length; i++) {
			if (!"".equals(origarray[i].trim())) {
				set.add(origarray[i].trim());
			}
		}
		for (int i = 0; i < removearray.length; i++) {
			set.remove(removearray[i].trim());
		}
		Iterator iterator = set.iterator();
		String result = "";
		while (iterator.hasNext()) {
			if ("".equals(result)) {
				result += iterator.next();
			} else {
				result += ", " + iterator.next();
			}
		}
		return result;
	}

	/**
	 * 處理 以逗點分割的 String 的串接。以 Tree Set 實作，因此不會有重復值<br>
	 * problemid, 及 privilege 使用
	 * 
	 * @return
	 */
	public static String treemerge(String first, String second) {
		first = first.replaceAll(" ", "");
		second = second.replaceAll(" ", "");
		String[] firstarray = first.split(",");
		String[] secondarray = second.split(",");
		TreeSet<String> set = new TreeSet<String>();
		for (int i = 0; i < firstarray.length; i++) {
			if (!"".equals(firstarray[i].trim())) {
				set.add(firstarray[i].trim());
			}
		}
		for (int i = 0; i < secondarray.length; i++) {
			if (!"".equals(secondarray[i].trim())) {
				set.add(secondarray[i].trim());
			}
		}
		Iterator iterator = set.iterator();
		String result = "";
		while (iterator.hasNext()) {
			if ("".equals(result)) {
				result += iterator.next();
			} else {
				result += ", " + iterator.next();
			}
		}
		return result;
	}

	public static boolean isLegalDatestring(String datestring) {
		if (datestring == null
				|| !datestring
						.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 所有網頁上由user 輸入的 text 欄位都應該過濾, 不可含有敏感符號
	 * 
	 * @param text
	 * @return
	 */
	// 由 Parameter.islegalParameter() 取代
	public static boolean isLegalText_Deprecated20081009(String text) {
		// 以負面表列, 把在 sql 裡有特殊意義的字元排除即可
		if (text == null || text.contains("'") || text.contains("\\")
				|| text.contains(";") || text.contains("--")
				|| text.contains("#")) {
			return false;
		}
		return true;
	}

	/**
	 * 驗證 account 是否合法
	 * 
	 * @param account
	 * @return
	 */
	// 計劃由 User.parseAccount() 來取代
	public static boolean isLegalAccount_Deprecated(String account) {
		if (account != null && account.matches("[a-zA-Z]+\\w+")) {
			return true;
		}
		return false;
	}

	/**
	 * 判斷是否為 MANAGER_IP 定義在 web.xml 中
	 * 
	 * @param IP
	 */
	public static boolean isMANAGER_IP(String CurrentIP) {
		return Utils.isSubnetwork(ENV.context.getInitParameter("MANAGER_IP"),
				CurrentIP);
	}

	/**
	 * 所有要 SQL 指令都要經過處理, 以避免 SQL Injection 攻擊
	 * 
	 * @param s
	 * @return
	 */
	// 只能出現在 Dao or Object 的 SQL 語法內，以避免重復問題
	public static String intoSQL(String s) {
		if (s == null) {
			return "";
		}
		s = s.replaceAll("'", "''");
		// s = s.replaceAll("\\+", "\\\\+");
		s = s.replaceAll("\\\\", "\\\\\\\\");
		return s;
	}

	/**
	 * 從 sql 取出後, 再改回原狀才能正常顯示
	 * 
	 * @param s
	 * @return
	 */
	public static String outofSQL(String s) {
		s = s.replaceAll("''", "'");
		s = s.replaceAll("\\\\\\\\", "\\\\");
		return s;
	}

	/**
	 * 寫入資料庫的資料如果需要用 HTML 的方式顯示, 許多特殊符號就必須轉換才行
	 * 
	 * @param content
	 * @return
	 */
	public static String replaceHTML(String content) {
		content = content.replaceAll("&", "&amp;");
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		content = content.replaceAll(" ", "&nbsp;");
		content = content.replaceAll("\"", "&quot;");
		return content;
	}

	/**
	 * 判斷檔案是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isFileExist_NOTUSE(String path) {
		File f = new File(path);
		return f.exists();
	}

	public static boolean isFileExist_NOTUSE(String path, String filename) {
		if (new File(path).isDirectory() && new File(filename).isFile()) {
			File f = new File(path + filename);
			return f.exists();
		}
		return false;
	}

	/**
	 * 建立檔案 包括 測資檔 及 .cpp 檔
	 * 
	 * @param filename
	 * @param data
	 */
	public static void createfile_DEPRECATED(String filename, String data) {
		BufferedWriter outfile = null;
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			// BufferedReader breader = new BufferedReader(new
			// InputStreamReader(
			// fis, "UTF-8"));
			outfile = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			data = data.replaceAll("\n", System.getProperty("line.separator"));
			// System.out.println("data=" + data);
			outfile.write(data);
			// outfile.newLine(); // qx 換行
			outfile.flush();
			outfile.close();
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 建立檔案 包括 測資檔 及 .cpp 檔
	 * 
	 * @param filename
	 * @param data
	 */
	public static void createfile(String path, String filename, String data) {
		// TODO 20090306
		// 此處要驗證 path 及 filename 的合法性，不能出現 .. 或者允許範圍外的path
		path = path.replaceAll("\\.\\.", "");
		filename = filename.replaceAll("\\.\\.", "");
		BufferedWriter outfile = null;
		try {
			FileOutputStream fos = new FileOutputStream(path + filename);
			// BufferedReader breader = new BufferedReader(new
			// InputStreamReader(
			// fis, "UTF-8"));
			outfile = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			data = data.replaceAll("\n", System.getProperty("line.separator"));
			// System.out.println("data=" + data);
			outfile.write(data);
			// outfile.newLine(); // qx 換行
			outfile.flush();
			outfile.close();
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean renameFile(String oldpath, String newpath) {
		File f = new File(oldpath);
		return f.renameTo(new File(newpath));
	}

	/**
	 * 刪除檔案
	 * 
	 * @param path
	 */
	public static void delFile_NOTUSE(String path) {
		File f = new File(path);
		if (f.exists()) {
			System.out.println("Deleting " + f.getName() + "...");
			try {
				if (f.delete()) {
					System.out.println(ENV.logHeader() + "刪除 " + path);
				} else {
					System.out.println(ENV.logHeader() + "刪除失敗");
				}
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		}
	}

	public static void delFiles() {

	}

	/**
	 * 刪除資料夾底下的一些由 regex 指定出來的檔案
	 * 
	 * @param path
	 * @param regex
	 */
	public static void delRegexFiles(String path, String regex) {
		File file = new File(path);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			System.out.println("f.getPath()=" + file.getPath());
			for (int i = 0; i < files.length; i++) {
				delRegexFiles(files[i].getPath(), regex);
			}
		} else if (file.exists() && file.getPath().matches(regex)) {
			if (file.delete()) {
				System.out.println("刪除 " + path);
			} else {
				System.out.println(path + " 刪除失敗");
			}
		}
	}

	/**
	 * 取得指定目錄下的檔案, 不含目錄
	 */
	public static TreeSet<String> getTestfilenames(String path, String regex) {
		File file = new File(path);
		System.out.println("path=" + path + ", regex=" + regex);
		TreeSet<String> fileList = new TreeSet<String>();
		if (!file.exists()) {
			return fileList;
		}
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			String filestring = files[i].toString();
			// 先列出目錄
			if (!files[i].isDirectory() && filestring.matches(regex)) { // 是否為目錄
				String filename = filestring.substring(filestring
						.lastIndexOf(System.getProperty("file.separator")) + 1);
				fileList.add(filename);
			}
		}
		return fileList;
	}

	/**
	 * 取得指定目錄下的檔案, 不含目錄
	 */
	public static TreeMap<String, Integer> getTestfilenames_Map(String path,
			String regex) {
		File file = new File(path);
		TreeMap<String, Integer> fileList = new TreeMap<String, Integer>();
		if (!file.exists()) {
			return fileList;
		}
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			String filestring = files[i].toString();
			// 先列出目錄
			if (!files[i].isDirectory() && filestring.matches(regex)) { // 是否為目錄
				String filename = filestring.substring(filestring
						.lastIndexOf(System.getProperty("file.separator")) + 1);
				fileList.put(filename,
						Utils.readFile(path, filename).split("\\\n").length);
			}
		}
		return fileList;
	}

	public static String readFile(String path, String filename) {
		// 所有跟 readfile 有關的都要改成 path 由內部指定。外部只允許指定 filename
		filename = filename.replaceAll("\\.\\.", "");
		path = path.replaceAll("\\.\\.", "");
		if (!path.endsWith(System.getProperty("file.separator"))) {
			path = path + System.getProperty("file.separator");
		}
		int MAX_LENGTH = 1000000;
		String line = null;
		StringBuffer text = new StringBuffer(MAX_LENGTH);
		try {
			FileInputStream fis = new FileInputStream(path + filename);
			BufferedReader breader = new BufferedReader(new InputStreamReader(
					fis, "UTF-8"));
			while ((line = breader.readLine()) != null) {
				text.append(line + "\n");
				if (text.length() >= MAX_LENGTH) {
					text = new StringBuffer(20);
					text.append(new ENV().getFileLimitExceed());
					break;
					// return null;
				}
			}
			fis.close();
			breader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text.toString();
	}

	/**
	 * 回傳字串在目標陣列的位置。 若存在則回傳 < 0 的數
	 * 
	 * @param array
	 * @param s
	 * @return
	 */
	public static int indexOf(String[] array, String s) {
		if (array == null || s == null || array.length == 0 || "".equals(s)
				|| (array.length == 1 && "".equals(array[0]))) {
			return -1;
		}
		for (int i = 0; i < array.length; i++) {
			if (s.trim().equals(array[i].trim())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 回傳字串 s 是否 match 目標陣列裡的字串。 若與陣列內的字串 match 則回傳其 index
	 * 
	 * @param array
	 * @param s
	 * @return
	 */
	public static int matches(String[] array, String s) {
		if (array == null || s == null || array.length == 0) {
			return -1;
		}
		for (int i = 0; i < array.length; i++) {
			if (s.trim().matches(array[i].trim())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 判斷一個 currentIP 是否屬於 一個 network string 如：192.168.*.*, 203.1.2.3
	 * 
	 * @param networks
	 * @return
	 */
	public static boolean isSubnetwork(String networkstring, String currentIP) {
		networkstring = networkstring.trim();
		if (networkstring == null || "".equals(networkstring)) {
			return false;
		}
		if ("*".equals(networkstring) || "127.0.0.1".equals(currentIP)) {
			return true;
		}
		String[] networkarray = networkstring.split(",");
		for (int i = 0; i < networkarray.length; i++) {
			networkarray[i] = networkarray[i].trim();
			if (isSubip(networkarray[i], currentIP)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isSubip(String ip, String currentip) {
		ip = ip.trim();
		if (ip == null || "".equals(ip)) {
			return false;
		}
		if ("*".equals(ip) || "127.0.0.1".equals(currentip)) {
			return true;
		}
		String[] subip = ip.split("\\.");
		String[] curriparray = currentip.split("\\.");
		if (subip.length != 4 || curriparray.length != 4) {
			return false;
		}
		for (int i = 0; i < subip.length; i++) {
			if (!"*".equals(subip[i]) && !subip[i].equals(curriparray[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 分析 權限規則
	 * 
	 * @param rule
	 * @param string
	 * @return
	 */
	public static int parseRule(String rule, String string) {
		int result0 = 0; // allow
		int result1 = 1; // notallow
		int result2 = 2; // deny
		if (rule == null) {
			return result1;
		}

		rule = rule.replace(" ", "");
		if ("*".equals(rule)) {
			return result0;
		}
		if ("".equals(rule)) {
			return result1;
		}
		String[] rulearray = rule.split(",");
		if (Utils.indexOf(rulearray, "!" + string) >= 0) {
			return result2;
		}
		if (Utils.indexOf(rulearray, string) >= 0) {
			return result0;
		}
		if (rule.startsWith("*")) { // allow from all
			return result0;
		} else if (rule.startsWith("!*")) { // deny from all
			return result2;
		} else {
			return result1;
		}

	}

	/**
	 * 格式： yyyyMMdd
	 * 
	 * @param datestring
	 * @return
	 */
	public static Date parseDate(String datestring) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			return format.parse(datestring);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * 格式： yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datestring
	 * @return
	 */
	public static Date parseDatetime(String datestring) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(datestring);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		}
	}

	public static String parseDatetime(long timestamp) {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(
				timestamp));
	}

	public static String getNow() {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
	}

	/**
	 * 分析 privilege
	 * 
	 * @param privilege
	 * @param URI
	 * @return
	 */
	public static String parsePrivilege(String privilege, String URI) {
		URI = URI.substring(URI.lastIndexOf('/') + 1);
		if (URI.equals("")) {
			return "allowed";
		}
		switch (Utils.parseRule(privilege, URI)) {
		case 0:
			return "allowed";
		case 1:
			// return "您目前的權限無法存取本頁(" + URI + ")"; // 沒有允許存取該頁
			return "Message.NotAllow";
		case 2:
			// return "您目前的權限禁止存取本頁(!" + URI + ")"; // 明確禁止存取該頁
			return "Message.Forbidden";
		default:
			return "Unknown parseRule()";
		}
	}

	/**
	 * 讀取 URL HTML 並回傳 ArrayList
	 * 
	 * @param URL
	 * @return
	 */
	public static ArrayList readURL(String urlstring) {
		ArrayList<String> buffer = new ArrayList<String>();
		try {
			URL url = new URL(urlstring);
			URLConnection conn = url.openConnection();
			InputStream stream = conn.getInputStream();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(stream));
			String line;
			while ((line = in.readLine()) != null) {
				buffer.add(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return buffer;
		}
		return buffer;
	}

	public static String ReturnPage(HttpServletRequest request) {
		String ReturnPage = request.getParameter("ReturnPage");
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "./";
		}
		String CurrentPage = "./";
		if (ReturnPage == null || "".equals(ReturnPage)) {
			CurrentPage = (String) session.getAttribute("CurrentPage");
			if (CurrentPage == null || "".equals(CurrentPage)) {
				return "./";
			}
			return CurrentPage;
		} else {
			return ReturnPage;
		}
	}

	public static String PreviousPage(HttpSession session) {
		if (session == null) {
			return "./";
		}
		String PreviousPage = (String) session.getAttribute("PreviousPage");
		if (PreviousPage == null || "".equals(PreviousPage)) {
			String CurrentPage = (String) session.getAttribute("CurrentPage");
			if (CurrentPage == null || "".equals(CurrentPage)) {
				return "./";
			} else {
				return CurrentPage;
			}
		}
		return PreviousPage;
	}

	public static String CurrentPage(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "./";
		}
		String CurrentPage = "./";
		if (request.getParameter("CurrentPage") == null) {
			CurrentPage = (String) session.getAttribute("CurrentPage");
			if (CurrentPage == null || "".equals(CurrentPage)) {
				return "./";
			}
		} else {
			return request.getParameter("CurrentPage");
		}
		return CurrentPage;
	}

	public static String getBannerfile() {
		// MyProperties properties = new MyProperties();
		// String property = properties.getProperty("VBANNER");
		String[] VBANNERs = {
				"VBANNER_1:" + ENV.context.getInitParameter("VBANNER_1"),
				"VBANNER_2:" + ENV.context.getInitParameter("VBANNER_2"),
				"VBANNER_3:" + ENV.context.getInitParameter("VBANNER_3") };
		// if (property == null || "".equals(property)) {
		// VBANNERs = new String[0];
		// } else {
		// VBANNERs = property.split(",");
		// }
		String bannerfile = "VBANNER_DEFAULT.jsp";
		int total = 0;
		for (int i = 0; i < VBANNERs.length; i++) {
			total += Integer.parseInt(VBANNERs[i].split(":")[1]);
		}
		int rate = 0;
		double random = Math.random() * total;
		for (int i = 0; i < VBANNERs.length; i++) {
			rate = Integer.parseInt(VBANNERs[i].split(":")[1]);
			if (random > 0 && random <= rate) {
				bannerfile = VBANNERs[i].split(":")[0];
				break;
			} else {
				total -= rate;
				random = Math.random() * total;
			}
		}
		// if (Utils.isaroundNewYear()) {
		// bannerfile = "VBANNER_NewYear.jsp";
		// } else if (Utils.isHoliday()) {
		// bannerfile = "VBANNER_Holiday.jsp";
		// }
		return bannerfile;
	}

	/**
	 * 濾掉 querystring 內的 page 參數
	 * 
	 * @param querystring
	 * @return
	 */
	public static String querystingFilter(String querystring) {
		if (querystring == null || "".equals(querystring)) {
			return "";
		}
		String[] items = querystring.split("&");
		String query = "";
		for (int i = 0; i < items.length; i++) {
			if (!items[i].matches("^[Pp][Aa][Gg][Ee].*")) {
				if ("".equals(query)) {
					query += items[i];
				} else {
					query += "&" + items[i];
				}
			}
		}
		return query;
	}

	public static boolean isDigits(String s) {
		// 負的整數會 return false
		if (s == null || "".equals(s.trim())) {
			return false;
		}
		return s.matches("[\\d]+");
	}

	// /**
	// * 強迫所有 Online User logout
	// *
	// */
	// public static void forcedLogout() {
	// Hashtable OnlineUsers = (Hashtable) ENV.context
	// .getAttribute("OnlineUsers");
	// Enumeration keys = OnlineUsers.keys();
	// while (keys.hasMoreElements()) {
	// String key = (String) keys.nextElement();
	// HttpSession session = (HttpSession) OnlineUsers.get(key);
	// String session_account = (String) session
	// .getAttribute("session_account");
	// String session_usergroup = (String) session
	// .getAttribute("session_usergroup");
	// // User user = new User((String) session
	// // .getAttribute("session_account"));
	// // TODO 應該還是要全部踢出，否則後續操作容易出問題如: session 的 User 就沒辦法復原
	// if (!"GroupAdmin".equals(session_usergroup)) {
	// // 20081125 現在已經不需要發簡訊了
	// // IMessageDAO imessage = new IMessageDAO(ENV.context);
	// // imessage.sendMessage("admin", session_account, "[系統通知]
	// // 系統維護通知",
	// // "由於進行維護及更新，系統必須暫時關閉，因此必須強制將您登出。\n不便之處敬請包含。");
	// // System.out.println(ENV.logHeader() + "account= "
	// // + session_account + " was kicked out, key=" + key);
	//
	// new UserDAO().getUserByAccount(session_account).Logout(session);
	// }
	// }
	// }

	public static String printStackTrace(Exception e) {
		if (e == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer(5000);
		StackTraceElement[] st = e.getStackTrace();
		for (int i = 0; i < st.length; i++) {
			sb.append(st[i] + "\n");
		}
		return sb.toString();
	}

	/**
	 * 將 exception 的 stacktrace 做成 string, 以方便寫入 errorlog
	 * 
	 * @param e
	 * @return
	 */
	public static String printStackTrace(Throwable throwable) {
		StringBuffer sb = new StringBuffer(5000);
		StackTraceElement[] st = throwable.getStackTrace();
		for (int i = 0; i < st.length; i++) {
			sb.append(st[i] + "\n");
		}
		return sb.toString();
	}

	/**
	 * 將 exception 的 stacktrace 做成 string, 以方便寫入 errorlog
	 * 
	 * @param e
	 * @return
	 */
	public static String printStackTrace(Exception e, String sql) {
		System.out.println("ERROR_SQL: " + sql);
		StringBuffer sb = new StringBuffer(5000);
		StackTraceElement[] st = e.getStackTrace();
		for (int i = 0; i < st.length; i++) {
			sb.append(st[i] + "\n");
		}
		return sb.toString();
	}

	public ArrayList<IpAddress> getIpList(HttpServletRequest request) {
		ArrayList<IpAddress> ipSet = new ArrayList<IpAddress>();
		ipSet.add(new IpAddress(request.getRemoteAddr()));
		String forwarded = request.getHeader("x-forwarded-for");
		if (forwarded != null && !"".equals(forwarded)
				&& !"unknown".equalsIgnoreCase(forwarded)) {
			ipSet.add(new IpAddress(forwarded));
		}
		String proxy = request.getHeader("Proxy-Client-IP");
		if (proxy != null && !"".equals(proxy)
				&& !"unknown".equalsIgnoreCase(proxy)) {
			ipSet.add(new IpAddress(proxy));
		}
		String wl = request.getHeader("WL-Proxy-Client-IP");
		if (wl != null && !"".equals(wl) && !"unknown".equalsIgnoreCase(wl)) {
			ipSet.add(new IpAddress(wl));
		}
		return ipSet;
	}

	/** *************************************************************** */
	public static void main(String[] args) {
		String result = "";
		result = Utils.treeremove("abc,aab,bbc", "aaa");
		System.out.println(result);
		String querystring = "tab=tab01&pagenum=1&name=ssss";
		System.out.println(querystring);
		System.out.println(Utils.querystingFilter(querystring));
		Utils.addDenyIP("127.0.0.2");
	}
}

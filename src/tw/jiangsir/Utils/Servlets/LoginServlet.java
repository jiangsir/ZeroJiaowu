package tw.jiangsir.Utils.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.ZeroJiaowu.DAOs.CurrentUserDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.UserDAO;
import tw.jiangsir.ZeroJiaowu.Objects.*;

@WebServlet(urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("Login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String account = User.parseAccount(request.getParameter("account"));
		String passwd = Parameter.parseString(request.getParameter("passwd"));
		passwd = passwd.toUpperCase();

		User user = new UserDAO().getUserByAccountPasswd(account, passwd);
		if (user != null && !user.isNullUser()) {
			SessionScope sessionScope = new SessionScope(session);
			sessionScope.setOnlineUser(new CurrentUserDAO().getCurrentUserById(user.getId(), session));
			response.sendRedirect(request.getContextPath() + sessionScope.getHistories().get(0));
			return;
		} else {
			request.setAttribute("users", new UserDAO().getUsers());
			request.getRequestDispatcher("/Login.jsp").forward(request, response);
			return;
		}

		// if (account == null || !utils.isLegalAccount(account)) {
		// if (!User.islegalAccount(account)) {
		// Message message = new Message();
		// message.setPlainText("學號不合法");
		// HashMap<String, String> links = new HashMap<String, String>();
		// links.put("./", "重新進入");
		// message.setLinks(links);
		// request.setAttribute("message", message);
		// request.getRequestDispatcher("Message.jsp").forward(request,
		// response);
		// return;
		// }
		//
		// if (passwd == null || !Parameter.islegalParameter(passwd)) {
		// Message message = new Message();
		// message.setPlainText("登入不正確");
		// request.setAttribute("message", message);
		// request.getRequestDispatcher("Message.jsp").forward(request,
		// response);
		// return;
		// }

		// session.removeAttribute("OriginalURI");
		//
		// String CurrentIP = request.getRemoteAddr();
		// LoginChecker loginChecker = new LoginChecker();
		// // String theURI = targetURI.substring(targetURI.lastIndexOf('/') +
		// 1);
		// String CurrentPage = (String) session.getAttribute("CurrentPage");
		// if (CurrentPage == null) {
		// CurrentPage = "./";
		// }
		// CurrentPage = CurrentPage.substring(CurrentPage.lastIndexOf('/') +
		// 1);
		// // qx 如果 CurrentPage 是 Login 代表user 直接按 登入
		// if (CurrentPage != null && "Login".equals(CurrentPage)) {
		// // theURI = CurrentPage;
		// // qx target 指向 前一頁, 也就是 Login 完 跳回原來那一頁
		// Utils.PreviousPage(session);
		// }
		// // qx 舊時有 authhost 的作法
		// // String message = checker.isLegalUser(account, UserPasswd,
		// authhost,
		// // theURI, CurrentIP);
		// if (!loginChecker.isLegalUser(account, passwd, CurrentIP)) {
		// session.setAttribute("LoginMessage",
		// "學號、身份證字號驗證有誤<br>若一直無法解決，請洽教務處詢問！");
		// // qx 不能用 Dispatcher 的方式, 如果登入錯誤,會照成無線循環,無法結束
		// // 因為 method 一直維持 POST, 就會一直進來 doPost 方法
		// // 只能用 redirect 的方式, 因此 LoginMessage 就只能由 session傳遞
		// response.sendRedirect("Login");
		// return;
		// }
		// User user = new UserDAO().getUserByAccount(account);
		// CurrentUser currentUser = new CurrentUser(session, user);
		// session.setAttribute("Logintime", ENV.getNow());
		// session.setAttribute("sessionid", session.getId());
		// session.setAttribute("currentUser", currentUser);
		// // session.setAttribute("session_account", user.getAccount());
		// // session.setAttribute("session_usergroup", user.getUsergroup());
		// // session.setAttribute("session_privilege", user.getPrivilege());
		// session.setAttribute("Locale", request.getLocale().toString());
		// session.setAttribute("passed", "true");
		// response.sendRedirect(Utils.CurrentPage(request));
	}
}

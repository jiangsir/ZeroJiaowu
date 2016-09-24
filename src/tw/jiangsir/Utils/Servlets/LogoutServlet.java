package tw.jiangsir.Utils.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.Scopes.SessionScope;

@WebServlet(urlPatterns = {"/Logout"})
public class LogoutServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		// CurrentUser currentUser = SessionFactory.getCurrentUser(session);
		CurrentUser currentUser = new SessionScope(session).getCurrentUser();
		currentUser.doLogout();
		response.sendRedirect("./");
	}
}

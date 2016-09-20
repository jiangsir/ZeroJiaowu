package tw.jiangsir.ZeroJiaowu.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.ZeroJiaowu.DAOs.JobDAO;

@WebServlet(urlPatterns = {"/Index"})
public class IndexServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("runningjobs", new JobDAO().getRunningJobs());
		request.setAttribute("suspendingjobs", new JobDAO().getSuspendingJobs());
		request.getRequestDispatcher("Index.jsp").forward(request, response);
		return;

		// 預留未來可能還有其他服務
		// response.sendRedirect("Elective");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}

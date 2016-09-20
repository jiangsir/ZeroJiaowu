package tw.jiangsir.ZeroJiaowu.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.ZeroJiaowu.DAOs.JobDAO;

@WebServlet(urlPatterns = { "/Admin" })
@RoleSetting
public class AdminServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request)
			throws AccessException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("jobs", new JobDAO().getJobs());
		request.getRequestDispatcher("Admin.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}

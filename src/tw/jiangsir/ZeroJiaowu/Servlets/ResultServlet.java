package tw.jiangsir.ZeroJiaowu.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.ZeroJiaowu.DAOs.CourseDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.ElectiveDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.JobDAO;
import tw.jiangsir.ZeroJiaowu.Objects.User.ROLE;

@WebServlet(urlPatterns = { "/Result" })
@RoleSetting(allowHigherThen = ROLE.DEBUGGER)
public class ResultServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ElectiveDAO electiveDao = new ElectiveDAO();
		int jobid = Integer.parseInt((String) request.getParameter("jobid"));
		request.setAttribute("job", new JobDAO().getJobById(jobid));
		request.setAttribute("submittedelectives",
				electiveDao.SubmittedElectives(jobid));
		request.setAttribute("nonfenfaedelectives",
				electiveDao.NonFenfaedElectives(jobid));
		request.setAttribute("reservedselections",
				electiveDao.reservedSelections(jobid));
		request.setAttribute("courses",
				new CourseDAO().getCoursesByJobid(jobid));
		request.getRequestDispatcher("Result.jsp").forward(request, response);
	}
}

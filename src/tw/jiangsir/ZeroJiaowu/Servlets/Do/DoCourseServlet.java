package tw.jiangsir.ZeroJiaowu.Servlets.Do;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.ZeroJiaowu.DAOs.CourseDAO;
import tw.jiangsir.ZeroJiaowu.Objects.User.ROLE;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/Course.do"})
@RoleSetting(allowHigherThen = ROLE.DEBUGGER)
public class DoCourseServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if ("deleteCourse".equals(action)) {
			int courseid = Integer.parseInt(request.getParameter("courseid"));
			new CourseDAO().delete(courseid);
		}
		response.getWriter().print("Done");
	}
}

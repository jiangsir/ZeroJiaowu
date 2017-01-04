package tw.jiangsir.ZeroJiaowu.Servlets.Do;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.ZeroJiaowu.DAOs.CourseDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.ElectiveDAO;
import tw.jiangsir.ZeroJiaowu.Objects.Course;
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
			Course course = new CourseDAO().getCourseById(courseid);
			if (new ElectiveDAO().getElectivesByJobid(course.getJobid()).size() > 0) {
				throw new DataException(
						"本選課作業(" + course.getJob().getTitle() + ")已經有人開始選課了，不能刪除任何一個課程（" + course.getName() + "）。");
			}
			new CourseDAO().delete(courseid);
		}
		response.getWriter().print("Done");
	}
}

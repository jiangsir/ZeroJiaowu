package tw.jiangsir.ZeroJiaowu.Servlets.Do;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.ZeroJiaowu.DAOs.CourseDAO;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/ReFenfa.do"})
@RoleSetting
public class ReFenfaServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int jobid = Integer.parseInt((String) request.getParameter("jobid"));
		CourseDAO courseDao = new CourseDAO();
		courseDao.clearFenfa(jobid);
		// courseDao.doFenfa(jobid);
		response.getWriter().print("Done");
	}
}

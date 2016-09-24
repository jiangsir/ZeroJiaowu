package tw.jiangsir.ZeroJiaowu.Servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.ZeroJiaowu.DAOs.CourseDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.JobDAO;
import tw.jiangsir.ZeroJiaowu.Objects.Course;
import tw.jiangsir.ZeroJiaowu.Objects.Job;
import tw.jiangsir.ZeroJiaowu.Objects.User.ROLE;
import tw.jiangsir.ZeroJiaowu.utils.Utils;

@WebServlet(urlPatterns = {"/UpdateJob"})
@RoleSetting(allowHigherThen = ROLE.DEBUGGER)
public class UpdateJobServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int jobid = Integer.parseInt(request.getParameter("jobid"));
		request.setAttribute("job", new JobDAO().getJobById(jobid));
		ArrayList<Course> courses = new CourseDAO().getCoursesByJobid(jobid);
		if (courses.size() == 0) {
			courses.add(new Course());
		}
		request.setAttribute("courses", courses);
		request.setAttribute("coursecsv", this.parseCourses(courses));

		request.getRequestDispatcher("InsertJob.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int jobid = Integer.parseInt(request.getParameter("jobid"));
		Job job = new JobDAO().getJobById(jobid);
		job.setTitle(request.getParameter("title"));
		job.setContent(request.getParameter("content"));
		job.setSemester(Integer.parseInt(request.getParameter("semester")));
		// String allowedusers_PRE = job.getAllowedusers();

		job.setAllowedusers(request.getParameter("allowedusers"));
		job.setIpset(request.getParameter("ipset"));
		job.setStarttime(request.getParameter("starttime"));
		job.setFinishtime(request.getParameter("finishtime"));
		try {
			new JobDAO().update(job);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		new CourseDAO().deleteByJobid(job.getId());

		String[] coursenames = request.getParameterValues("coursename");
		String[] coursecontents = request.getParameterValues("coursecontent");
		String[] teachers = request.getParameterValues("teacher");
		String[] coursecapacitys = request.getParameterValues("coursecapacity");
		for (int i = 0; i < coursenames.length; i++) {
			// System.out.println("coursenames.length=" + coursenames.length +
			// ", courseids[i]=" + courseids[i]);
			Course course = new Course();
			// if (!"".equals(courseids[i])) {
			// // course = new Course(Integer.valueOf(courseids[i]));
			// course = new
			// CourseDAO().getCourseById(Integer.valueOf(courseids[i]));
			// }
			course.setName(coursenames[i]);
			course.setContent(coursecontents[i]);
			course.setCapacity(Integer.valueOf(coursecapacitys[i]));
			course.setTeacher(teachers[i]);
			course.setJobid(jobid);
			try {
				new CourseDAO().insert(course);
			} catch (Exception e) {
				e.printStackTrace();
				throw new DataException(e);
			}
		}

		// allowedusers 有更動時，更新 electives 記錄
		// if (!allowedusers_PRE.equals(job.getAllowedusers())) {
		// ElectiveDAO electiveDao = new ElectiveDAO();
		// electiveDao.cleanElectives(jobid);
		// Iterator<?> it = new UserDAO().getUsers().iterator();
		// while (it.hasNext()) {
		// User user = (User) it.next();
		// if (user.getUseraccount().matches(job.getAllowedusers())) {
		// Elective elective = new Elective();
		// elective.setJobid(jobid);
		// elective.setUseraccount(user.getUseraccount());
		// electiveDao.insert(elective);
		// }
		// }
		// }

		response.sendRedirect(Utils.PreviousPage(request.getSession(false)));
	}

	private String parseCourses(ArrayList<Course> courses) {
		StringBuffer s = new StringBuffer(100000);
		for (Course course : courses) {
			s.append(course.getName() + ",");
			s.append(course.getTeacher() + ",");
			s.append(course.getCapacity() + ",");
			s.append(course.getContent() + "\n");
		}
		return s.toString();
	}

}

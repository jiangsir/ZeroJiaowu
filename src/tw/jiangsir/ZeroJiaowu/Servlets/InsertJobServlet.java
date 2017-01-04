package tw.jiangsir.ZeroJiaowu.Servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.ZeroJiaowu.DAOs.CourseDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.JobDAO;
import tw.jiangsir.ZeroJiaowu.Objects.Course;
import tw.jiangsir.ZeroJiaowu.Objects.Job;
import tw.jiangsir.ZeroJiaowu.utils.Utils;

@WebServlet(urlPatterns = {"/InsertJob"})
@RoleSetting
public class InsertJobServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("job", new Job());
		ArrayList<Course> courses = new ArrayList<Course>();
		courses.add(new Course());
		request.setAttribute("courses", courses);
		request.getRequestDispatcher("InsertJob.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Job job = new Job();
		job.setTitle(request.getParameter("title"));
		job.setContent(request.getParameter("content"));
		job.setSemester(Integer.parseInt(request.getParameter("semester")));
		job.setMax_choose(Integer.parseInt(request.getParameter("max_choose")));
		job.setAllowedusers(request.getParameter("allowedusers"));
		job.setIpset(request.getParameter("ipset"));
		job.setStarttime(request.getParameter("starttime"));
		job.setFinishtime(request.getParameter("finishtime"));
		int jobid;
		try {
			jobid = new JobDAO().insert(job);

			// String coursecsv = request.getParameter("coursecsv");
			// CourseDAO courseDao = new CourseDAO();
			// for (Course course : this.parseCourseCSV(coursecsv, jobid)) {
			// courseDao.insert(course);
			// }
			String[] coursenames = request.getParameterValues("coursename");
			String[] coursecontents = request.getParameterValues("coursecontent");
			String[] teachers = request.getParameterValues("teacher");
			String[] coursecapacitys = request.getParameterValues("coursecapacity");
			for (int i = 0; i < coursenames.length; i++) {
				Course newcourse = new Course();
				newcourse.setName(coursenames[i]);
				newcourse.setContent(coursecontents[i]);
				newcourse.setCapacity(Integer.valueOf(coursecapacitys[i]));
				newcourse.setTeacher(teachers[i]);
				newcourse.setJobid(jobid);
				int courseid;
				try {
					courseid = new CourseDAO().insert(newcourse);
					newcourse.setId(courseid);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// 20100824 改掉，不在處理 Job 的時候加入。等真正選下去了才加入。
		// Iterator<?> it = new UserDAO().getUsers().iterator();
		// ElectiveDAO electiveDao = new ElectiveDAO();
		// while (it.hasNext()) {
		// User user = (User) it.next();
		// if (user.getUseraccount().matches(job.getAllowedusers())) {
		// Elective elective = new Elective();
		// elective.setJobid(jobid);
		// elective.setUseraccount(user.getUseraccount());
		// electiveDao.insert(elective);
		// }
		// }
		response.sendRedirect(Utils.PreviousPage(request.getSession(false)));
	}

	private ArrayList<Course> parseCourseCSV(String coursecsv, int jobid) {
		ArrayList<Course> courses = new ArrayList<Course>();
		String[] courselines = coursecsv.split("\n");
		for (String courseline : courselines) {
			if (courseline.trim().startsWith("#")) {
				continue;
			}
			String[] course = courseline.split(",");
			if (course.length != 4) {
				continue;
			}
			try {
				Course newcourse = new Course();
				newcourse.setJobid(jobid);
				newcourse.setName(course[0].trim());
				newcourse.setTeacher(course[1].trim());
				newcourse.setCapacity(Integer.parseInt(course[2].trim()));
				newcourse.setContent(course[3].trim());
				courses.add(newcourse);
			} catch (Exception e) {
				continue;
			}
		}
		return courses;
	}

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		// TODO Auto-generated method stub

	}

}

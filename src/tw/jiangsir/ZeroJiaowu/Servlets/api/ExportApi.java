package tw.jiangsir.ZeroJiaowu.Servlets.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.ZeroJiaowu.DAOs.CourseDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.JobDAO;
import tw.jiangsir.ZeroJiaowu.Objects.Course;
import tw.jiangsir.ZeroJiaowu.Objects.Elective;

@WebServlet(urlPatterns = {"/Export.api"})
@RoleSetting
public class ExportApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static enum TARGET {
		getResults_XLS, // 匯出選課結果
		getStudents_CSV; //
	};

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		switch (TARGET.valueOf(request.getParameter("target"))) {
			case getStudents_CSV :
				Integer courseid = Integer.parseInt(request.getParameter("courseid"));
				this.exportCSV(courseid, response);
				break;
			case getResults_XLS :
				Integer jobid = Integer.parseInt(request.getParameter("jobid"));
				this.exportResults_XLS(jobid, response);
				return;
			default :
				break;

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	private void exportResults_XLS(int jobid, HttpServletResponse response) throws UnsupportedEncodingException {

		response.reset(); // Reset the response
		// response.setContentType("application/vnd.ms-excel; charset=UTF-8");
		// response.setContentType("application/octet-stream;charset=UTF-8");
		response.setContentType("octets/stream");
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			String fileName = new JobDAO().getJobById(jobid).getTitle() + ".xls";
			// fileName = new String(fileName.getBytes("UTF-8"), "ISO8859_1");
			fileName = new String(fileName.getBytes("Big5"), "ISO8859_1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			new JobDAO().exportResults_XLS(jobid, out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void exportCSV(Integer courseid, HttpServletResponse response) throws UnsupportedEncodingException {
		Course course = new CourseDAO().getCourseById(courseid);

		String fileName = course.getName() + ".csv";
		fileName = new String(fileName.getBytes("Big5"), "ISO8859_1");
		StringBuffer content = new StringBuffer(100000);
		content.append("nth, 學號, 姓名, 1th, 2th, 3th, 4th\n");
		for (Elective elective : course.getElectives()) {
			content.append(elective.getNth());
			content.append("," + elective.getAccount());
			content.append("," + elective.getUser().getUsername());
			content.append("," + elective.getCourse1());
			content.append("," + elective.getCourse2());
			content.append("," + elective.getCourse3());
			content.append("," + elective.getCourse4());
			content.append("\n");
		}

		// ***** Output strOut to Response ******
		response.reset(); // Reset the response
		response.setContentType("application/vnd.ms-excel; charset=UTF8");
		// response.setContentType("application/octet-stream;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			// writer.write(new String(content.toString().getBytes("UTF-8"),
			// "UTF-8"));
			writer.write(content.toString());
			writer.flush();
			writer.close();
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

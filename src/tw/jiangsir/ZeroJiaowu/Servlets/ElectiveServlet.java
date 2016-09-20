package tw.jiangsir.ZeroJiaowu.Servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.ZeroJiaowu.DAOs.ElectiveDAO;
import tw.jiangsir.ZeroJiaowu.DAOs.JobDAO;
import tw.jiangsir.ZeroJiaowu.Objects.Elective;
import tw.jiangsir.ZeroJiaowu.Objects.Job;
import tw.jiangsir.ZeroJiaowu.Objects.Message;
import tw.jiangsir.ZeroJiaowu.Objects.User.ROLE;
import tw.jiangsir.ZeroJiaowu.utils.Utils;

@WebServlet(urlPatterns = {"/Elective"})
@RoleSetting(allowHigherThen = ROLE.USER)
public class ElectiveServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static boolean isAccessible(CurrentUser currentUser, int jobid) {
		if (currentUser == null) {
			return false;
		}
		if ("admin".equals(currentUser.getAccount())) {
			return true;
		}
		Job job = new JobDAO().getJobById(jobid);
		if (job.getIsRunning() && Utils.isAllowed(job.getAllowedusers(), currentUser.getAccount())) {
			return true;
		}
		return false;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		CurrentUser onlineUser = new SessionScope(session).getOnlineUser();
		int jobid = Integer.parseInt(request.getParameter("jobid"));
		ArrayList<Job> jobs = new ArrayList<Job>();
		if (!ElectiveServlet.isAccessible(onlineUser, jobid)) {
			Message message = new Message();
			message.setType(Message.getMessageType_ALERT());
			message.setTitle("您可能進到不屬於您的選課作業。");
			HashMap<String, String> links = new HashMap<String, String>();
			links.put("./", "回首頁");
			message.setLinks(links);
			request.setAttribute("message", message);
			request.getRequestDispatcher("Message.jsp").forward(request, response);
			return;
		}
		// UserBean userBean = new UserBean(session_account);

		Elective elective = new ElectiveDAO().getElectiveByJobidAccount(jobid, onlineUser.getAccount());
		Job job = new JobDAO().getJobById(jobid);
		job.setCurrentUser(onlineUser);
		System.out.println("elective=" + elective);

		if (elective != null) {
			Message message = new Message();
			message.setType(Message.getMessageType_ERROR());
			message.setTitle("您已經選填完畢！");
			String text = "如有疑問請將本畫面列印下來，並洽教務處。<br><br>";
			// text += "您已經於 "
			// + Utils.parseDatetime(elective.getSubmittime().getTime())
			// + " 完成『" + job.getTitle() + "』 選填作業！(#" + elective.getId()
			// + ")<br>";
			// text += "第一志願： " + elective.getCourse1() + "<br>";
			// text += "第二志願： " + elective.getCourse2() + "<br>";
			// text += "第三志願： " + elective.getCourse3() + "<br>";
			// text += "第四志願： " + elective.getCourse4() + "<br>";
			message.setPlainText(elective.getResult());
			HashMap<String, String> links = new HashMap<String, String>();
			links.put("./Logout", "離開");
			message.setLinks(links);
			request.setAttribute("message", message);
			request.getRequestDispatcher("Message.jsp").forward(request, response);
			return;
		}

		// if (new JobBean(session_account, jobid).isAllowedUser()) {
		// jobs.add(new Job(jobid));
		// }

		if (job.isAllowedUser()) {
			jobs.add(job);
		}

		request.setAttribute("jobs", jobs);
		request.getRequestDispatcher("Elective.jsp").forward(request, response);
		return;

		// ArrayList<Job> visibleJobs = new UserBean(session_account)
		// .getVisibleJobs();
		// if (visibleJobs.size() == 0) {
		// Message message = new Message();
		// message.setType(Message.getMessageType_ERROR());
		// message.setTitle("目前沒有任何選填作業在進行，請查明時間。");
		// HashMap<String, String> links = new HashMap<String, String>();
		// links.put("./Logout", "離開");
		// message.setLinks(links);
		// request.setAttribute("message", message);
		// request.getRequestDispatcher("Message.jsp").forward(request,
		// response);
		// return;
		// } else {
		// request.setAttribute("jobs", visibleJobs);
		// request.getRequestDispatcher("Elective.jsp").forward(request,
		// response);
		// return;
		// }

		// Message message = new Message();
		// message.setType(Message.getMessageType_ERROR());
		// message.setTitle("您已經選填完畢！");
		// String text = "如有疑問請將本畫面列印下來，並洽教務處。<br><br>";
		// text += "您已經於 "
		// + Utils.parseDatetime(elective.getSubmittime().getTime())
		// + " 完成選填作業！(#" + elective.getId() + ")<br>";
		// text += "第一志願： " + elective.getCourse1() + "<br>";
		// text += "第二志願： " + elective.getCourse2() + "<br>";
		// text += "第三志願： " + elective.getCourse3() + "<br>";
		// text += "第四志願： " + elective.getCourse4() + "<br>";
		// message.setPlainText(text);
		// HashMap<String, String> links = new HashMap<String, String>();
		// links.put("./Logout", "離開");
		// message.setLinks(links);
		// request.setAttribute("message", message);
		// request.getRequestDispatcher("Message.jsp").forward(request,
		// response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		CurrentUser onlineUser = new SessionScope(session).getOnlineUser();
		// String session_account = (String) session
		// .getAttribute("session_account");
		Elective newelective = new Elective();
		newelective.setJobid(Integer.parseInt(request.getParameter("jobid")));
		newelective.setAccount(onlineUser.getAccount());
		newelective.setCourse1(request.getParameter("course1"));
		newelective.setCourse2(request.getParameter("course2"));
		newelective.setCourse3(request.getParameter("course3"));
		newelective.setCourse4(request.getParameter("course4"));
		TreeSet<String> set = new TreeSet<String>();
		set.add(newelective.getCourse1());
		set.add(newelective.getCourse2());
		set.add(newelective.getCourse3());
		set.add(newelective.getCourse4());
		if (set.size() < 4) {
			Message message = new Message();
			message.setType(Message.getMessageType_ALERT());
			message.setTitle("不可以選相同的課程！");
			HashMap<String, String> links = new HashMap<String, String>();
			links.put("./", "回首頁");
			message.setLinks(links);
			request.setAttribute("message", message);
			request.getRequestDispatcher("Message.jsp").forward(request, response);
			return;
		}
		newelective.setSubmittime(new Timestamp(System.currentTimeMillis()));
		newelective.setIpfrom(request.getRemoteAddr());
		int id;
		try {
			id = new ElectiveDAO().insert(newelective);
		} catch (SQLException e) {
			e.printStackTrace();
			Message message = new Message();
			message.setType(Message.getMessageType_ALERT());
			message.setTitle("選填發生問題！");
			String plainText = "請將畫面列印下來，並洽教務處！<br>";
			plainText += "錯誤訊息：";
			plainText += new ElectiveDAO().printElective(newelective);
			message.setPlainText(plainText);

			HashMap<String, String> links = new HashMap<String, String>();
			links.put("./", "回首頁");
			message.setLinks(links);
			request.setAttribute("message", message);
			request.getRequestDispatcher("Message.jsp").forward(request, response);
			return;
		}
		newelective.setId(id);
		newelective.setCurrentUser(onlineUser);
		Message message = new Message();
		message.setType(Message.getMessageType_ALERT());
		message.setTitle("恭喜您完成志願選填作業！");
		// message.setPlainText(new ElectiveBean(Integer.parseInt(request
		// .getParameter("jobid")), currentUser.getAccount()).getResult());
		message.setPlainText(newelective.getResult());

		onlineUser.doLogout();

		HashMap<String, String> links = new HashMap<String, String>();
		links.put("./", "回首頁");
		message.setLinks(links);
		request.setAttribute("message", message);
		request.getRequestDispatcher("Message.jsp").forward(request, response);
	}
}

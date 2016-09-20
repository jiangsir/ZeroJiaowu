package tw.jiangsir.Utils.Servlets;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.ZeroJiaowu.Objects.Message;

@WebServlet("/ShowSessions")
public class ShowSessionsServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		Message message = new Message();
		String text = "";
		if (session == null) {
			text = "目前 session = null";
		} else {
			Enumeration enumeration = session.getAttributeNames();
			while (enumeration.hasMoreElements()) {
				String name = enumeration.nextElement().toString();
				text += name + " = " + session.getAttribute(name) + "<br>";
			}
		}
		if ("".equals(text)) {
			text += "Session 內沒有任何資料";
		}
		message.setTitle("列出所有的 sessions");
		message.setPlainText(text);
		request.setAttribute("message", message);
		request.getRequestDispatcher("/Message.jsp").forward(request, response);
	}
}

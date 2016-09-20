package tw.jiangsir.ZeroJiaowu.Filters;


import java.io.IOException;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import tw.jiangsir.ZeroJiaowu.Objects.Message;
import tw.jiangsir.ZeroJiaowu.utils.MyProperties;
import tw.jiangsir.ZeroJiaowu.utils.Utils;

public class _GeneralFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {

	}

	/**
	 * GeneralFilter 過濾全部的頁面，包含 .jsp, .css, doGET, doPOST 等
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String requestURI = request.getRequestURI();
		requestURI = requestURI.substring(requestURI.lastIndexOf('/') + 1);
		// 20091207 解決 IE 暫存問題
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		// or response.setHeader("Cache-Control","no-store");//HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the
		// proxy
		HttpSession session = request.getSession();
		String session_usergroup = (String) session
				.getAttribute("session_usergroup");
		if ("".equals(requestURI) || requestURI.matches(".*\\.[Cc][Ss][Ss]$")
				|| requestURI.matches(".*\\.[Jj][Ss]$")
				|| requestURI.matches(".*\\.[Pp][Nn][Gg]$")
				|| requestURI.matches(".*Login$")
				|| requestURI.matches(".*Logout$")
				|| requestURI.matches(".*Admin$")
				|| "GroupAdmin".equals(session_usergroup)) {
			chain.doFilter(request, response);
			return;
		}

		MyProperties myprop = new MyProperties();
		if (!"yes".equals(myprop.getProperty("IS_SYSTEMOPEN"))) {
			Message message = new Message();
			message.setType(Message.getMessageType_ERROR());
			message.setTitle(myprop.getProperty("IS_SYSTEMOPEN"));
			request.setAttribute("message", message);
			request.getRequestDispatcher("Message.jsp").forward(request,
					response);
			return;
		}

		// Date opentime = Utils.parseDatetime(myprop.getProperty("OPENTIME"));
		// Date closetime =
		// Utils.parseDatetime(myprop.getProperty("CLOSETIME"));
		// String ipfrom = request.getRemoteAddr();
		// if (new Date().getTime() < opentime.getTime()
		// && !Utils
		// .isSubnetwork(myprop.getProperty("MANAGER_IP"), ipfrom)) {
		// Message message = new Message();
		// message.setTitle("選課尚未開始！");
		// message.setPlainText("系統將於 "
		// + Utils.parseDatetime(opentime.getTime()) + " 開始接受選課，請稍候！");
		//
		// request.setAttribute("message", message);
		// request.getRequestDispatcher("Message.jsp").forward(request,
		// response);
		// return;
		// } else if (new Date().getTime() > closetime.getTime()
		// && !Utils
		// .isSubnetwork(myprop.getProperty("MANAGER_IP"), ipfrom)) {
		// Message message = new Message();
		// message.setTitle("選課已經結束！");
		// message.setPlainText("選課已經在 "
		// + Utils.parseDatetime(closetime.getTime()) + " 結束，下次請早！");
		// request.setAttribute("message", message);
		// request.getRequestDispatcher("Message.jsp").forward(request,
		// response);
		// return;
		// }

		chain.doFilter(request, response);
	}

	public void destroy() {

	}

}

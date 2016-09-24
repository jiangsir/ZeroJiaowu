package tw.jiangsir.Utils.Filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Mailer;

@WebFilter(urlPatterns = {"/*"})
public class GeneralFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {

	}

	/**
	 * GeneralFilter 過濾全部的頁面，包含 .jsp, .css, doGET, doPOST 等
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();
		String servletPath = request.getServletPath();
		String qs = request.getQueryString() == null ? "" : "?" + request.getQueryString();

		String requestURI = request.getRequestURI();
		requestURI = requestURI.substring(requestURI.lastIndexOf('/') + 1);
		// 20091207 解決 IE 暫存問題
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		// or response.setHeader("Cache-Control","no-store");//HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the
		// proxy

		// String session_account = (String) session
		// .getAttribute("session_account");
		if ("GET".equals(request.getMethod()) && !(servletPath + qs).equals(session.getAttribute("returnPage"))
				&& !servletPath.equals("/OAuth2Callback") && !servletPath.equals("/GoogleLogin")
				&& !servletPath.equals("/Login")) {
			session.setAttribute("returnPage", servletPath + qs);
		}

		// if (requestURI.matches(".*\\.[Cc][Ss][Ss]$")
		// || requestURI.matches(".*\\.[Jj][Ss]$")
		// || requestURI.matches(".*\\.[Gg][Ii][Ff]$")
		// || requestURI.matches(".*\\.[Pp][Nn][Gg]$")
		// || requestURI.matches(".*\\.[Hh][Tt][Mm]$")
		// || requestURI.matches(".*\\.[Hh][Tt][Mm][Ll]$")
		// || requestURI.matches(".*\\.[Tt][Xx][Tt]$")
		// || requestURI.matches(".*Include.*")
		// || requestURI.matches(".*Login$")
		// || requestURI.matches(".*Logout$")
		// || requestURI.matches(".*Admin$")) {
		// chain.doFilter(request, response);
		// return;
		// }

		// MyProperties myprop = new MyProperties();
		// if (!"yes".equals(myprop.getProperty("IS_SYSTEMOPEN"))) {
		// new AlertDispatcher(request, response).forward(new AlertBean(myprop
		// .getProperty("IS_SYSTEMOPEN")));
		// return;
		// }

		Runtime runtime = Runtime.getRuntime();
		if (runtime.freeMemory() < runtime.totalMemory() / 10) {
			String message = "runtime.freeMemory()=" + runtime.freeMemory() / 1024 / 1024 + "MB, runtime.totalMemory()="
					+ runtime.totalMemory() / 1024 / 1024 + "MB \n" + servletPath + qs;
			Thread mailer = new Thread(new Mailer("jiangsir@tea.nknush.kh.edu.tw", "記憶體即將耗盡", message));
			mailer.start();
		}

		chain.doFilter(request, response);

		// String CurrentPage = "";
		// if ("GET".equals(request.getMethod()) &&
		// !requestURI.matches(".*\\..*")
		// && !requestURI.matches(".*SystemClosed.jsp$")
		// && !requestURI.matches(".*ShowSessions$")
		// && !requestURI.matches(".*Download$")
		// && !requestURI.matches(".*Logout.*")
		// && !requestURI.matches(".*Login.*")) {
		// // qx 為何 Login 要記錄? 暫時 不把 Login 列入 PreviousFilter 觀察看看
		// CurrentPage = (String) session.getAttribute("CurrentPage");
		// if (request.getQueryString() != null) {
		// requestURI += "?" + request.getQueryString();
		// }
		//
		// if (!requestURI.equals(CurrentPage)) {
		// session.setAttribute("PreviousPage",
		// session.getAttribute("CurrentPage"));
		// session.setAttribute("CurrentPage", requestURI);
		// }
		// }
	}

	public void destroy() {

	}

}

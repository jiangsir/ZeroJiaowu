package tw.jiangsir.Utils.Filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.AlertBean;
import tw.jiangsir.Utils.AlertDispatcher;
import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.SessionFactory;
import tw.jiangsir.Utils.WebXmlParser;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Servlets.LoginServlet;

@WebFilter(urlPatterns = { "/*" })
public class LoginFilter implements Filter {

    private String FilterName;

    public void init(FilterConfig config) throws ServletException {
	FilterName = config.getFilterName();
	config.getServletContext().log(FilterName + "進入初始化...");
    }

    public void doFilter(ServletRequest req, ServletResponse res,
	    FilterChain chain) throws IOException, ServletException {
	HttpServletRequest request = (HttpServletRequest) req;
	HttpServletResponse response = (HttpServletResponse) res;
	String fromUrl = request.getHeader("REFERER");// 取得上一级页面的RUL
	System.out.println("從 " + fromUrl + "進入 LoginFilter");

	HttpSession session = request.getSession(false);
	// qx 測試在 filter 內部再來過濾 如 regex 的判斷
	String requestURI = request.getRequestURI();
	String servletPath = request.getServletPath();
	String urlpattern = requestURI
		.substring(requestURI.lastIndexOf('/') + 1);
	System.out.println("urlpattern=" + urlpattern + ", servletPath="
		+ request.getServletPath() + ", serveltname="
		+ new WebXmlParser().getServletName(request.getServletPath())
		+ ", serveltclass=");
	String loginURI = LoginServlet.class.getAnnotation(WebServlet.class)
		.urlPatterns()[0];
	// String loginURI = "/GoogleLogin";
	System.out.println("loginURI=" + loginURI);
	System.out.println("returnPage=" + request.getServletPath());

	CurrentUser currentUser = SessionFactory.getCurrentUser(session);
	// Boolean.valueOf("");
	if (currentUser != null && currentUser.getId() != 0) {
	    try {
		// UserBean userBean = (session_account);
		currentUser.isAccessible(servletPath);
		chain.doFilter(request, response);
		return;
	    } catch (AccessException e) {
		e.printStackTrace();
		new AlertDispatcher(request, response)
			.forward(new AlertBean(e));
		return;
	    }
	}
	if (request.getQueryString() != null) {
	    urlpattern += "?" + request.getQueryString();
	}
	session.setAttribute("OriginalURI", urlpattern);
	request.setAttribute("returnPage", request.getServletPath());
	request.getRequestDispatcher(loginURI).forward(request, response);
    }

    public void destroy() {

    }

}

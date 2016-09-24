package tw.jiangsir.Utils.Filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.Scopes.SessionScope;

@WebFilter(urlPatterns = {"/*"})
public class ExceptionFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		if (throwable == null) {
			chain.doFilter(request, response);
			return;
		}
		String uri = request.getAttribute("javax.servlet.error.request_uri").toString();

		StackTraceElement[] trace;
		trace = throwable.getStackTrace();
		StringBuffer tracestring = new StringBuffer(5000);
		for (int i = 0; i < trace.length; i++) {
			tracestring.append(trace[i] + "\n");
		}
		HttpSession session = request.getSession();
		CurrentUser currentUser = new SessionScope(session).getCurrentUser();
		if (currentUser == null) {
			throw new javax.servlet.ServletException("session 無效，請稍候再試。 系統管理員敬上");
		}
		String s = tracestring.toString();
		// 如果錯誤來自 errorlog 本身，代表資料庫連線有問題，不能記錄下來，否則會無窮迴圈
		if (!s.contains("SQL ERROR: INSERT INTO errorlog")) {
			// new LogDAO().insert(new Log(uri + "?" + request.getQueryString(),
			// currentUser.getAccount(), request.getRemoteAddr(),
			// throwable.toString(), (Exception) throwable));
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}

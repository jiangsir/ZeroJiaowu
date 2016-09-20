package tw.jiangsir.Utils.Filters;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter(filterName = "EncodingFilter", urlPatterns = {
		"/*" }, asyncSupported = true)
public class EncodingFilter implements Filter {
	// private String encoding = null;
	// private boolean ignore = true;

	public enum ENCODING {
		ISO_8859_1("ISO-8859-1"), // ISO-8859-1
		UTF_8("UTF-8");// UTF-8
		private String value = "";

		ENCODING(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	};

	public void init(FilterConfig config) throws ServletException {
		// this.encoding = config.getInitParameter("encoding");
		// String value = config.getInitParameter("ignore");
		// if (value == null)
		// this.ignore = true;
		// else if (value.equalsIgnoreCase("true"))
		// this.ignore = true;
		// else if (value.equalsIgnoreCase("yes"))
		// this.ignore = true;
		// else
		// this.ignore = false;
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		// if (ignore || (request.getCharacterEncoding() == null)) {
		// String encoding = selectEncoding(request);
		// if (encoding != null) {
		// request.setCharacterEncoding(encoding);
		// response.setContentType("text/html;charset=UTF-8");
		// // TODO // qx 對 GET 的中文進行轉碼, POST 的沒問題,只要將 tomcat server.xml
		// // connector 設定加上 URIEncoding="UTF-8" 即可。
		// // if ("GET".equals(request.getMethod())) {
		// // String Title = new String(request.getParameter("Title")
		// // .getBytes("ISO-8859-1"), "UTF-8");
		// // }
		// }
		// }

		// HttpServletRequest req = (HttpServletRequest) request;
		// HttpServletResponse resp = (HttpServletResponse) response;

		request.setCharacterEncoding(ENCODING.UTF_8.getValue());
		response.setContentType(
				"text/html;charset=" + ENCODING.UTF_8.getValue());
		// resp 也要設定好 ENCODING 否則直接 response.writer 輸出會亂碼。
		response.setCharacterEncoding(ENCODING.UTF_8.getValue());

		chain.doFilter(request, response);
	}

	public void destroy() {
		// this.encoding = null;
	}

	// protected String selectEncoding(HttpServletRequest request) {
	// return this.encoding;
	// }

}

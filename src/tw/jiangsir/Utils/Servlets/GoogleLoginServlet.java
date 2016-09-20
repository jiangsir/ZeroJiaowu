package tw.jiangsir.Utils.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GoogleLogin")
public class GoogleLoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=156955164629.apps.googleusercontent.com&redirect_uri=http://apps.nknush.kh.edu.tw/ZeroJiaowu/OAuth2Callback&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email
		String oauth_uri = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=156955164629.apps.googleusercontent.com";
		String returnPage = (String) request.getAttribute("returnPage");
		System.out.println("GoogleLogin returnPage=" + returnPage);
		// String redirect_uri =
		// "&redirect_uri=http://apps.nknush.kh.edu.tw/ZeroJiaowu/OAuth2Callback";
		String redirect_uri = "&redirect_uri=http://apps.nknush.kh.edu.tw/ZeroJiaowu/OAuth2Callback";
		String scope = "&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email";
		response.sendRedirect(oauth_uri + redirect_uri + scope);
	}

}

package tw.jiangsir.Utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AlertDispatcher {

	public final String FILENAME_MESSAGE = "Message.jsp";
	HttpServletRequest request;
	HttpServletResponse response;

	public AlertDispatcher(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public void forward(AlertBean alertBean) throws ServletException,
			IOException {
		request.setAttribute("alertBean", alertBean);
		request.getRequestDispatcher(FILENAME_MESSAGE).forward(request,
				response);
	}

}

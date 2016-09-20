package tw.jiangsir.ZeroJiaowu.Servlets.Do;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.ZeroJiaowu.DAOs.ElectiveDAO;
import tw.jiangsir.ZeroJiaowu.Objects.Elective;
import tw.jiangsir.ZeroJiaowu.Objects.User.ROLE;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/Elective.do"})
@RoleSetting(allowHigherThen = ROLE.DEBUGGER)
public class DoElectiveServlet extends HttpServlet {

	public enum ACTION {
		deleteElective, changeLock,
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		switch (ACTION.valueOf(action)) {
			case deleteElective :
				int id = Integer.parseInt(request.getParameter("id"));
				new ElectiveDAO().delete(id);
				break;
			case changeLock :
				id = Integer.parseInt(request.getParameter("id"));
				Elective elective = new ElectiveDAO().getElectiveById(id);
				elective.setLock(1 - elective.getLock());
				try {
					new ElectiveDAO().update(elective);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			default :
				break;
		}
		response.getWriter().print("Done");
	}
}

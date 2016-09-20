package tw.jiangsir.ZeroJiaowu.Servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.ZeroJiaowu.DAOs.UserDAO;
import tw.jiangsir.ZeroJiaowu.Objects.User;
import tw.jiangsir.ZeroJiaowu.Objects.User.ROLE;

@WebServlet("/Import")
@RoleSetting(allowHigherThen = ROLE.USER)
public class ImportServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("Import.jsp").forward(request, response);
	}

	private int fieldindex(String[] fields, String fieldname) {
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].trim().equals(fieldname.trim())) {
				return i;
			}
		}
		return -1;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] csvdata = request.getParameter("csvdata").split("\n");
		String[] fields = csvdata[0].split(",");
		for (int i = 1; i < csvdata.length; i++) {
			System.out.println(csvdata[i]);
			String[] csvline = csvdata[i].split(",");
			if (csvline.length == fields.length && csvline[this.fieldindex(fields, "account")].matches("[0-9]+")) {
				// User user = new User(csvline[this.fieldindex(fields,
				// "useraccount")].trim());
				User user = new UserDAO().getUserByAccount(csvline[this.fieldindex(fields, "account")]);
				if (user == null) {
					user = new User();
					user.setAccount(csvline[this.fieldindex(fields, "account")].trim());
					// user.setUsergroup(User.GROUP.G roupUser);
					user.setRole(User.ROLE.USER);
					user.setNumber(Integer.parseInt(csvline[this.fieldindex(fields, "number")].trim()));
					user.setUsername(csvline[this.fieldindex(fields, "username")].trim());
					user.setPasswd(csvline[this.fieldindex(fields, "passwd")].trim());
					user.setComment(csvline[this.fieldindex(fields, "comment")].trim());
					try {
						new UserDAO().insert(user);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					user.setAccount(csvline[this.fieldindex(fields, "account")].trim());
					// user.setUsergroup(User.GROUP.GroupUser);
					user.setRole(User.ROLE.USER);
					user.setNumber(Integer.parseInt(csvline[this.fieldindex(fields, "number")].trim()));
					user.setUsername(csvline[this.fieldindex(fields, "username")].trim());
					user.setPasswd(csvline[this.fieldindex(fields, "passwd")].trim());
					user.setComment(csvline[this.fieldindex(fields, "comment")].trim());
					try {
						new UserDAO().update(user);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		response.sendRedirect("Admin");
	}
}

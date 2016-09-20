package tw.jiangsir.Utils.Filters;

import java.io.IOException;
import java.util.HashSet;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import tw.jiangsir.Utils.CurrentUser;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.ZeroJiaowu.Objects.User;

@WebFilter(filterName = "RoleFilter", urlPatterns = {"/*"}, asyncSupported = true)
public class RoleFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public RoleFilter() {
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		String servletPath = request.getServletPath();
		HttpServlet httpServlet = ApplicationScope.getUrlpatterns().get(servletPath);
		if (httpServlet == null || httpServlet.getClass().getAnnotation(RoleSetting.class) == null) {
			chain.doFilter(request, response);
			return;
		}

		// if (request.getMethod().toUpperCase().equals("GET")) {
		// new SessionScope(session).setReturnPage(servletPath,
		// request.getQueryString());
		// }
		/**
		 * 先檢查整站模式。
		 * 
		 */

		// if (httpServlet.getClass().getAnnotation(RoleSetting.class) == null)
		// {
		// chain.doFilter(request, response);
		// return;
		// }

		CurrentUser onlineUser = new SessionScope(session).getOnlineUser();
		if (onlineUser == null || onlineUser.isNullUser()) {

			request.getRequestDispatcher("/Login.jsp").forward(request, response);
			// response.sendRedirect(request.getContextPath() +
			// LoginServlet.class
			// .getAnnotation(WebServlet.class).urlPatterns()[0]);
			return;
		}
		if (!this.isUserInRoles(onlineUser, httpServlet)) {
			throw new DataException("您沒有權限瀏覽這個頁面。");
		}
		chain.doFilter(request, response);

	}

	public void destroy() {

	}

	/**
	 * 取得某一個 servlet 所有“允許”存取的 roles<br>
	 * 例：<br>
	 * IndexServlet = HashSet{User.ROLE.DEBUGGER, User.ROLE.MANAGER,
	 * User.ROLE.USER, User.ROLE.GUEST}<br>
	 * AdminServlet = HashSet{User.ROLE.DEBUGGER}<br>
	 * 
	 * @param httpServlet
	 * @return
	 */
	public HashSet<User.ROLE> getRoleSet(HttpServlet httpServlet) {
		HashSet<User.ROLE> roleSet = new HashSet<User.ROLE>();
		RoleSetting roleSetting = httpServlet.getClass().getAnnotation(RoleSetting.class);
		if (roleSetting == null) {
			// roleSetting 沒有設定，代表所有所有 role 都可以存取這個 servlet
			for (User.ROLE role : User.ROLE.values()) {
				roleSet.add(role);
			}
			return roleSet;
		}

		// 加入 高於指定的 role
		for (User.ROLE role : User.ROLE.values()) {
			if (role.isHigherEqualThan(roleSetting.allowHigherThen())) {
				// if (roleSetting.allowHigherThen().ordinal() >=
				// role.ordinal()) {
				roleSet.add(role);
			}
		}

		// 加入 個別指定的 role
		for (User.ROLE role : roleSetting.allows()) {
			roleSet.add(role);
		}

		// 移除 低於指定的 role
		for (User.ROLE role : User.ROLE.values()) {
			if (role.isLowerEqualThan(roleSetting.denyLowerThen())) {
				roleSet.remove(role);
			}
		}

		// 移除個別指定的 role
		for (User.ROLE role : roleSetting.denys()) {
			roleSet.remove(role);
		}
		return roleSet;

	}

	/**
	 * 只判斷該使用者是否是 某個 servlet 的 RoleSetting 允許 ROLE
	 * 
	 * @param user
	 * @param httpServlet
	 * @return
	 */
	private boolean isUserInRoles(CurrentUser onlineUser, HttpServlet httpServlet) {
		// return ApplicationScope.getRoleMap().get(onlineUser.getRole())
		// .contains(httpServlet);
		return this.getRoleSet(httpServlet).contains(onlineUser.getRole());
	}

}

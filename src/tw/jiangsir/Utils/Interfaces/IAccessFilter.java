package tw.jiangsir.Utils.Interfaces;

import javax.servlet.http.HttpServletRequest;

import tw.jiangsir.Utils.Exceptions.AccessException;

/**
 * @author jiangsir
 * 
 */
public interface IAccessFilter {
	// abstract public int insert(T t) throws DataException;
	public void AccessFilter(HttpServletRequest request) throws AccessException;

	// public void AccessibleFilter(HttpServletRequest request)
	// throws AccessException;
}

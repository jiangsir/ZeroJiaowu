/**
 * idv.jiangsir.utils - XMLParser.java
 * 2009/3/21 上午 04:09:15
 * jiangsir
 */
package tw.jiangsir.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;

import net.htmlparser.jericho.Source;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * @author jiangsir
 * 
 */
public class WebXmlParser {
	private Document doc_webxml = null;
	private String path_webxml;

	/**
	 * 對 Web.xml 進行解析
	 * 
	 */
	public WebXmlParser() {
		this.path_webxml = ENV.APP_REAL_PATH + "/WEB-INF/web.xml";

		if (ENV.source_webxml == null) {
			InputStream is;
			try {
				is = new FileInputStream(new File(this.path_webxml));
				ENV.source_webxml = new Source(is);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// SAXBuilder builder = new SAXBuilder();
		// try {
		// this.doc_webxml = builder.build(new File(this.path_webxml));
		// } catch (JDOMException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	/**
	 * 外部使用
	 * 
	 * @param APP_REAL_PATH
	 */
	public WebXmlParser(String APP_REAL_PATH) {
		this.path_webxml = APP_REAL_PATH + "/WEB-INF/web.xml";
	}

	/**
	 * 實際寫入 web.xml
	 * 
	 */
	public void writetoWebxml() {
		XMLOutputter outp = new XMLOutputter();
		FileOutputStream fo = null;

		try {
			fo = new FileOutputStream(this.path_webxml);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			outp.output(this.doc_webxml, fo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得 web.xml context-param 的設定。
	 * 
	 * @param name
	 * @return
	 */
	public String getInitParam(String param) {
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_webxml == null) {
				this.doc_webxml = builder.build(new File(this.path_webxml));
			}
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Get the root element
		Element root = this.doc_webxml.getRootElement();
		// Print servlet information
		List children = root.getChildren();
		Iterator i = children.iterator();
		while (i.hasNext()) {
			Element child = (Element) i.next();
			if ("context-param".equals(child.getName())) {
				Element param_name = (Element) child.getChildren().get(0);
				Element param_value = (Element) child.getChildren().get(1);
				if (param_name.getValue().equals(param)) {
					return param_value.getText().trim();
				}
			}
		}
		return null;
	}

	/**
	 * 用 urlpattern 取得 Servlet class
	 * 
	 * @param urlpattern
	 * @return
	 * @throws IOException
	 */
	public String getServletClass(String urlpattern) throws IOException {
		String servletname = this.getServletName(urlpattern);
		Iterator<net.htmlparser.jericho.Element> it = ENV.source_webxml
				.getAllElements("servlet").iterator();
		while (it.hasNext()) {
			net.htmlparser.jericho.Element element = it.next();
			String name = element.getFirstElement("servlet-name").getContent()
					.toString();
			if (name.equals(servletname)) {
				return element.getFirstElement("servlet-class").getContent()
						.toString().trim();
			}
		}
		return null;
	}

	/**
	 * 用 urlpattern 取得 Servlet name
	 * 
	 * @param urlpattern
	 * @return
	 * @throws FileNotFoundException
	 */
	public String getServletName(String urlpattern)
			throws FileNotFoundException {
		Iterator<net.htmlparser.jericho.Element> it = ENV.source_webxml
				.getAllElements("servlet-mapping").iterator();
		while (it.hasNext()) {
			net.htmlparser.jericho.Element element = it.next();
			String url = element.getFirstElement("url-pattern").getContent()
					.toString();
			if (url.equals(urlpattern)) {
				return element.getFirstElement("servlet-name").getContent()
						.toString().trim();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param urlpattern
	 * @return
	 */
	public String getServletName_ByJDOM(String urlpattern) {
		Element root = this.doc_webxml.getRootElement();
		Iterator<?> it = root.getChildren().iterator();
		// Iterator<?> it = root.getChildren().iterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();

			if (element.getName().equals("servlet-mapping")) {
				List mapping = element.getChildren();
				if (((Element) mapping.get(1)).getTextTrim().equals(urlpattern)) {
					return ((Element) mapping.get(0)).getTextTrim();
				}
			}
		}
		return null;
	}

	/**
	 * 從 servlet class 取得 servlet name.
	 * 
	 * @param servlet
	 * @return
	 */
	public String getServletname(Class<? extends HttpServlet> servletclass) {
		for (net.htmlparser.jericho.Element element : ENV.source_webxml
				.getAllElements("servlet")) {
			if (servletclass.getName().equals(
					element.getFirstElement("servlet-class").getContent()
							.toString().trim())) {
				return element.getFirstElement("servlet-name").getContent()
						.toString().trim();
			}
		}
		return "";
	}

	/**
	 * 從 servlet class 取得 url-pattern
	 * 
	 * @param servlet
	 * @return
	 */
	public String getUrlpattern(Class<? extends HttpServlet> servlet) {
		String servletname = this.getServletname(servlet);
		System.out.println("servletname = " + servletname);
		for (net.htmlparser.jericho.Element element : ENV.source_webxml
				.getAllElements("servlet-mapping")) {
			if (servletname.equals(element.getFirstElement("servlet-name")
					.getContent().toString())) {
				return element.getFirstElement("url-pattern").getContent()
						.toString().trim();
			}
		}
		return "";
		// Iterator<net.htmlparser.jericho.Element> it = ENV.source_webxml
		// .getAllElements("servlet-mapping").iterator();
		// while (it.hasNext()) {
		// net.htmlparser.jericho.Element element = it.next();
		// String url = element.getFirstElement("url-pattern").getContent()
		// .toString();
		// if (url.equals(urlpattern)) {
		// return element.getFirstElement("servlet-name").getContent()
		// .toString().trim();
		// }
		// }
		//
		// return "";
	}

	/**
	 * 設定 web.xml context-param
	 * 
	 * @param name
	 * @param value
	 */
	public void setInitParam(String param, String value) {
		if ("".equals(value)) {
			return;
		}
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_webxml == null) {
				this.doc_webxml = builder.build(new File(this.path_webxml));
			}
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Get the root element
		Element root = this.doc_webxml.getRootElement();
		// Print servlet information
		List children = root.getChildren();
		Iterator i = children.iterator();
		while (i.hasNext()) {
			Element child = (Element) i.next();
			if ("context-param".equals(child.getName())) {
				Element param_name = (Element) child.getChildren().get(0);
				Element param_value = (Element) child.getChildren().get(1);
				if (param.equals(param_name.getValue())) {
					param_value.setText(value);
					return;
				}
			}
		}
	}
}

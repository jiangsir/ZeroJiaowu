/**
 * idv.jiangsir.utils - XMLParser.java
 * 2009/3/21 上午 04:09:15
 * jiangsir
 */
package tw.jiangsir.ZeroJiaowu.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import tw.jiangsir.Utils.ENV;

/**
 * @author jiangsir
 * 
 */
public class XMLParser {
	private Document doc_contextxml = null;

	private Document doc_webxml = null;

	private String path_webxml;

	private String path_contextxml;

	/**
	 * web 使用
	 * 
	 */
	public XMLParser() {
		this.path_webxml = ENV.APP_REAL_PATH + "/WEB-INF/web.xml";
		this.path_contextxml = ENV.APP_REAL_PATH + "/META-INF/context.xml";
	}

	/**
	 * 外部使用
	 * 
	 * @param APP_REAL_PATH
	 */
	public XMLParser(String APP_REAL_PATH) {
		this.path_webxml = APP_REAL_PATH + "/WEB-INF/web.xml";
		this.path_contextxml = APP_REAL_PATH + "/META-INF/context.xml";
	}

	/**
	 * 讀取 context.xml 的資料
	 * 
	 * @param attribute
	 * @param key
	 * @return
	 */
	public String getContextParam(String attribute, String key) {
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_contextxml == null) {
				this.doc_contextxml = builder.build(new File(
						this.path_contextxml));
			}
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Get the root element
		Element root = this.doc_contextxml.getRootElement();
		// Print servlet information
		List children = root.getChildren();
		Iterator i = children.iterator();

		while (i.hasNext()) {
			Element child = (Element) i.next();
			if (attribute.equals(child.getName())) {
				return child.getAttributeValue(key);
			}
		}
		return null;
	}

	/**
	 * 設定 context.xml 的資料。空白的話，則維持原設定
	 * 
	 * @param attribute
	 * @param key
	 * @param value
	 */
	public void setContextParam(String attribute, String key, String value) {
		if ("".equals(value)) {
			return;
		}
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_contextxml == null) {
				this.doc_contextxml = builder.build(new File(
						this.path_contextxml));
			}
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Get the root element
		Element root = this.doc_contextxml.getRootElement();
		// Print servlet information
		List children = root.getChildren();
		Iterator i = children.iterator();
		while (i.hasNext()) {
			Element child = (Element) i.next();
			if (attribute.equals(child.getName())) {
				child.setAttribute(key, value);
				return;
			}
		}
	}

	/**
	 * 設定 context.xml 的 root (docBase)
	 * 
	 * @param key
	 * @param value
	 */
	public void setContextParam_docBase(String value) {
		if ("".equals(value)) {
			return;
		}
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_contextxml == null) {
				this.doc_contextxml = builder.build(new File(
						this.path_contextxml));
			}
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Get the root element
		Element root = this.doc_contextxml.getRootElement();
		root.setAttribute("docBase", value);
	}

	public String getDBHost() {
		String url = this.getContextParam("Resource", "url");
		// jdbc:mysql://127.0.0.1:3306/zerojudge_dev?useUnicode=true&characterEncoding=UTF-8
		return url.substring(url.indexOf("//") + 2, url.indexOf(":3306"));
	}

	public void setDBHost(String host) {
		if ("".equals(host)) {
			return;
		}
		String url = this.getContextParam("Resource", "url");
		url = url.replaceFirst(this.getDBHost(), host);
		this.setContextParam("Resource", "url", url);
	}

	public String getDBName() {
		String url = this.getContextParam("Resource", "url");
		// jdbc:mysql://127.0.0.1:3306/zerojudge_dev?useUnicode=true&characterEncoding=UTF-8
		return url.substring(url.indexOf("3306/") + 5, url.indexOf("?"));
	}

	public void setDBName(String name) {
		if ("".equals(name)) {
			return;
		}
		String url = this.getContextParam("Resource", "url");
		url = url.replaceFirst(this.getDBName(), name);
		this.setContextParam("Resource", "url", url);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outp.output(this.doc_webxml, fo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	/**
	 * 實際寫入 context.xml
	 * 
	 */
	public void writeContextxml() {
		XMLOutputter outp = new XMLOutputter();
		FileOutputStream fo = null;

		try {
			fo = new FileOutputStream(this.path_contextxml);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outp.output(this.doc_contextxml, fo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

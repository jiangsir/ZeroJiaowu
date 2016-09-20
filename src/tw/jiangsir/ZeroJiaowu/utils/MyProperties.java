package tw.jiangsir.ZeroJiaowu.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import tw.jiangsir.Utils.ENV;

@SuppressWarnings("serial")
public class MyProperties {

	Properties props;

	String path_propertiesxml;

	public MyProperties() {
		this.path_propertiesxml = ENV.APP_REAL_PATH
				+ "/META-INF/properties.xml";
		this.readProperties();
	}

	public MyProperties(String APP_REAL_PATH) {
		this.path_propertiesxml = APP_REAL_PATH + "/META-INF/properties.xml";
		this.readProperties();
	}

	private void readProperties() {
		props = new Properties();
		FileInputStream fis = null;
		try {
			// fis = new FileInputStream(ENV.APP_REAL_PATH + PropertiesFile);
			fis = new FileInputStream(this.path_propertiesxml);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			props.loadFromXML(fis);
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * 取得所有 properties
	 */
	public ArrayList getProperties() {
		ArrayList list = new ArrayList();
		// java.util.Properties props = new java.util.Properties();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(this.path_propertiesxml);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			props.loadFromXML(fis);
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// qx Enumeration enumeration = props.propertyNames();
		Enumeration enumeration = props.keys();
		while (enumeration.hasMoreElements()) {
			HashMap map = new HashMap();
			String name = enumeration.nextElement().toString();
			map.put(name, props.getProperty(name));

			// qx list.add(name + " = " + props.getProperty(name) + " ");
			list.add(map);
		}
		return list;
	}

	/**
	 * 取得系統提供的 properties
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList getSystemProperties() {
		ArrayList list = new ArrayList();
		Enumeration enumeration = System.getProperties().keys();
		while (enumeration.hasMoreElements()) {
			HashMap map = new HashMap();
			String name = enumeration.nextElement().toString();
			map.put(name, System.getProperty(name));
			list.add(map);
		}
		return list;
	}

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getProperty() {
		return props.getProperty(this.getKey());
	}

	/**
	 * 以 key 取得單一 property 的值
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return props.getProperty(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public void setProperty(String key, String value) {
		if (key == null || value == null) {
			return;
		}
		props.setProperty(key, value);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(this.path_propertiesxml);
			props.storeToXML(fos, "系統參數");
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removeProperty(String key) {
		if (key == null) {
			return;
		}
		props.remove(key);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(this.path_propertiesxml);
			props.storeToXML(fos, "系統參數");
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

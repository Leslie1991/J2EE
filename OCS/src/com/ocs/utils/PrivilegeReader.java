package com.ocs.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.ocs.entities.Privilege;

/**
 * 用于解析privileges.xml的工具类
 * @author Leslie Leung
 */
public class PrivilegeReader {
	// 用于存储privileges.xml的权限操作信息
	private static List<Privilege> privileges = 
			new ArrayList<Privilege>();

	static {
		InputStream xml = PrivilegeReader.class.getClassLoader()
				.getResourceAsStream("privileges.xml");
		privileges = toModuleList(xml);
	}

	/**
	 * 返回XML中所有权限数据
	 * 
	 * @return
	 */
	public static List<Privilege> getPrivileges() {
		return privileges;
	}

	/**
	 * 根据权限ID查询模块名称
	 * @param id
	 * @return
	 */
	public static String getPrivilegeNameById(Integer id) {
		List<Privilege> privileges = getPrivileges();
		for (Privilege privilege : privileges) {
			if (privilege.getId().equals(id)) {
				return privilege.getName();
			}
		}
		return null;
	}
	
	/**
	 * 根据权限id查询可访问url
	 * @param id
	 */
	public static List<String> getPrivilegeURLsById(Integer id) {
		List<Privilege> privileges = getPrivileges();
		for (Privilege privilege : privileges) {
			if (privilege.getId().equals(id)) {
				return privilege.getUrls();
			}
		}
		return null;
	}

	/**
	 * 解析privileges.xml文件
	 * 
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static List<Privilege> toModuleList(InputStream xml) {
		List<Privilege> modules = new ArrayList<Privilege>();
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(xml);
			Element root = doc.getRootElement();
			List<Element> moduleElements = root.elements("privilege");
			for (Element moduleElement : moduleElements) {
				Privilege module = new Privilege();
				module.setId(Integer.valueOf(moduleElement.attributeValue("id")));
				module.setName(moduleElement.elementText("name"));
				Element urlElement = moduleElement.element("urls");
				List<Element> urlElements = urlElement.elements();
				List<String> urls = new ArrayList<String>();
				for (Element element : urlElements) {
					urls.add(element.getText());
				}
				module.setUrls(urls);
				modules.add(module);
			}

			return modules;
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("解析权限文件失败！", e);
		}
	}
}

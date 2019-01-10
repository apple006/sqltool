package com.ui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.entity.LoginInfo;

/**
 * 解析XML查找已存在连接
 * 
 * @author Administrator
 * 
 */
public class XMLConnectionsConfig {
	private static XMLConnectionsConfig xmlTree;
	private static Object o = new Object();

	private final String property = System.getProperty("user.dir");

	public static XMLConnectionsConfig getConnDataBaseInfo() {
		if (xmlTree == null) {
			synchronized (o) {
				xmlTree = new XMLConnectionsConfig();
			}
		} else {
			return xmlTree;
		}
		return xmlTree;
	}

	private Document getDocument() {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(property
					+ "\\config\\connectionsConfig.xml");
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

		return document;
	}

	public Hashtable<String, LoginInfo> getConnectionInfos() {
		Element root = getDocument().getRootElement();
		Hashtable<String, LoginInfo> connMap = new Hashtable<String, LoginInfo>();

		LoginInfo loginInfo ;
		Iterator iterator = root.elementIterator("connection");
		while (iterator.hasNext()) {
			loginInfo = new LoginInfo();
			Element next = (Element) iterator.next();
			String name = next.elementText("name");
			loginInfo.setName(name);
			String pdm = next.elementText("pdm");
			loginInfo.setPdm(pdm);
			String dataType = next.elementText("datatype");
			loginInfo.setDataType(dataType);
			String driverClass = next.elementText("driverClass");
			loginInfo.setDriverClass(driverClass);
			String url = next.elementText("url");
			loginInfo.setUrl(url);
			String ip = next.elementText("ip");
			loginInfo.setIp(ip);
			String port = next.elementText("port");
			loginInfo.setPort(port);
			String example = next.elementText("example");
			loginInfo.setExample(example);
			String username = next.elementText("username");
			loginInfo.setUserName(username);
			String password = next.elementText("password");
			loginInfo.setPassword(password);
			String date = next.elementText("date");
			loginInfo.setDate(date);
			String update = next.elementText("update");
			loginInfo.setUpDate(update);
			connMap.put(date, loginInfo);
		}
		return connMap;
	}


	public void createConnectionInfo(){
	}
	
	/**
	 * 更新或者新建连接时调用此方法
	 * @param info
	 * @param connInfo_old
	 * @return 返回true则数据更新成功，false则数据没有更新
	 * @throws IOException
	 */
	public boolean updateConnectionInfo(LoginInfo info, LoginInfo connInfo_old) throws IOException {
	
//		if(info.equals(connInfo_old)){
//			return true;
//		}
		return editConnectionInfo(info);
	}
	public boolean deleteLoginInfo(LoginInfo info) throws IOException {
		Document document = getDocument();
		Element findElemnt = earchAll(info, document);
		boolean remove = document.getRootElement().remove(findElemnt);
		OutputFormat format = OutputFormat.createPrettyPrint();// 创建格式化模版对象
		format.setEncoding("UTF-8");
		format.setIndent(true); // 设置是否缩进
		format.setIndent("    "); // 以空格方式实现缩进
		format.setNewlines(true); // 设置是否换行
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(property+ "\\config\\connectionsConfig.xml"), format);
		xmlWriter.write(document);
		xmlWriter.flush();
		xmlWriter.close();
		return remove;
	}


	public boolean addConnectionInfo(LoginInfo info) throws IOException {
		Document document = getDocument();
		Element root = document.getRootElement();
//		Element newInfo = earchAll(info, document);
//		if(newInfo!=null){
//			return false;
//		}
		
		
		Element addElement = root.addElement("connection");
		Element name = addElement.addElement("name");
		name.addText(info.getName());
		Element pdm = addElement.addElement("pdm");
		pdm.addText(info.getPdm());
		Element datatype = addElement.addElement("datatype");
		datatype.addText(info.getDataType());
		Element url = addElement.addElement("url");
		url.addText(info.getUrl());
		Element ip = addElement.addElement("ip");
		ip.addText(info.getIp());
		Element port = addElement.addElement("port");
		port.addText(info.getPort());
		Element example = addElement.addElement("example");
		example.addText(info.getExample());
		
		Element driverClass = addElement.addElement("driverClass");
		driverClass.addText(info.getDriverClass());
		Element username = addElement.addElement("username");
		username.addText(info.getUserName());
		Element password = addElement.addElement("password");
		password.addText(info.getPassword());
		Element date = addElement.addElement("date");
		date.addText(info.getDate());
		Element update = addElement.addElement("update");
		update.addText(info.getUpdate());
		
		OutputFormat format = OutputFormat.createPrettyPrint();// 创建格式化模版对象
		format.setEncoding("UTF-8");
		format.setIndent(true); // 设置是否缩进
		format.setIndent("    "); // 以空格方式实现缩进
		format.setNewlines(true); // 设置是否换行
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(property+ "\\config\\connectionsConfig.xml"), format);
		xmlWriter.write(document);
		xmlWriter.flush();
		xmlWriter.close();
		return true;
	}
	
	private boolean editConnectionInfo(LoginInfo info) throws IOException {
		Document document = getDocument();
		
	
		Element findElemnt = earchAll(info, document);
//		if(){
//			
//		}
	
		Element name = findElemnt.element("name");
		name.setText(info.getName());
		Element pdm = findElemnt.element("pdm");
		pdm.setText(info.getPdm());
		Element datatype = findElemnt.element("datatype");
		datatype.setText(info.getDataType());
		Element driverClass = findElemnt.element("driverClass");
		driverClass.setText(info.getDriverClass());
		Element url = findElemnt.element("url");
		url.setText(info.getUrl());
		Element ip = findElemnt.element("ip");
		ip.setText(info.getIp());
		Element port = findElemnt.element("port");
		port.setText(info.getPort());
		Element example = findElemnt.element("example");
		example.setText(info.getExample());
		Element username = findElemnt.element("username");
		username.setText(info.getUserName());
		Element password = findElemnt.element("password");
		password.setText(info.getPassword());
		Element date = findElemnt.element("date");
		date.setText(info.getDate());
		Element update = findElemnt.element("update");
		update.setText(new Date().toLocaleString());
		OutputFormat format = OutputFormat.createPrettyPrint();// 创建格式化模版对象
		format.setEncoding("UTF-8");
		format.setIndent(true); // 设置是否缩进
		format.setIndent("    "); // 以空格方式实现缩进
		format.setNewlines(true); // 设置是否换行
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(property+ "\\config\\connectionsConfig.xml"), format);
		xmlWriter.write(document);
		xmlWriter.flush();
		xmlWriter.close();
		return true;
	}

	private Element earchAll(LoginInfo connInfo_old, Document document) {
		Element rootElement = document.getRootElement();
		Iterator elementIterator = rootElement.elementIterator();
		while(elementIterator.hasNext()){
			Element next = (Element) elementIterator.next();
			String date = next.elementText("date");
			if(connInfo_old.getDate().equals(date)){
				 return next;
			}
		}
		return null;
	}
}

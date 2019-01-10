
package com.view.system.perferenc.xml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.entity.DataBaseType;
import com.entity.DriverPathInfo;

public class XMLJdbcConfig {
	private final String property = System.getProperty("user.dir");
	private static XMLJdbcConfig jdbcConfig;
	private static Object o = new Object();
	private Hashtable<String , DataBaseType> dataBaesTypes;
	private Hashtable<String , DriverPathInfo> driverPaths;
	private ArrayList<DriverPathInfo> driverPathList ;
	/**
	 * 数据库类型做下拉列表使用
	 */
	ArrayList<DataBaseType> dataBaseTypeList ;
	private XMLJdbcConfig(){
		getDataBaseTypesArr();
		getDriverPathsArr();
	}
	
	public Hashtable<String, DataBaseType> getDataBaseTypes() {
		return dataBaesTypes;
	}

	/**
	 * 做下拉使用
	 * @return
	 */
	public ArrayList<DataBaseType> getDataBaseTypeList() {
		return dataBaseTypeList;
	}


	public void setDataBaesTypes(Hashtable<String, DataBaseType> dataBaesTypes) {
		this.dataBaesTypes = dataBaesTypes;
	}

	public Hashtable<String, DriverPathInfo> getDriverPaths() {
		return driverPaths;
	}

	public void setJdbcPaths(Hashtable<String, DriverPathInfo> jdbcPaths) {
		this.driverPaths = jdbcPaths;
	}

	public static XMLJdbcConfig getXMLJdbcConfig(){
		if(jdbcConfig==null){
			synchronized (o) {
				jdbcConfig = new XMLJdbcConfig();
			}
		}
		return jdbcConfig;
	}
	
	public Hashtable<String, DriverPathInfo> getDriverPathsArr() {
		driverPaths = new Hashtable<String, DriverPathInfo>();
		Element xmlRoot = getXMLRoot().getRootElement();
		Iterator elementIterator = xmlRoot.elementIterator("jdbcPaths");
		Element element = (Element) elementIterator.next();
		Iterator elementIterator2 = element.elementIterator();
		driverPathList = new ArrayList<DriverPathInfo>();
		while(elementIterator2.hasNext()){
			Element next = ((Element) elementIterator2.next());
			DriverPathInfo jdbcConfig = new DriverPathInfo(next.elementText("name"), next.elementText("urlFormat"), next.elementText("driverClass"),next.elementText("fileUrl"));
			driverPaths.put(next.elementText("name"), jdbcConfig);
			driverPathList.add(jdbcConfig);
		}
		return driverPaths;
	}
	public static void main(String[] args) {
//		boolean search = new XMLJdbcConfig().search("Oracle11ga");
//		System.out.println(search);
	}
	
	public boolean  addDriverPaths(DriverPathInfo info,DriverPathInfo old) throws IOException{
		
		 Document document = getXMLRoot();
		 Element xmlRoot =document.getRootElement();;
	
		if(old!=null){
			return editJbdcConfig(info, old,document);
		}
		else{
			if(search(info.getName(),document)!=null){
				return false;
	//			
			}
//		else{
			Iterator elementIterator = xmlRoot.elementIterator("jdbcPaths");
			Element next = (Element) elementIterator.next();
			Element jdbcPath = next.addElement("jdbcPath");
			Element name = jdbcPath.addElement("name");
			name.setText(info.getName());
			Element urlFormat = jdbcPath.addElement("urlFormat");
			urlFormat.setText(info.getUrlFormat());
			Element driverClass = jdbcPath.addElement("driverClass");
			driverClass.setText(info.getDriverClass());
			Element fileUrl = jdbcPath.addElement("fileUrl");
			fileUrl.setText(info.getFileUrl());
//		}
		OutputFormat format = OutputFormat.createPrettyPrint();// 创建格式化模版对象
		format.setEncoding("UTF-8");
		format.setIndent(true); // 设置是否缩进
		format.setIndent("    "); // 以空格方式实现缩进
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(property+ "\\config\\jdbcConfig.xml"), format);
		format.setNewlines(true); // 设置是否换行
		xmlWriter.write(document);
		xmlWriter.flush();
		xmlWriter.close();	
		return true;
		}
	}
	
	private Element  search(String name, Document document){
		Element rootElement = document.getRootElement();
		Iterator elementIterator = ((Element)rootElement.elementIterator("jdbcPaths").next()).elementIterator();
		while(elementIterator.hasNext()){
			Element next = (Element) elementIterator.next();
			String name1 = next.elementText("name");
			if(name.equals(name1)){
				return next;
			}
		}
		return null;
	}
	
	public ArrayList<DriverPathInfo> getDriverPathList() {
		return driverPathList;
	}

	private Document getXMLRoot(){
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(property + "\\config\\jdbcConfig.xml");
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		return document;
	}

	public boolean  editJbdcConfig(DriverPathInfo info,DriverPathInfo old,Document document) throws IOException{
		
//		Iterator element= xmlRoot.elementIterator("jdbcPaths");
		Element next ;
		if((next =search(old.getName(),document))==null){
			return false;
		}
		Element name = next.element("name");
		name.setText(info.getName());
		Element urlFormat = next.element("urlFormat");
		urlFormat.setText(info.getUrlFormat());
		Element driverClass = next.element("driverClass");
		driverClass.setText(info.getDriverClass());
		Element fileUrl = next.element("fileUrl");
		fileUrl.setText(info.getFileUrl());
		OutputFormat format = OutputFormat.createPrettyPrint();// 创建格式化模版对象
		format.setEncoding("UTF-8");
		format.setIndent(true); // 设置是否缩进
		format.setIndent("    "); // 以空格方式实现缩进
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(property+ "\\config\\jdbcConfig.xml"), format);
		format.setNewlines(true); // 设置是否换行
		xmlWriter.write(document);
		xmlWriter.flush();
		xmlWriter.close();	
		return true;
	}
	

	public void deleteElement(DriverPathInfo selectedValue) throws IOException {
		Element xmlRoot = getXMLRoot().getRootElement();;
		
		Iterator element= xmlRoot.elementIterator("jdbcPaths");
		Element ele =  ((Element)element.next());
		Iterator elementIterator = ele.elementIterator();
		while(elementIterator.hasNext()){
			Element next = (Element) elementIterator.next();
			String name = next.elementText("name");
			if(name.equals(selectedValue.getName())){
				ele.remove(next);
			}
		}
		OutputFormat format = OutputFormat.createPrettyPrint();// 创建格式化模版对象
		format.setEncoding("UTF-8");
		format.setIndent(true); // 设置是否缩进
		format.setIndent("    "); // 以空格方式实现缩进
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(property+ "\\config\\jdbcConfig.xml"), format);
		format.setNewlines(true); // 设置是否换行
		xmlWriter.write(xmlRoot);
		xmlWriter.flush();
		xmlWriter.close();	
	}
	/////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 得到所有数据库类型
	 */
	
	private void getDataBaseTypesArr(){
		dataBaesTypes = new Hashtable<String, DataBaseType>();
		Element xmlRoot = getXMLRoot().getRootElement();
		Iterator element = xmlRoot.elementIterator("dataBaseTypes");
		Element ele = ((Element) element.next());
		Iterator elementIterator = ele.elementIterator();
		dataBaseTypeList= new ArrayList<DataBaseType>();
		while (elementIterator.hasNext()) {
			Element next = (Element) elementIterator.next();
			String name = next.elementText("name");
			String classUrl = next.elementText("classUrl");
			DataBaseType dataBaseTye = new DataBaseType(name,classUrl);
			dataBaesTypes.put(name, dataBaseTye);
			dataBaseTypeList.add(dataBaseTye);
		}
	}
}

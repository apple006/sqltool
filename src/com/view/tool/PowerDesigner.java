package com.view.tool;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.entity.LoginInfo;
import com.view.tool.entity.PDColumnEntity;
import com.view.tool.entity.PDTableEntity;

/**
 * 解析XML构建的tree
 * @author Administrator
 *
 */
public class PowerDesigner{
	private static Object o = new Object();
	private final String property = System.getProperty("user.dir");
	private static  PowerDesigner powerDesigner;
	private Hashtable<LoginInfo,Hashtable<String, PDTableEntity>> tables;
	private PowerDesigner(){
		tables = new Hashtable<LoginInfo, Hashtable<String,PDTableEntity>>();
//		initData();
	}
	public static PowerDesigner  initPowerDesigner(){
		if(powerDesigner==null){
			synchronized (o) {
				if(powerDesigner == null){
					powerDesigner = new PowerDesigner();
				}
			}
		}
		return powerDesigner;
	}
	public PDTableEntity getTable(LoginInfo login,String tableCode){
		if(tables.containsKey(login)){
//			boolean containsKey = tables.get(login).containsKey(tableCode);
			return tables.get(login).containsKey(tableCode)?tables.get(login).get(tableCode):new PDTableEntity("","");
		}
		return new PDTableEntity("","");
	}
	public PDColumnEntity getTable(LoginInfo login,String tableCode,String columnCode){
		if(tables.containsKey(login)){
			if(tables.get(login).containsKey(tableCode)){
				return tables.get(login).get(tableCode).getColumn(columnCode);
			}
			if(tables.get(login).containsKey(tableCode.toUpperCase())){
				return tables.get(login).get(tableCode.toUpperCase()).getColumn(columnCode.toUpperCase());
			}
		}
		return new PDColumnEntity(); 
	}
	private Document getXMLRoot(LoginInfo info) throws DocumentException{
		SAXReader saxReader = new SAXReader();
		Document document = null;
		document = saxReader.read(new File(info.getPdm()));
		return document;
	}
	
	public static void main(String[] args) {
		new  PowerDesigner();
	}
	
	public void initData(LoginInfo info) throws DocumentException{
		if("".equals(info.getPdm().trim())){
			return;
		}
		Document doc = getXMLRoot(info);
		Element rootElement = doc.getRootElement();
		List selectNodes = rootElement.selectNodes("//c:Tables//o:Table");
		Iterator elementIterator = selectNodes.iterator();
		int i = 0;
		Hashtable<String, PDTableEntity> hashtable;
		if(tables.containsKey(info)){
			 hashtable = tables.get(info);
		}else{
			hashtable = new Hashtable<String, PDTableEntity>();
			tables.put(info, hashtable);
		}
		
		boolean comment = isComment();
		while(elementIterator.hasNext()){
			Element element = (Element)elementIterator.next();
			List<Element> tableName = element.selectNodes("a:Name[last()]");
			List<Element> tableCode = element.selectNodes("a:Code[last()]");
			PDTableEntity pdTable = new PDTableEntity(tableName.get(0).getText(), tableCode.get(0).getText());
			 Iterator<Element> iterator = element.selectNodes("c:Columns/*").iterator();
			while(iterator.hasNext()){
				PDColumnEntity column = new PDColumnEntity();
				Element next = iterator.next();
				List<Element> columnName =next.selectNodes("a:Name");
				List<Element> columnCode =next.selectNodes("a:Code");
				List<Element> columnComment =next.selectNodes("a:Comment");
				List<Element> columnDataType =next.selectNodes("a:DataType");
				List<Element> columnLength =next.selectNodes("a:Length");
				
				column.setColumnName(columnName.isEmpty()?"":columnName.get(0).getText());
				if(comment){
					column.setColumnComment(columnComment.isEmpty()?"":columnComment.get(0).getText());
				}else{
					column.setColumnComment(columnName.isEmpty()?"":columnName.get(0).getText());
				}
				column.setColumnCode(columnCode.isEmpty()?"":columnCode.get(0).getText());
				column.setDataType(columnComment.isEmpty()?"":columnComment.get(0).getText());
				column.setColumnLength(columnLength.isEmpty()?"":columnLength.get(0).getText());
				pdTable.addColumn(column);
			}

			hashtable.put(tableCode.get(0).getText(), pdTable);
		}
	}
	private boolean isComment() {
		String comment_field = HelpProperties.GetValueByKey("keyvalue.properties", "comment_field");

		boolean isComment = true;
		if("name".equals(comment_field)){
			isComment = false;
		}
		return isComment;
	}
	
	public void clear(LoginInfo info) throws DocumentException{
		tables.clear();
		initData(info);
	}
}

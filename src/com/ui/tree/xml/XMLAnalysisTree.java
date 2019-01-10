package com.ui.tree.xml;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.entity.LoginInfo;
import com.ui.XMLConnectionsConfig;
import com.ui.tree.AnalysisTree;
import com.ui.tree.IconMutableTreeNode;

/**
 * 解析XML构建的tree
 * @author Administrator
 *
 */
public class XMLAnalysisTree implements AnalysisTree{
	private static XMLAnalysisTree xmlTree;
	private static Object o = new Object();
	private final String property = System.getProperty("user.dir");
	private  XMLConnectionsConfig conns;
	public XMLAnalysisTree(){
		conns = new XMLConnectionsConfig();
	}
	private Element getXMLRoot(){
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(property + "\\config\\dataBaseTree.xml");
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		return document.getRootElement();
	}
	/**
	 * 初始化所有已存在于该树下的孩子节点
	 */
	@Override
	public  IconMutableTreeNode addChilds() {
		Element root = getXMLRoot();
		String attribute = root.attributeValue("name");
		String ico = root.attributeValue("ico");
		String action = root.attributeValue("do");
		IconMutableTreeNode treeRoot = new IconMutableTreeNode(attribute,ico,action);
		searchAll(root,treeRoot);
		return treeRoot;
	}
	
	/**
	 * 添加一个二级节点（连接数据库节点）
	 */
	public  IconMutableTreeNode addChild() throws IOException {
		Element root = (Element) getXMLRoot();
		Iterator<Element> elementIterator = root.elementIterator();
		Element next = elementIterator.next();
		String ico = next.attributeValue("ico");
		String action = next.attributeValue("do");
		LoginInfo connectionInfo = new LoginInfo();
		IconMutableTreeNode treeRoot = new IconMutableTreeNode(connectionInfo,ico,action);
		 Element rootNext= (Element) root.elementIterator().next();
		 searchAllElement(rootNext,treeRoot);
		return treeRoot;
	}
	private void searchIco(Element root, IconMutableTreeNode treeRoot) {
		
	}
	@SuppressWarnings("unchecked")
	private void searchAll(Element e,DefaultMutableTreeNode node){
		
		Hashtable<String, LoginInfo> connections = conns.getConnectionInfos();
		Set<String> keySet = connections.keySet();
		Iterator<String> iterator = keySet.iterator();
		
		while(iterator.hasNext()){
			String key = iterator.next();
			LoginInfo connectionInfo = connections.get(key);
			Iterator<Element> root = e.elementIterator();
			while(root.hasNext()){
				Element next = root.next();
				String name = next.attributeValue("name");
				String ico = next.attributeValue("ico");
				String action = next.attributeValue("do");
				IconMutableTreeNode leaf = new IconMutableTreeNode(connectionInfo,ico,action);
//				
				node.add(leaf);
				
				if(next.elementIterator().hasNext()){
					searchAllElement(next,leaf);
				}
				node.add(leaf);
			}
		}
		return;
	}
	private void searchAllElement(Element e, IconMutableTreeNode node) {
		Iterator<Element> root = e.elementIterator();
		while(root.hasNext()){
			Element next = root.next();
			String name = next.attributeValue("name");
			String ico = next.attributeValue("ico");
			String action = next.attributeValue("do");
			IconMutableTreeNode leaf = new IconMutableTreeNode(name,ico,action);
//			
			node.add(leaf);
			if(next.elementIterator().hasNext()){
				searchAllElement(next,leaf);
			}
		}
	}
	
}

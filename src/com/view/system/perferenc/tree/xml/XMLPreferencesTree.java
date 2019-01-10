package com.view.system.perferenc.tree.xml;


import java.io.IOException;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.entity.PreferencInfo;
import com.ui.XMLConnectionsConfig;
import com.ui.tree.AnalysisTree;
import com.ui.tree.IconMutableTreeNode;



/**
 * 解析XML构建的tree
 * @author Administrator
 *
 */
public class XMLPreferencesTree implements AnalysisTree{
	private static AnalysisTree xmlTree;
	private static Object o = new Object();
	private XMLPreferencesTree(){}
	private final String property = System.getProperty("user.dir");
	private XMLConnectionsConfig info = XMLConnectionsConfig.getConnDataBaseInfo();
	public static AnalysisTree getPreferencesTree(){
		if(xmlTree==null){
			synchronized (o) {
				xmlTree = new XMLPreferencesTree();
			}
		}
		else{
			return xmlTree;
		}
		return xmlTree;
	}
	private Element getXMLRoot(){
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(property + "\\config\\preperencesTree.xml");
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
		
		String name = root.attributeValue("name");
		String ico = root.attributeValue("ico");
		String doAction = root.attributeValue("do");
		String view = root.attributeValue("view");
		PreferencInfo prefer = new PreferencInfo(name,view);
		IconMutableTreeNode treeRoot = new IconMutableTreeNode(prefer,ico,doAction);
		searchAll(root,treeRoot);
		return treeRoot;
	}
	

	@SuppressWarnings("unchecked")
	private void searchAll(Element e,DefaultMutableTreeNode node){
		
		Iterator<Element> root = e.elementIterator();
		while(root.hasNext()){
			
			Element next = root.next();
			String name = next.attributeValue("name");
			String ico = next.attributeValue("ico");
			String action = next.attributeValue("do");
			String view = next.attributeValue("view");
			PreferencInfo prefer = new PreferencInfo(name,view);
			IconMutableTreeNode leaf = new IconMutableTreeNode(prefer,ico,action);
			
			node.add(leaf);
			if(next.elementIterator().hasNext()){
				searchAll(next,leaf);
			}
		}
		return;
	}
	@Override
	public IconMutableTreeNode addChild() throws IOException {
		return null;
	}
	
}

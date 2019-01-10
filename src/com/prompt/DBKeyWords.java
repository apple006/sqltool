package com.prompt;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dao.Dao;

public class DBKeyWords {
	private HashMap<String, MutableAttributeSet> keywords;
	private HashMap<String, MutableAttributeSet> redisKeywords;
	private static final Object o = new Object();
	private MutableAttributeSet keyWord;
	private MutableAttributeSet redisKeyWord;
	private MutableAttributeSet funWord;
	private MutableAttributeSet normalAttr;
	private static DBKeyWords dbKeyWords ;
	private final String prompKeyWordsPath =System.getProperty("user.dir")+"\\config\\keywords.xml";
	private final String prompRedisKeyWordsPath =System.getProperty("user.dir")+"\\config\\rediskeywords.xml";
	private final String prompFunctionPath =System.getProperty("user.dir")+"\\config\\prompt.xml";
	private  DBKeyWords(){
		keywords = new HashMap<String, MutableAttributeSet>();
		redisKeywords = new HashMap<String, MutableAttributeSet>();
		keyWord = new SimpleAttributeSet();
		funWord = new SimpleAttributeSet();
		normalAttr = new SimpleAttributeSet();
		redisKeyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord,Color.GRAY);
		StyleConstants.setBold(keyWord,true);
		StyleConstants.setForeground(funWord, Color.blue);
		StyleConstants.setBold(funWord,true);
		StyleConstants.setForeground(redisKeyWord, Color.orange);
		StyleConstants.setBold(redisKeyWord,true);

		readKeyWords();
	}
	public static DBKeyWords getDBKeyWorods(){
		if(dbKeyWords==null){
			synchronized (o) {
				if(dbKeyWords==null){
					dbKeyWords = new DBKeyWords();
				}
			}
		}
		return dbKeyWords;
	}
	private void readKeyWords(){
		Document xmlRoot = getXMLRoot(prompKeyWordsPath);
		Element rootElement = xmlRoot.getRootElement();
		Iterator nextElement = rootElement.elementIterator();
		while(nextElement.hasNext()){
			Element next = (Element) nextElement.next();
			String name = next.getText();
			keywords.put(name.toUpperCase(),funWord);
		}
		Document functionKey = getXMLRoot(prompFunctionPath);
		Element rootFunction = functionKey.getRootElement();
		Iterator nextFunctionElement = rootFunction.elementIterator();
		while(nextFunctionElement.hasNext()){
			Element next = (Element) nextFunctionElement.next();
			Element element = next.element("name");
			String name = element.getText();
			if(keywords.containsKey(name)){
				keywords.remove(name);
				keywords.put(name.toUpperCase(), keyWord);
			}
			keywords.put(name.toUpperCase(),keyWord);
		}
		Document prompRedisKeyWordsPathRoot = getXMLRoot(prompRedisKeyWordsPath);
		Element reids = prompRedisKeyWordsPathRoot.getRootElement();
		Iterator nextRedisKey = reids.elementIterator();
		while(nextRedisKey.hasNext()){
			Element next = (Element) nextRedisKey.next();
			String name = next.getText();
			redisKeywords.put(name.toUpperCase(),redisKeyWord);
		}
	}
	public boolean hasKey(String key){
		return keywords.containsKey(key.toUpperCase());
	}
	
	/**
	 * 根据传入的key找到相应key的颜色
	 * @param key
	 * @return
	 */

	public MutableAttributeSet getKeyWord(String key){
		MutableAttributeSet color = keywords.get(key.trim().toUpperCase());
		return color==null?normalAttr:color ;
	}
	private Document getXMLRoot(String path){
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(path);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		return document;
	}
	public void isKey() {
		
	}
	public String[] getKeyWords() {
		String[] array = keywords.keySet().toArray(new String[0]);
		return array;
	}
	public String[] getRedisKeyWords() {
		String[] array = redisKeywords.keySet().toArray(new String[0]);
		return array;
	}
	public int getSize(){
		return keywords.size();
	}
}

package com.prompt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ActionMap;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.log4j.Logger;

import com.dnd.FileToTabDnd;
import com.entity.LoginInfo;
import com.ui.extensible.UITabbedPane;
import com.view.sqloperate.QuerySqlTab;

import main.SQLTool;


public class DBKeyTextPane extends JTextPane {

	private static final long serialVersionUID = -66377652770879651L;
	protected StyleContext m_context;
	protected DefaultStyledDocument m_doc;
	private MutableAttributeSet keyAttr, normalAttr;
	private MutableAttributeSet bracketAttr;
	private MutableAttributeSet inputAttributes = new RTFEditorKit().getInputAttributes();
	private DBKeyWords promptMessage = DBKeyWords.getDBKeyWorods();
	
	
	private MutableAttributeSet string ;
	private MutableAttributeSet normalStyle;
	int j,i;
	private static Object o = new Object();
	Logger logger = Logger.getLogger(getClass());

	private PrompDocument promp;
	
	
	public DBKeyTextPane() {
		promp	= new PrompDocument(this);
		this.setDocument(promp);
//		super(promp);
		m_doc = (DefaultStyledDocument) this.getDocument();
//		ActionMap actionMap2 = this.getActionMap();
		getInputMap().put(KeyStroke.getKeyStroke("control V"), new MyPasteAction(this));

//		m_doc.addDocumentListener(new SyntaxHighlighter(this));
		m_context = new StyleContext();

		keyAttr = new SimpleAttributeSet();
		StyleConstants.setForeground(keyAttr, Color.black);

		normalAttr = new SimpleAttributeSet();
		StyleConstants.setBold(normalAttr, false);
		StyleConstants.setForeground(normalAttr, Color.black);
		bracketAttr = new SimpleAttributeSet();
		string =new SimpleAttributeSet();
		StyleConstants.setForeground(string, Color.red);
	

	}

	
	/**
	 * 
	 * @param i
	 * @param j
	 */
	public void setSelectText(int i,int j){
		 setSelectionStart(i);
		 setSelectionEnd(j);
	}
	/**

	 * 
	 * @param _start
	 * @param _end
	 */
	public void dealText(int _start, int _end) {

		String text = "";
		try {
			text = m_doc.getText(_start, _end - _start).toUpperCase();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		setSemiColor(_start, text);
	}

	public String getDataType(){
		UITabbedPane tabQuerySql = SQLTool.getSQLTool().getToolFrame().getSqlView().getTabQuerySql();
		Component selectedComponent = tabQuerySql.getSelectedComponent();
		int selectedIndex = tabQuerySql.getSelectedIndex();
		QuerySqlTab editSqlTab = (QuerySqlTab)selectedComponent;
		DBKeyTextPane editSqlText = editSqlTab.getEditSqlText();
		LoginInfo info = editSqlTab.getConnectionInfo();
		return info.getDataType();
	}
	public void setSemiColor(int start, String text) {
		Pattern per1 = Pattern.compile("(\'.*?\')", Pattern.CASE_INSENSITIVE);
		Matcher match1 = per1.matcher(text);
//		SwingUtilities.invokeLater(new ColouringTask(this, start, text.length(), normalStyle));
		Pattern wordPat = Pattern.compile("(\\w+)", Pattern.CASE_INSENSITIVE);
		Matcher matchWord = wordPat.matcher(text);
		int wordStart =0;

		while(matchWord.find()){
			String group = matchWord.group();
			int indexOf1 = text.indexOf(group.toUpperCase(),wordStart);
			if(indexOf1==-1){
				continue;
			}
			SwingUtilities.invokeLater(new ColouringTask(promp, indexOf1+start, group.length(),promptMessage.getKeyWord( group)));
			wordStart = indexOf1 + group.length();
		}
		int start1 =0;
		while(match1.find()){
			String group = match1.group();
			int indexOf1 = text.indexOf(group.toUpperCase(),start1);
			if(indexOf1==-1||group.length()<2){
				continue;
			}
			SwingUtilities.invokeLater(new ColouringTask(promp, indexOf1+start, group.length(), string));
			start1 = indexOf1 + group.length();
		}
	}

	/**
	
	 */
	public void dealSingleRow() {
		Element root = m_doc.getDefaultRootElement();
	
		int cursorPos = this.getCaretPosition(); // 
		int line = root.getElementIndex(cursorPos);// 
		Element para = root.getElement(line);
		int start = para.getStartOffset();
		int end = para.getEndOffset() - 1;
	}

	/**
	
	 * 
	 * @return
	 * @throws BadLocationException
	 */
	public String getCursRow() throws BadLocationException {
		Element root = m_doc.getDefaultRootElement();
	
		int cursorPos = this.getCaretPosition(); //
		int line = root.getElementIndex(cursorPos);//
		Element para = root.getElement(line);
		int start = para.getStartOffset();
		int end = para.getEndOffset() - 1;//
		return m_doc.getText(start, end);
	}

	/**
	 * ?????????????????
	 * 
	 * @return
	 * @throws BadLocationException
	 */
	public String getCursLastWrod() throws BadLocationException {
		Element root = m_doc.getDefaultRootElement();
		
		int cursorPos = this.getCaretPosition(); //
		int line = root.getElementIndex(cursorPos);//
		Element para = root.getElement(line);
		int indexOfWordStart = indexOfWordAndSmartStart(m_doc,cursorPos);
		String text = m_doc.getText(indexOfWordStart, cursorPos-indexOfWordStart);
		if(".".equals(text)) {
			 indexOfWordStart = indexOfWordAndSmartStart(m_doc,cursorPos-1);
			 text = m_doc.getText(indexOfWordStart, cursorPos-1-indexOfWordStart);

		}
		return text.replaceAll("\\s+", "");
	}
	
	public String getSql() throws BadLocationException {
		String sql = this.getText();
//		sql.length();
		int cursorPos = this.getCaretPosition(); //
//		char[] charArray = sql.toCharArray();
//		int i = charArray.length;
//		for ( i = cursorPos-1; i > 0; i--) {
//			if(charArray[i] == ';'){
//				break;
//			}
//		}
//		i =	i>0?i+1:0;
//		int endOf = sql.indexOf(";", cursorPos);
//		endOf = endOf==-1?sql.length():endOf;
//		String sql1 = sql.substring(i, endOf)+"  ";
		String[] split = sql.split(";");
		for (int i = 0; i <= split.length; i++) {
			if(cursorPos<=split[i].length()) {
				return split[i];
			}else {
				cursorPos=cursorPos-split[i].length()-1;
			}
		}
		return sql;
	}
	
	

	/**
	
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int indexOfWordStart(Document doc, int pos) throws BadLocationException {
		
		for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);

		return pos;
	}
	
	public int indexOfWordAndSmartStart(Document doc, int pos) throws BadLocationException {
		
		for (; pos > 0 && isWordCharacterAndSmart(doc, pos - 1); --pos);

		return pos;
	}
	public boolean isWordCharacterAndSmart(Document doc, int pos) throws BadLocationException {
		char ch = getCharAt(doc, pos);
		
		if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_'||ch=='.') { return true; }
		return false;
	}

	/**
	
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {
		
		for (; isWordCharacter(doc, pos); ++pos);

		return pos;
	}
	/**
	
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public boolean isWordCharacter(Document doc, int pos) throws BadLocationException {
		char ch = getCharAt(doc, pos);
		
		if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') { return true; }
		return false;
	}
	/**
	
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public char getCharAt(Document doc, int pos) throws BadLocationException {
		return doc.getText(pos, 1).charAt(0);
	}

	

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public void setSize(Dimension d) {
		if (d.width < getParent().getSize().width) {
			d.width = getParent().getSize().width;
		}
		super.setSize(d);
	};

	public void insertSelectWord(String str, String likeWord) {
		if (str == null) {
			return;
		}
		try {
			
			int caretPosition = this.getCaretPosition();
//			System.out.println(caretPosition+"==="+likeWord);

			String replaceAll = likeWord.replaceAll("\\s", "");
			int i = caretPosition - replaceAll.length();
//			System.out.println(str+"===="+caretPosition+"===="+likeWord+"=="+replaceAll+"=="+i+"=="+m_doc.getLength());
			m_doc.remove(i, replaceAll.length());
			m_doc.insertString(i, str, normalAttr);
//			m_doc.replace(i, replaceAll.length(), "", normalAttr);
		} catch (BadLocationException e1) {
			logger.error("",e1);
		}
	}



	
	public void insertCurrWord(String selectWord) throws BadLocationException {
		if (selectWord == null) {
			return;
		}
		int caretPosition = this.getCaretPosition();
		m_doc.insertString(caretPosition, selectWord, normalAttr);
	}
	
	
	public void setWordColor(int startPos,int endPos ) throws BadLocationException{
//		new_pos=new_pos>0?new_pos-1:new_pos;
		
		
		 String dealSingleRow = m_doc.getText(startPos,endPos).toUpperCase();
//		String dealSingleRow = dealSingleRow(0,jtextPane.getText().length());
//
		Pattern wordPat = Pattern.compile("(\\w+)", Pattern.CASE_INSENSITIVE);
		Matcher matchWord = wordPat.matcher(dealSingleRow);
		int wordStart =0;

		while(matchWord.find()){
			String group = matchWord.group();
			int indexOf1 = dealSingleRow.indexOf(group.toUpperCase(),wordStart);
			if(indexOf1==-1){
				continue;
			}
			SwingUtilities.invokeLater(new ColouringTask(m_doc, indexOf1+startPos, group.length(), 	promptMessage.getKeyWord( group)));
			wordStart = indexOf1 + group.length();
		}
//		
//		
//		
		Pattern per1 = Pattern.compile("(\'.*?\')", Pattern.CASE_INSENSITIVE);
		Matcher match1 = per1.matcher(dealSingleRow);
		int start1 =0;
		while(match1.find()){
			String group = match1.group();
			int indexOf1 = dealSingleRow.indexOf(group.toUpperCase(),start1);
			if(indexOf1==-1){
				continue;
			}
			SwingUtilities.invokeLater(new ColouringTask(m_doc, indexOf1+startPos, group.length(), string));
			start1 = indexOf1 + group.length();
		}
//		int fristWord= indexOfWordStart(m_doc,startPos);
//		int lastWord = indexOfWordEnd(m_doc,endPos);
//		String dealSingleRow = dealSingleRow(fristWord,lastWord);
//		Pattern per = Pattern.compile("(\\w+)", Pattern.CASE_INSENSITIVE);
//		Matcher match = per.matcher(dealSingleRow);
//		int start =0;
//		if(startPos!=fristWord){
//			SwingUtilities.invokeLater(new ColouringTask(m_doc, fristWord, startPos, normalStyle));
//		}
//		
//		if(endPos!=lastWord){
//			SwingUtilities.invokeLater(new ColouringTask(m_doc, endPos, lastWord-endPos, normalStyle));
//		}
//		while(match.find()){
//			String group = match.group();
//			int indexOf = dealSingleRow.indexOf(group.toUpperCase(),start);
//			if(!promptMessage.hasKey(group)){
//				start = indexOf + group.length();
//				continue;
//			}
//			MutableAttributeSet keyWordArrt = promptMessage.getKeyWord(group);
//			SwingUtilities.invokeLater(new ColouringTask(m_doc, indexOf+fristWord, group.length(), keyWordArrt));
////			m_doc.setCharacterAttributes(indexOf+start,group.length(), keyWordArrt, true);
//			start = indexOf + group.length();
//		}
		this.repaint();
	} 
	
	public String dealSingleRow(int cursorPos, int end)
			throws BadLocationException {
		return m_doc.getText(cursorPos, end - cursorPos).toUpperCase();
	}


	public void getSaveFile() {
		
	}
}


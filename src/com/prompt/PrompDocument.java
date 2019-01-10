package com.prompt;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class PrompDocument extends DefaultStyledDocument {
	private boolean b;
	private DBKeyWords promptMessage = DBKeyWords.getDBKeyWorods();
	private String removeString = "";
	private MutableAttributeSet string = new SimpleAttributeSet();
	private MutableAttributeSet normalAttr = new SimpleAttributeSet();
	private Style keywordStyle;
	private Style normalStyle;
	private DBKeyTextPane  pane;
	public PrompDocument(DBKeyTextPane dbKeyTextPane) {
		this.pane = dbKeyTextPane;
		keywordStyle = this.addStyle("Keyword_Style", null);
		normalStyle = this.addStyle("Keyword_Style", null);
		StyleConstants.setForeground(keywordStyle, Color.RED);
		StyleConstants.setForeground(normalStyle, Color.BLACK);
		StyleConstants.setForeground(string, Color.red);
	}

	@Override
	public void insertString(int paramInt, String paramString,
			AttributeSet paramAttributeSet) throws BadLocationException {
 		if(paramString.indexOf('\r')!=-1||paramString.indexOf('\n')!=-1){
// 			StyleConstants.setFontFamily(normalStyle, "楷体");
			super.insertString(paramInt, paramString, normalStyle);
			setRowColor();
			return;
		}
		if (paramString.length()==1&&(paramString.indexOf('\'')==-1)) {
			int singleMarksNum = singleMarksNum();
			if(singleMarksNum%2!=0){
				super.insertString(paramInt, paramString, string);
			}else{
				super.insertString(paramInt, paramString, normalStyle);
				colouring(this, paramInt, 1);
			}
		} else {
			super.insertString(paramInt, paramString, string);
			setRowColor();
		}
	}
	
	private void setRowColor() throws BadLocationException{
		Element root = this.getDefaultRootElement();
		int cursorPos = pane.getCaretPosition(); 
		int line = root.getElementIndex(cursorPos);
		Element para = root.getElement(line);
		int start = para.getStartOffset();
		int end = para.getEndOffset() ;// 除\r字符
		dealText(start, end);
	}
	
	private int singleMarksNum() throws BadLocationException{
		char[] charArray = getRowStr().toCharArray();
		int num = 0;
		for (int i = 0; i < charArray.length; i++) {
			if(charArray[i]=='\''){
				num++;
			}
			
		}
		 return num;
	}
	
	/**
	 * 得到鼠标当前行文本
	 * @return
	 * @throws BadLocationException
	 */
	private String getRowStr() throws BadLocationException{
		Element root = this.getDefaultRootElement();
		int cursorPos = pane.getCaretPosition(); 
		int line = root.getElementIndex(cursorPos);
		Element para = root.getElement(line);
		int start = para.getStartOffset();
		return this.getText(start, cursorPos - start).toUpperCase();
	}
	
	public void dealText(int start, int end) throws BadLocationException {
		String text = this.getText(start, end - start).toUpperCase();
		setSemiColor(start, text);
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
			SwingUtilities.invokeLater(new ColouringTask(this, indexOf1+start, group.length(), 	promptMessage.getKeyWord(group)));
			wordStart = indexOf1 + group.length();
		}
		int start1 =0;
		while(match1.find()){
			String group = match1.group();
			int indexOf1 = text.indexOf(group.toUpperCase(),start1);
			if(indexOf1==-1||group.length()<2){
				continue;
			}
			SwingUtilities.invokeLater(new ColouringTask(this, indexOf1+start, group.length(), string));
			start1 = indexOf1 + group.length();
		}
	
	}
	@Override
	public void remove(int paramInt, int paramInt2)
			throws BadLocationException {
		if(paramInt2-paramInt>1){
			
		}
//		int firstCharIndex = Tool.getFirstWordChar(this, paramInt)+1;
		String charAt = pane.getText(paramInt, paramInt2);
//		char charAt = Tool.getCharAt(this, firstCharIndex);
		super.remove(paramInt, paramInt2);
		int singleMarksNum = singleMarksNum();
		if(charAt=="\'"||charAt=="\n"||charAt=="\r"||singleMarksNum%2!=0){
			setRowColor();
		}else{
			if(charAt.length()==1){
				colouring(this, paramInt, 0);
			}
		}
	}
	public String dealSingleRow(int cursorPos, int end)
			throws BadLocationException {
		return this.getText(cursorPos, end - cursorPos).toUpperCase();
	}
	public String getRemoveString() {
		return removeString;
	}
	
	public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {
		int start = indexOfWordStart(doc, pos);
		int end = indexOfWordEnd(doc, pos + len);
		char ch;
		while (start < end) {
			ch = getCharAt(doc, start);
			if (Character.isLetter(ch) || ch == '_') {
				start = colouringWord(doc, start);
			} else {
				SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
				++start;
			}
		}
	}
	/**
	 * 取得下标为pos时, 它所在的单词开始的下标.
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int indexOfWordStart(Document doc, int pos) throws BadLocationException {
		// 从pos开始向前找到第一个非单词字符.
		for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);

		return pos;
	}
	/**
	 * 如果一个字符是字母, 数字, 下划线, 则返回true.
	 * 
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
	 * 取得在文档中下标在pos处的字符.
	 * 
	 * 如果pos为doc.getLength(), 返回的是一个文档的结束符, 不会抛出异常. 如果pos<0, 则会抛出异常.
	 * 所以pos的有效值是[0, doc.getLength()]
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public char getCharAt(Document doc, int pos) throws BadLocationException {
		return doc.getText(pos, 1).charAt(0);
	}

	/**
	 * 对单词进行着色, 并返回单词结束的下标.
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {
		int wordEnd = indexOfWordEnd(doc, pos);
		String word = doc.getText(pos, wordEnd - pos);
		
		if(promptMessage.hasKey(word)){
			MutableAttributeSet keyWordArrt = promptMessage.getKeyWord(word) ;
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos,keyWordArrt ));
		}
		else{
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));
		}
		return wordEnd;
	}
	
	/**
	 * 取得下标为pos时, 它所在的单词结束的下标. Â±wor^dÂ± (^表示pos, Â±表示开始或结束的下标)
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {
		// 从pos开始向前找到第一个非单词字符.
		for (; isWordCharacter(doc, pos); ++pos);

		return pos;
	}
}

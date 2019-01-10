package com.prompt;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

/**
 * 当文本输入区的有字符插入或者删除时, 进行高亮.
 * 
 * 要进行语法高亮, 文本输入组件的document要是styled document才行. 所以不要用JTextArea. 可以使用JTextPane.
 * 
 * @author Biao
 * 
 */
public class SyntaxHighlighter implements DocumentListener {
	private DefaultStyledDocument doc ;
	private Style keywordStyle;
	private Style normalStyle;
	private DBKeyTextPane editor;
	private MutableAttributeSet string = new SimpleAttributeSet();

	private DBKeyWords promptMessage = DBKeyWords.getDBKeyWorods();
	

	public SyntaxHighlighter(){
		
	}
	public SyntaxHighlighter(DBKeyTextPane editor) {
		// 准备着色使用的样式
		this.editor = editor;
		keywordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		doc = (DefaultStyledDocument) editor.getDocument();
		StyleConstants.setForeground(keywordStyle, Color.RED);
		StyleConstants.setForeground(normalStyle, Color.BLACK);
		StyleConstants.setForeground(string, Color.red);

	}
	
	public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {
		// 取得插入或者删除后影响到的单词.
		// 例如"public"在b后插入一个空格, 就变成了:"pub lic", 这时就有两个单词要处理:"pub"和"lic"
		// 这时要取得的范围是pub中p前面的位置和lic中c后面的位置
		int start = indexOfWordStart(doc, pos);
		int end = indexOfWordEnd(doc, pos + len);

		char ch;
		while (start < end) {
			ch = getCharAt(doc, start);
			if (Character.isLetter(ch) || ch == '_') {
				// 如果是以字母或者下划线开头, 说明是单词
				// pos为处理后的最后一个下标
				start = colouringWord(doc, start);
			} else {
				SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
				++start;
			}
		}
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
	 * 取得下标为pos时, 它所在的单词开始的下标. Â±wor^dÂ± (^表示pos, Â±表示开始或结束的下标)
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
	
	public int indexOfCharStart(Document doc, int pos) throws BadLocationException {
		// 从pos开始向前找到第一个非单词字符.
		for (; pos > 0 && !isWordCharacter(doc, pos - 1); --pos);

		return pos;
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


	@Override
	public void insertUpdate( DocumentEvent e) {
		try {
			//如果插入字符为粘贴操作则不重复再次进行设置颜色提高效率
			int offset = e.getOffset();
			
			
			if(e.getLength()>1){
				return;
			}
			
			int firstCharIndex = Tool.getFirstWordChar(doc, offset);
			char charAt = Tool.getCharAt(doc, firstCharIndex);
			if(!Tool.isRed(doc, firstCharIndex)&&charAt!='\''){
				colouring((StyledDocument) e.getDocument(), offset, 1);
			}else{
				setRowColor();
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
	public boolean dealText(int _start, int _end,boolean is) throws BadLocationException {
		String text = doc.getText(_start, _end - _start).toUpperCase();
		is = setSemiColor(_start, text,is);
		return is;
	}
	
	public boolean setSemiColor(int _start, String text,boolean is) {
//		Pattern per1 = Pattern.compile("(\'.*?\')", Pattern.CASE_INSENSITIVE);
		boolean b = is;
		int index=_start;
		int begin_index=_start;
		int num = 0;
		char[] charArray = text.toCharArray();
		int i ;
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println(currentTimeMillis-System.currentTimeMillis());
		if(num%2!=0){
			SwingUtilities.invokeLater(new ColouringTask(doc, _start,text.length()-_start, b?string:normalStyle));
		}
		Pattern per1 = Pattern.compile("(\'.*?\')", Pattern.CASE_INSENSITIVE);

		Matcher match1 = per1.matcher(text);
		SwingUtilities.invokeLater(new ColouringTask(doc, _start, text.length(), normalStyle));
		int start1 =0;
		while(match1.find()){
			String group = match1.group();
			int indexOf1 = text.indexOf(group.toUpperCase(),start1);
			if(indexOf1==-1){
				continue;
			}
			SwingUtilities.invokeLater(new ColouringTask(doc, indexOf1+_start, group.length(), string));
			start1 = indexOf1 + group.length();
		}
		return is;
	
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		try {
			int offset = e.getOffset();
			if(e.getLength()>1){
				return;
			}
//			String removeString = doc.getRemoveString();
//			if(!"\'".endsWith(removeString)){
				colouring((StyledDocument) e.getDocument(), offset, 0);
//			}else{
//				setRowColor();
//			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
	

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
	}
	
	
	private void setRowColor() throws BadLocationException{
		Element root = doc.getDefaultRootElement();
		
		int li_count = root.getElementCount();
		boolean next=false;
		for (int i = 0; i < li_count; i++) {
			Element para = root.getElement(i);
			int start = para.getStartOffset();
			int end = para.getEndOffset() ;// 除\r字符
			next = dealText(start, end,next);
		}
	}
	
}
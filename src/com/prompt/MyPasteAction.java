package com.prompt;

import java.awt.Color;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.view.tool.HelpProperties;

class MyPasteAction extends AbstractAction {
	private DBKeyTextPane jtextPane;
	DBKeyWords promptMessage = DBKeyWords.getDBKeyWorods();
	private MutableAttributeSet string ;
	private MutableAttributeSet normalStyle;
	DefaultStyledDocument m_doc;

	
	public MyPasteAction(DBKeyTextPane jtextPane) {
		this.jtextPane = jtextPane;
		string = jtextPane.addStyle("string", null);
		normalStyle = new SimpleAttributeSet();
		StyleConstants.setForeground(normalStyle, Color.BLACK);
		StyleConstants.setForeground(string, Color.red);
		m_doc = (DefaultStyledDocument) jtextPane.getDocument();
		
	}

	
	

	
	public void actionPerformed(final ActionEvent e) {
		
		// 获得系统黏贴板
		Clipboard strBoard = jtextPane.getToolkit().getSystemClipboard();
		Transferable transferable = strBoard.getContents(jtextPane);
		for (DataFlavor flavor : transferable.getTransferDataFlavors()) {
			if (flavor.equals(DataFlavor.stringFlavor)) {
				try {
					// 如果是字符串，则取出
					String cursRow = ((String) transferable.getTransferData(DataFlavor.stringFlavor));

					// 获得jtextPane的样式化文档
//					int pos = jtextPane.getCaretPosition();
					int pos = jtextPane.getSelectionStart();
					Position startPosition = m_doc.getStartPosition();
					// 插入文本，颜色设置为黑色
					jtextPane.replaceSelection(cursRow);
					 String dealSingleRow = jtextPane.getText(pos, m_doc.getLength()-pos).toUpperCase();
//					String dealSingleRow = dealSingleRow(0,jtextPane.getText().length());
//
					int wordStart =0;
//					Boolean changeColor = new Boolean( HelpProperties.GetValueByKey("keyvalue.properties", "change_color"));

					if(true){
						Pattern wordPat = Pattern.compile("(\\w+)", Pattern.CASE_INSENSITIVE);
						Matcher matchWord = wordPat.matcher(dealSingleRow);
						while(matchWord.find()){
							String group = matchWord.group();
							int indexOf1 = dealSingleRow.indexOf(group.toUpperCase(),wordStart)+pos;
							if(indexOf1==-1){
								continue;
							}
							SwingUtilities.invokeLater(new ColouringTask(m_doc, indexOf1, group.length(), 	promptMessage.getKeyWord(group)));
							wordStart = indexOf1 + group.length()-pos;
						}
	//					
	//					
	//					
						Pattern per1 = Pattern.compile("(\'.*?\')", Pattern.CASE_INSENSITIVE);
						Matcher match1 = per1.matcher(dealSingleRow);
						int start1 =0;
						while(match1.find()){
							String group = match1.group();
							int indexOf1 = dealSingleRow.indexOf(group.toUpperCase(),start1)+pos;
							if(indexOf1==-1){
								continue;
							}
							SwingUtilities.invokeLater(new ColouringTask(m_doc, indexOf1, group.length(), string));
							start1 = indexOf1 + group.length()-pos;
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				break;
			}
		}
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
	public void setWordColor(int startPos,int endPos ) throws BadLocationException{
//		new_pos=new_pos>0?new_pos-1:new_pos;
		int fristWord= indexOfWordStart(m_doc,startPos);
		int lastWord = indexOfWordEnd(m_doc,endPos);
		String dealSingleRow = dealSingleRow(fristWord,lastWord);
		Pattern per = Pattern.compile("(\\w+)", Pattern.CASE_INSENSITIVE);
		Matcher match = per.matcher(dealSingleRow);
		int start =0;
		if(startPos!=fristWord){
			SwingUtilities.invokeLater(new ColouringTask(m_doc, fristWord, startPos, normalStyle));
		}
		
		if(endPos!=lastWord){
			SwingUtilities.invokeLater(new ColouringTask(m_doc, endPos, lastWord-endPos, normalStyle));
		}
		while(match.find()){
			String group = match.group();
			int indexOf = dealSingleRow.indexOf(group.toUpperCase(),start);
			if(!promptMessage.hasKey(group)){
				start = indexOf + group.length();
				continue;
			}
			MutableAttributeSet keyWordArrt = promptMessage.getKeyWord(group);
			SwingUtilities.invokeLater(new ColouringTask(m_doc, indexOf+fristWord, group.length(), keyWordArrt));
//			m_doc.setCharacterAttributes(indexOf+start,group.length(), keyWordArrt, true);
			start = indexOf + group.length();
		}
		jtextPane.repaint();
	}

	public String dealSingleRow(int cursorPos, int end)
			throws BadLocationException {
		return m_doc.getText(cursorPos, end - cursorPos).toUpperCase();
	}

	// 获得颜色信息
	public final AttributeSet getDivisionTextAttribute(final Color color) {

		StyleContext styleContext = StyleContext.getDefaultStyleContext();
		AttributeSet attributeSet = styleContext.addAttribute(
				SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
		return attributeSet;
	}
}
package com.prompt;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;

public class Tool {
	public static char getCharAt(Document doc, int pos) throws BadLocationException {
		return doc.getText(pos, 1).charAt(0);
	}

	public static boolean isWordCharacter(Document doc, int pos)
			throws BadLocationException {
		char ch = getCharAt(doc, pos);

		if (Character.isLetter(ch)||Character.isDefined(ch)||ch=='_') {
			return false;
		}
		return true;
	}

	/**
	 * 得到光标位置之前第一个非空字符位置
	 * @param doc
	 * @param pos 光标位置
	 * @return    非空字符位置
	 * @throws BadLocationException
	 */
	public static int getFirstCharIndex(Document doc, int pos)
			throws BadLocationException {
		char ch = getCharAt(doc, pos - 1 > 0 ? pos - 1 : pos);
		for (; pos > 0 && Character.isSpaceChar(ch); --pos) {
			ch = getCharAt(doc, pos);
		}

		return pos;
	}

	/**
	 * 得到文档中此位置的 单引号标识
	 * @param doc
	 * @param pos 文档位置
	 * @return    
	 * @throws BadLocationException
	 */
	public static boolean isMark(Document doc, int pos)
			throws BadLocationException {
		char ch = getCharAt(doc, pos-1);

		if (!Character.isSpaceChar(pos) && ch == '\'') {
			return true;
		}
		return false;
	}

	public static  int getFirstWordChar(Document doc, int pos)
			throws BadLocationException {
		// 从pos开始向前找到第一个单词字符.
		for (; pos > 0 && isWordCharacter(doc, pos ); --pos)
			;
		return pos;
	}

	/**
	 * 之前有几个连续的单引号
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public static int continuousSingleMarkSum(Document doc, int pos)
			throws BadLocationException {
		int p = pos;
		for (; pos > 0 && isMark(doc, pos ); --pos)
			;
		return p - pos;
	}

	public static int indexOfFirstSingleMark(Document doc, int pos)
			throws BadLocationException {
		// 从pos开始向前找到第一个单词字符.

		for (; pos > 0 && isMark(doc, pos - 1); --pos)
			;
		return pos;
	}

	public static boolean beforeHasSingleMark(DefaultStyledDocument doc,int pos) throws BadLocationException {
		boolean b = false;

		int firstChar = getFirstWordChar(doc, pos);
		return isRed(doc ,firstChar);
	}

	
	public static boolean getInputColor(DefaultStyledDocument doc,int pos) throws BadLocationException {
		int firstCharIndex = getFirstCharIndex(doc, pos);
		boolean red = isRed(doc,firstCharIndex);

		if(pos==0){
			return false;
		}
		if(isMark(doc,firstCharIndex)){
			int sum = continuousSingleMarkSum(doc, pos);
			int firstWordChar = getFirstWordChar(doc, firstCharIndex );
			if(sum==1){
				return !isRed(doc,firstWordChar-1);
			}
			//防止连续写单引号出现的
			if (sum % 2 == 0) {
				return isRed(doc,firstWordChar);
			} else {
				return !isRed(doc,firstWordChar);
			}
		} else {
			return isRed(doc,firstCharIndex);
		}

	}

	public static boolean isRed(DefaultStyledDocument doc ,int offset) {
		Element characterElement = doc.getCharacterElement(offset-1);
		AttributeSet attributes = characterElement.getAttributes();
		Color color = StyleConstants.getForeground(attributes);
		return color == Color.red;
	}
}

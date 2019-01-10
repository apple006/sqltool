package com.prompt;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

/**
 * 完成着色任务
 * 
 * @author Biao
 * 
 */
public class ColouringTask implements Runnable {
	private StyledDocument doc;
	private MutableAttributeSet style;
	private int pos;
	private int len;

	public ColouringTask(StyledDocument doc, int pos, int len, MutableAttributeSet style) {
		this.doc = doc;
		this.pos = pos;
		this.len = len;
		this.style = style;
	}

	public void run() {
		try {
			// 这里就是对字符进行着色
			doc.setCharacterAttributes(pos, len, style, true);
		} catch (Exception e) {
		}
	}
}
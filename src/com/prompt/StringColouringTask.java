package com.prompt;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * 完成着色任务
 * 
 */
public class StringColouringTask implements Runnable {
	private DBKeyTextPane edit;
	private MutableAttributeSet style;
	private int pos;

	public StringColouringTask(DBKeyTextPane editor, int pos) {
		this.edit = editor;
		this.pos = pos;
		style = new SimpleAttributeSet();
		StyleConstants.setForeground(style, Color.RED);
	}

	public void run() {
		try {
			// 这里就是对字符进行着色
			String dealSingleRow = edit.getDocument().getText(pos,edit.getDocument().getLength()>=100000?100000:edit.getDocument().getLength()-pos);
			Pattern per1 = Pattern.compile("(\'.*?\')", Pattern.CASE_INSENSITIVE);
			String replaceAll = dealSingleRow.replaceAll("\\s", " ");
			Matcher match1 = per1.matcher(replaceAll);
			int start1 =0;
			while(match1.find()){
				String group = match1.group();
				int indexOf1 = replaceAll.indexOf(group,start1)+pos;
				SwingUtilities.invokeLater(new ColouringTask((StyledDocument) edit.getDocument(), indexOf1, group.length(), style));
				start1 = indexOf1 + group.length();
			}
			edit.repaint();
		} catch (Exception e) {
		}
		edit.repaint();
	}
}
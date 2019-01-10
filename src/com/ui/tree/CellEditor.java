package com.ui.tree;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

public class CellEditor  extends DefaultCellEditor{

	public CellEditor(JCheckBox checkBox) {
		super(checkBox);
		// TODO 自动生成的构造函数存根
	}
	public CellEditor(final JTextField textField) {
	    super(textField);
	}
	
	public void setCellEditorValue(String str){
		((JTextField)editorComponent).setText(str);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5351334305415992526L;

}

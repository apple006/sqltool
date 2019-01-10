package com.util;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;

import main.SQLTool;

public class DynamicDialog {
	private Component parent;
	private Component comp;
	private JDialog dialog;
	private static Object o = new Object();
	private static DynamicDialog dynmicDialog;
	private DynamicDialog(){
		this(SQLTool.getSQLTool().getToolFrame().getFrame());
	}
	public DynamicDialog(JFrame frame) {
		dialog = new JDialog(frame);
		parent = frame;
	}
	
	public static DynamicDialog getDynamicDialog() {
		if(dynmicDialog==null){
			synchronized (o) {
				dynmicDialog = new DynamicDialog();
			}
		}
		return dynmicDialog;
	}
	
	public void setRootPanel(Component comp,int width,int height){
		this.comp = comp;
		dialog.add(this.comp);
		dialog.setSize(width,height);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
}

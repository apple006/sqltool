package com.view.system.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

import main.SQLTool;

public class WaringMsg  {
	public static void showMessageDialog(String msg ){
		JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), msg);
	}
	public static String showInputDialog(String msg ,Component comp){
		return JOptionPane.showInputDialog(comp,msg);
	}
	
	public static int showConfirmDialog(String msg ,Component comp){
		return JOptionPane.showConfirmDialog(comp, msg, "提示信息", JOptionPane.YES_NO_OPTION);
	}

}

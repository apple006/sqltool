package com.ui.panel;

import java.awt.Component;

import javax.swing.JPanel;


/**
 * @author Administrator
 *
 */
public interface Border {
	/**
	 * 常规边界
	 */
	final int ROUTINE = 0;
	final int MOVEMENT = 1;
	/**
	 * 设置左边面板
	 * @param comp
	 */
	public void addLeftPanel(Component comp);
	/**
	 * 设置右边面板
	 * @param comp
	 */
	public void addRightPanel(Component comp);
	/**
	 * 设置上边面板
	 * @param comp
	 */
	public void addUpPanel(Component comp);
	/**
	 * 设置下边面板
	 * @param comp
	 */
	public void addDownPanel(Component comp);
	/**
	 * 设置主面板
	 * @param comp
	 */
	public void addMainPanel(Component comp);
	
	/**
	 * 得到左边面板
	 * @param comp
	 */
	public JPanel getLeftPanel();
	/**
	 * 得到右边面板
	 * @param comp
	 */
	public JPanel getRightPanel();
	/**
	 * 得到上边面板
	 * @param comp
	 */
	public JPanel getUpPanel();
	/**
	 * 得到下边面板
	 * @param comp
	 */
	public JPanel getDownPanel();
	/**
	 * 得到主面板
	 * @param comp
	 */
	public JPanel getMainPanel();
	
	
	/**
	 * 初始化
	 */
	public void init();
	/**
	 * borderType 边界类型
	 */
	public int getBorderType();
	/**
	 * 
	 * @param borderType 设置边界类型
	 */
	public void setBorderType(int borderType);
}

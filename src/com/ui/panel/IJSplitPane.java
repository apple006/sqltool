package com.ui.panel;

import java.awt.Component;

/**
 * JSplitPane扩展功能
 * @author Administrator
 *
 */
public interface IJSplitPane {
	public void init();

	/**
	 * 设置下方容器
	 * 
	 * @param comp
	 * @roseuid 5282E1600292
	 */
	public void setDownPanel(Component comp);

	/**
	 * 设置上方容器
	 * 
	 * @param comp
	 * @roseuid 5282E16002A2
	 */
	public void setUpPanel(Component comp);

	/**
	 * 设置左方容器
	 * 
	 * @param comp
	 * @roseuid 5282E1600292
	 */
	public void setLeftPanel(Component comp);

	/**
	 * 设置右方容器
	 * 
	 * @param comp
	 * @roseuid 5282E16002A2
	 */
	public void setRightPanel(Component comp);

}

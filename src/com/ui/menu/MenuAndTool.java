package com.ui.menu;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

public interface MenuAndTool {
	/**
	 * 工具栏
	 * @param name 工具栏名称
	 * @param type 工具栏信息标识（如xml则为xml标识，如果为数据库则为相应id标识）
	 * @return
	 */
	public JToolBar getToolBar(String name,String type);
	/**
	 * 弹出菜单
	 * @param type  弹出菜单信息标识（如xml则为xml标识，如果为数据库则为相应id标识）
	 * @return
	 */
	public JPopupMenu getPopupMenu(String type) ;
	
	/**
	 * 菜单栏
	 * @param type
	 * @return
	 */
	public JMenuBar getMenu(String type)  ;
	/**
	 * 将依稀buttons存放于JPanel上
	 * @param type
	 * @return
	 */
	public JPanel getButtons(String type);
}

package com.ui.menu.listenner;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.SQLTool;

import com.UIFactory;
import com.ico.LazyImageIcon;
import com.prompt.DBKeyTextPane;
import com.ui.MenuListenner;
import com.ui.extensible.UITabbedPane;
import com.ui.extensible.listener.TabActionListener;
import com.view.sqloperate.Cache;
import com.view.sqloperate.QuerySqlTab;

public class ToolMenuAddListenner extends MenuListenner {
	@Override
	public void MouseOnClick(MouseEvent e) {
		System.out.println("新建查询");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("新建查询。。。");
		UITabbedPane tabQuerySql = SQLTool.getSQLTool().getToolFrame()
				.getSqlView().getTabQuerySql();
		LazyImageIcon lazyImageIcon = new LazyImageIcon("/imgs/tab/tabedit.png");
		QuerySqlTab querySqlTab = new QuerySqlTab(tabQuerySql);
		querySqlTab.addTabActionListener(new TabAction(querySqlTab));
		tabQuerySql.addTab("查询窗口", lazyImageIcon, querySqlTab);
	}
}

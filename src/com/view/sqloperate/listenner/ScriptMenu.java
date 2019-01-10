package com.view.sqloperate.listenner;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JMenuItem;
import javax.swing.tree.TreePath;

import main.SQLTool;

import com.entity.LoginInfo;
import com.ico.LazyImageIcon;
import com.prompt.PromptLabel;
import com.ui.MenuListenner;
import com.ui.extensible.UITabbedPane;
import com.ui.tree.IconMutableTreeNode;
import com.ui.tree.UITree;
import com.view.sqloperate.Controller;
import com.view.sqloperate.QuerySqlTab;

public class ScriptMenu extends MenuListenner{

	@Override
	public void MouseOnClick(MouseEvent e) {
		String name = ((JMenuItem) e.getSource()).getText();
		UITree uiTree = SQLTool.getSQLTool().getToolFrame().getSqlView().getDataBaseTree();
		
		TreePath selectionPath = uiTree.getSelectionPath();
		if (selectionPath != null) {
			IconMutableTreeNode selectionModel = (IconMutableTreeNode) selectionPath.getLastPathComponent();
			String username = ((PromptLabel)selectionModel.getUserObject()).getCode();
			LoginInfo info = null;
			while(selectionModel.getParent()!=null){
				if(selectionModel.getUserObject() instanceof LoginInfo){
					 info = (LoginInfo)selectionModel.getUserObject() ;
					 break;
				}
				selectionModel = (IconMutableTreeNode) selectionModel.getParent();
			}
			IconMutableTreeNode parent = (IconMutableTreeNode)selectionPath.getLastPathComponent();
			if("Table".equals(parent.getParent().toString())){
				if("±à¼­".equals(name)){
					try {
						
						Controller.newController().getTableDDL(username, info);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				if("SELECT".equals(name)){
					try {
						TreePath parentPath = selectionPath.getParentPath();
						Object[] path = selectionPath.getPath();
						Controller.newController().getSelectSql(info,path[3].toString());
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				if("É¾³ý".equals(name)){
					try {
						String[] selectNode =new String[uiTree.getSelectionCount()];
						TreePath[] selectionPaths = uiTree.getSelectionPaths();
						for (int i = 0; i < selectionPaths.length; i++) {
							IconMutableTreeNode node = (IconMutableTreeNode) selectionPaths[i].getLastPathComponent();
							selectNode[i] = node.getUserObject().toString();
						}
						Controller.newController().dropTables(selectNode,info);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			if("View".equals(parent.getParent().toString())){
				if("±à¼­".equals(name)){
					try {
						Controller.newController().getViewDDL(username, info);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			if("TableSpace".equals(parent.getParent().toString())){
				if("±à¼­".equals(name)){
					try {
						Controller.newController().getTableSpaceDDL(username,info);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
				
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
			String name = ((JMenuItem)e.getSource()).getName();
			System.out.println("11111111¡£¡£¡£"+name);
			UITabbedPane tabQuerySql = SQLTool.getSQLTool().getToolFrame().getSqlView().getTabQuerySql();
			System.out.println(new Date().getTime());
			LazyImageIcon lazyImageIcon = new LazyImageIcon("/imgs/tab/tabedit.png");
			tabQuerySql.addTab("aaa",lazyImageIcon.getIcon(),new  QuerySqlTab());
			System.out.println(new Date().getTime());
	}
}

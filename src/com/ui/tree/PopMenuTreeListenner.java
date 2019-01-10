package com.ui.tree;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.dom4j.DocumentException;

import main.SQLTool;

import com.dao.ConnectionPool;
import com.dao.DBManager;
import com.dao.DatabaseFactory;
import com.dao.ManagerThreadPool;
import com.entity.LoginInfo;
import com.sun.corba.se.pept.transport.Connection;
import com.ui.MenuListenner;
import com.ui.XMLConnectionsConfig;
import com.ui.extensible.UITabbedPane;
import com.view.sqloperate.Cache;
import com.view.sqloperate.Controller;
import com.view.sqloperate.QuerySqlTab;
import com.view.system.dialog.WaringMsg;
import com.view.tool.PowerDesigner;

public class PopMenuTreeListenner extends MenuListenner implements Refresh,
		Singleton {

	private static PopMenuTreeListenner refre;
	private static Object o = new Object();

	public PopMenuTreeListenner() {

	}

	@Override
	public void MouseOnClick(MouseEvent e) {
		if (((JMenuItem) e.getSource()).getText().equals("刷新")) {
			loadTablesInfo();
		}
		if (((JMenuItem) e.getSource()).getText().equals("连接")) {
			UITree uiTree = SQLTool.getSQLTool().getToolFrame().getSqlView()
					.getDataBaseTree();
			TreePath selectionPath = uiTree.getSelectionPath();
			DefaultMutableTreeNode pare = (DefaultMutableTreeNode) selectionPath
					.getLastPathComponent();
			if (selectionPath != null) {
				if (MouseEvent.BUTTON1 == e.getButton()) {
					IconMutableTreeNode node = (IconMutableTreeNode) selectionPath
							.getLastPathComponent();
					node.getDoAction().doDoubleClick(node);
				}

			}
		}
		if (((JMenuItem) e.getSource()).getText().equals("断开连接")) {
			UITree uiTree = SQLTool.getSQLTool().getToolFrame().getSqlView()
					.getDataBaseTree();
			TreePath selectionPath = uiTree.getSelectionPath();
			DefaultMutableTreeNode pare = (DefaultMutableTreeNode) selectionPath
					.getLastPathComponent();
			if (selectionPath != null) {
				if (MouseEvent.BUTTON1 == e.getButton()) {
					IconMutableTreeNode node = (IconMutableTreeNode) selectionPath
							.getLastPathComponent();
					LoginInfo info = (LoginInfo) node.getUserObject();
					ManagerThreadPool.getManagerThreadPool().close(info);
					info.setIsLive(ConnectionPool.IS_DIE);
					node.runIoc();

				}
				UITabbedPane tabQuerySql = SQLTool.getSQLTool().getToolFrame().getSqlView().getTabQuerySql();
				for (int i = 0; i < tabQuerySql.getTabCount(); i++) {
					tabQuerySql.getTitleAt(i);
					Component component = tabQuerySql.getComponentAt(i);
					if (component instanceof QuerySqlTab) {
						QuerySqlTab tab = (QuerySqlTab) component;
						tab.referDrowDown();
					}
					// component
				}

			}
		}
		if (((JMenuItem) e.getSource()).getText().equals("删除")) {
			UITree uiTree = SQLTool.getSQLTool().getToolFrame().getSqlView()
					.getDataBaseTree();
			TreePath selectionPath = uiTree.getSelectionPath();
			DefaultMutableTreeNode pare = (DefaultMutableTreeNode) selectionPath
					.getLastPathComponent();
			if (selectionPath != null) {
				if (MouseEvent.BUTTON1 == e.getButton()) {
					IconMutableTreeNode node = (IconMutableTreeNode) selectionPath
							.getLastPathComponent();
					LoginInfo info = (LoginInfo) node.getUserObject();
					try {
						if(WaringMsg.showConfirmDialog("删除数据源么？", SQLTool.getSQLTool().getToolFrame().getFrame())==0){
							boolean deleteLoginInfo = XMLConnectionsConfig.getConnDataBaseInfo().deleteLoginInfo(info);
							if(deleteLoginInfo){
								uiTree.removeNode(node);
							}
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		}
	}
	
	public void loadTablesInfo() {
		UITree uiTree = SQLTool.getSQLTool().getToolFrame().getSqlView().getDataBaseTree();
		TreePath selectionPath = uiTree.getSelectionPath();
		if (selectionPath != null) {
			IconMutableTreeNode selectionModel = (IconMutableTreeNode) selectionPath.getLastPathComponent();
			LoginInfo info = (LoginInfo) selectionModel.getUserObject();
			try {
				PowerDesigner.initPowerDesigner().clear(info);
				Controller.newController().getTables(info);
				Controller.newController().getViews(info);
				Controller.newController().getTableSpaces(info);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (DocumentException e1) {
				e1.printStackTrace();
				WaringMsg.showMessageDialog("PDM文件类型出错");
			}

		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	public PopMenuTreeListenner(UITree uiTree) {
		// this.uiTree = uiTree;

	}

	@Override
	public void refresh() {
		// TreeSelectionModel selectionModel = uiTree.getSelectionModel();
		// TreePath[] selectionPaths = uiTree.getSelectionPaths();
		// for (int i = 0; i < selectionPaths.length; i++) {
		// IconMutableTreeNode treeNode =
		// (IconMutableTreeNode)selectionPaths[i].getLastPathComponent();
		// Object userObject = treeNode.getUserObject();
		// if(userObject instanceof LoginInfo ){
		// LoginInfo connection = (LoginInfo) userObject;
		// int childCount = treeNode.getChildCount();
		// for (int j = 0; j < childCount; j++) {
		// IconMutableTreeNode childAt = (IconMutableTreeNode)
		// treeNode.getChildAt(j);
		// childAt.removeAllChildren();
		// childAt.add(new IconMutableTreeNode("a",childAt.getIcoName()));
		// }
		// }
		// }
	}

	@Override
	public Object newInstance() {
		if (refre == null) {
			synchronized (o) {
				if (refre == null) {
					refre = new PopMenuTreeListenner();
				}
			}
		}
		return refre;
	}

}

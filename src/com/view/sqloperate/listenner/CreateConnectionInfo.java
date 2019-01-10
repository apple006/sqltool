package com.view.sqloperate.listenner;

import java.awt.event.MouseEvent;
import java.io.IOException;

import main.SQLTool;

import com.entity.LoginInfo;
import com.ui.MenuListenner;
import com.ui.XMLConnectionsConfig;
import com.ui.extensible.UITabbedPane;
import com.ui.tree.IconMutableTreeNode;
import com.view.sqloperate.QuerySqlTab;
import com.view.sqloperate.SqlView;

/**
 * 新建数据库连接
 * @author Administrator
 *
 */
public class CreateConnectionInfo extends MenuListenner {
	
	public CreateConnectionInfo(){
		
	}
	@Override
	public void MouseOnClick(MouseEvent e) {
		SqlView sqlView = (SqlView) SQLTool.getSQLTool().getToolFrame().getSqlView();
		IconMutableTreeNode addConnection;
		try {
			addConnection = (IconMutableTreeNode) sqlView.addChild();
			XMLConnectionsConfig.getConnDataBaseInfo().addConnectionInfo((LoginInfo) addConnection.getUserObject());
			addConnection.getDoAction().doDoubleClick(addConnection);
//			referConnectDropDown(sqlView);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * 刷新所有Tab编辑中的下拉列表
	 * @param sqlView
	 */
	private void referConnectDropDown(SqlView sqlView) {
		UITabbedPane tabQuerySql = sqlView.getTabQuerySql();
		int tabCount = tabQuerySql.getTabCount();
		for (int i = 0; i < tabCount; i++) {
			QuerySqlTab editSqlTab = (QuerySqlTab) tabQuerySql.getComponentAt(i);
			editSqlTab.referDrowDown();
		}
	}
}

package com.control.doaction.tree;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import main.SQLTool;

import com.control.ITreeAction;
import com.entity.LoginInfo;
import com.ui.tree.IconMutableTreeNode;
import com.ui.tree.UITree;

public class DoTreeTableAction implements ITreeAction {

	@Override
	public void doClick() {

	}

	@Override
	public void doDoubleClick(IconMutableTreeNode treeNode) {
		IconMutableTreeNode parent = (IconMutableTreeNode) treeNode.getParent();
		LoginInfo conn = (LoginInfo) parent.getUserObject();
//			connection = conn.connection();
//			List<String> tables = OracleInfo.getOracleInfo().getTables(connection);
//			UITree dataBaseTree = SQLTool.getSQLTool().getToolFrame().getSqlView().getDataBaseTree();
//			
//			for (String name:tables) {
//				IconMutableTreeNode child = new IconMutableTreeNode(name,treeNode.getIcoName());
//				dataBaseTree.addChild(child, parent);
//			}
//			System.out.println("============");
//		} catch (MalformedURLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (SQLException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
			
	}

}

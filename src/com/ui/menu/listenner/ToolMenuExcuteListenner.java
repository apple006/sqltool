package com.ui.menu.listenner;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

import main.SQLTool;

import com.entity.LoginInfo;
import com.prompt.DBKeyTextPane;
import com.ui.MenuListenner;
import com.ui.extensible.UITabbedPane;
import com.view.sqloperate.Controller;
import com.view.sqloperate.QuerySqlTab;

public class ToolMenuExcuteListenner extends MenuListenner{
	@Override
	public void MouseOnClick(MouseEvent e) {
		System.out.println("÷¥––”Ôæ‰");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Thread th = new Thread(){
			public void run() {
		
			StringBuffer error=new StringBuffer();
			UITabbedPane tabQuerySql = SQLTool.getSQLTool().getToolFrame().getSqlView().getTabQuerySql();
			Component selectedComponent = tabQuerySql.getSelectedComponent();
			int selectedIndex = tabQuerySql.getSelectedIndex();
			QuerySqlTab editSqlTab = (QuerySqlTab)selectedComponent;
			DBKeyTextPane editSqlText = editSqlTab.getEditSqlText();
			int start = editSqlText.getSelectionStart();
			int end = editSqlText.getSelectionEnd();
			editSqlText.setSelectionStart(start);
			editSqlText.setSelectionEnd(end);
			editSqlText.setSelectionColor(editSqlText.getSelectionColor());
			editSqlText.grabFocus();
			LoginInfo info = editSqlTab.getConnectionInfo();
			if("".equals(info.getDataType())){
				JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "«Î—°‘Ò  Connection!");
				return ;
			}
			try {
				if(!editSqlTab.isExct()){
					editSqlTab.clearResult();
					editSqlTab.execting();
					Controller.newController().executQuerySqlTab(info, editSqlTab);
					editSqlText.setSelectText(start, end);
				}
			} catch (Exception e1) {
				try {
					Controller.newController().loginIn(info);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				error.append(new Date().toLocaleString());	
				error.append(" : execute ");
				error.append(" fail :  ");
				error.append(" message : "+e1.getMessage());
				error.append("cause : "+e1.getCause());
				if (e1 instanceof SQLException) {
					error.append("errorCode :"+((SQLException)e1).getErrorCode());
				}
				error.append("loa :"+e1.getLocalizedMessage());
				e1.printStackTrace();
			}
			finally{
				editSqlTab.exected();
			}
			}
		};
		th.start();
	}
}

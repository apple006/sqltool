package com.ui.menu.listenner;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import main.SQLTool;

import com.dao.entity.OracleDBManager;
import com.dao.entity.Table;
import com.dao.imp.Page;
import com.entity.LoginInfo;
import com.excel.ExcelWrite;
import com.prompt.DBKeyTextPane;
import com.ui.MenuListenner;
import com.ui.menu.listenner.dialog.SelectColumnDialog;
import com.view.sqloperate.Controller;
import com.view.sqloperate.QuerySqlTab;
import com.view.sqloperate.ResultView;
import com.view.system.dialog.ExpDialog;

public class OperateResultListener extends MenuListenner {

	private ResultView reusltView = null;
	@Override
	public void actionPerformed(ActionEvent e) {
		Component selectedComponent = SQLTool.getSQLTool().getToolFrame()
				.getSqlView().getTabQuerySql().getSelectedComponent();
		QuerySqlTab tab = (QuerySqlTab) selectedComponent;
		Component component = tab.getResutTab().getSelectedComponent();
		if (component != null) {
			reusltView = (ResultView) component;
			
			LoginInfo info = tab.getConnectionInfo();
			if ("".equals(info.getDataType())) {
				JOptionPane.showMessageDialog(SQLTool.getSQLTool()
						.getToolFrame().getFrame(), "ÇëÑ¡Ôñ  Connection!");
				return;
			}

			
			String name = ((JButton) e.getSource()).getName();
			if ("pagenext".equals(name)) {
				try {
					tab.clearResult();
					Controller.newController().executQuerySqlTab(info, tab);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if ("pageup".equals(name)) {
				
				try {
					tab.clearResult();
					Controller.newController().executQuerySqlTab(info, tab);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if ("add".equals(name)) {
				reusltView.prepAdd();
			}
			if ("edit".equals(name)) {
				reusltView.setStartEdit();
			}
			if ("commit".equals(name)) {
				reusltView.stopCellEditing();
				try {
					DBKeyTextPane editSqlText = tab.getEditSqlText();
					int start = editSqlText.getSelectionStart();
					int end = editSqlText.getSelectionEnd();
					Table updateData;
					updateData = reusltView.getUpdateData();
					if(!updateData.hasPk()){
						SelectColumnDialog dialog = new SelectColumnDialog(SQLTool.getSQLTool().getToolFrame().getFrame(),"ÐéÄâÖ÷¼ü");
						String[] showDialog = dialog.showDialog(updateData.getColumns());
						updateData.setPks(showDialog);
					}
					Controller.newController().executPreparedUpdate(info, tab, updateData);
					tab.clearResult();
					Controller.newController().executQuerySqlTab(info, tab);
					editSqlText.setSelectText(start, end);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if ("delete".equals(name)) {
				reusltView.prepDelete();
				reusltView.repaint();
			}
			if ("imp".equals(name)) {
				String tableName = reusltView.getTableName();

				final ExpDialog exp = new ExpDialog(SQLTool.getSQLTool().getToolFrame().getFrame(),tableName,380,230);
				exp.setLocationRelativeTo(null);
				exp.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(exp.getExpType()==ExpDialog.SQL){
							File file = exp.getFile();
							String endFileName = file.getAbsolutePath();
							if(endFileName.endsWith(".sql")){
								reusltView.stopCellEditing();
								StringBuffer[] insertSql = reusltView.getInsertSql();
								try {
									BufferedWriter wirter = new BufferedWriter( new FileWriter(file));
									for (int i = 0; i < insertSql.length; i++) {
										wirter.write(insertSql[i]+";\n");
									}
									wirter.flush();
									wirter.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							
							exp.setVisible(false);
						
						}
						if(exp.getExpType()==ExpDialog.XLS){
							File file = exp.getFile();

							reusltView.stopCellEditing();
							ArrayList<String[]> excel = reusltView.getExcel();
					
							ExcelWrite write = new ExcelWrite(file);
							try {
								write.writeExcelValue(excel);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							exp.setVisible(false);
						}
						if(exp.getExpType()==ExpDialog.EDIT){
							System.out.println(System.currentTimeMillis());
							reusltView.stopCellEditing();
							long currentTimeMillis = System.currentTimeMillis();

							StringBuffer[] insertSql = reusltView.getInsertSql();
							long currentTimeMillis2 = System.currentTimeMillis();
							OracleDBManager.getOracleDBManager().viewDMLResult(insertSql);
							long currentTimeMillis3 = System.currentTimeMillis();



							exp.setVisible(false);
						}
					}
				});
				exp.show();
			}
		}
	}

}

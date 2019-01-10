package com.view.movedata.execut;

import java.io.File;

import javax.swing.JTable;

import com.entity.LoginInfo;
import com.view.movedata.exp.entity.DataCache;
import com.view.sqloperate.execut.Execute;

public class ImpDataBaseCtr{
		private File file ;
		private Execute query;
		private LoginInfo loginfo;
		private JTable table;
		private DataCache<String[]> cath;
		private String[] colums;
		private String tableName;
		private int row;
		public ImpDataBaseCtr(LoginInfo loginfo,File file,JTable table,int row){
			this.row = row;
			this.table = table;
			this.file = file;
			tableName = file.getName().split("\\.")[0].toUpperCase();
			if(file.getName().endsWith(".sql")){
				query = new ImpDataFromFileImp(loginfo,this);
			}
			if(file.getName().endsWith(".xls")||file.getName().endsWith(".xlsx")){
				query = new ImpDataFromExcelImp(loginfo,this);
			}
			Thread th= new Thread(query);
			th.start();
		}
		public LoginInfo getLoginfo() {
			return loginfo;
		}
		public void setLoginfo(LoginInfo loginfo) {
			this.loginfo = loginfo;
		}
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public File getFile() {
			return file;
		}
		public void setFile(File file) {
			this.file = file;
		}
		public DataCache<String[]> getCath() {
			return cath;
		}
		
		public void setSpeed(){
			ExpMsg expMsg = (ExpMsg)table.getValueAt(row,2);
			expMsg.setValue();
			table.setValueAt(expMsg, row, 2);
		}
		public String[] getColums() {
			// TODO 自动生成的方法存根
			return colums;
		}
		public void setMsg(String message) {
			table.setValueAt(file.getName()+" : "+message,row,1);
		}
//		public void setError() {
//			ExpMsg expMsg = jprogressbar.get(tableName);
//			expMsg.setValue(0);
//			table.setValueAt(expMsg, expMsg.getRow(), expMsg.getCol());
//
//		}
}

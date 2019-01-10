package com.view.movedata.execut;

import java.io.File;
import java.util.Map;

import javax.swing.JTable;

import com.entity.LoginInfo;
import com.view.movedata.ISaveData;
import com.view.movedata.exp.entity.DataCache;

public class ExpDataToFileCtr{
		private String tableName;
		private File file ;
		private ISaveData saveWay;
		private QueryImp exp;
		private LoginInfo loginfo;
		private Thread queryTh;
		private Thread saveTh;
		private JTable table;
		private DataCache<String> cath;
		private int row;
		public ExpDataToFileCtr(LoginInfo loginfo,String tableName,File file, JTable table,int i){
			this.table = table;
			this.row = i;
			this.tableName = tableName;
			this.loginfo = loginfo;
			this.file = file;
			cath = new DataCache<String>();
			saveWay = new ExpDataToFileImp(this);
			exp = new QueryImp(this);
			queryTh = new Thread(exp,tableName);
			queryTh.start();
			saveTh= new Thread(saveWay);
			saveTh.start();
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
		public DataCache<String> getCath() {
			return cath;
		}
		public Thread getQueryTh() {
			return queryTh;
		}
		public void setQueryTh(Thread queryTh) {
			this.queryTh = queryTh;
		}
		public Thread getSaveTh() {
			return saveTh;
		}
		public void setSaveTh(Thread saveTh) {
			this.saveTh = saveTh;
		}
		
		public void setSpeed(){
			Object valueAt = table.getValueAt(row, 2);
			if(valueAt instanceof ExpMsg){
				ExpMsg	expMsg = (ExpMsg) valueAt;
				expMsg.setValue();
				table.setValueAt(expMsg,row, 2);
			}
		}
}

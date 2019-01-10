package com.view.movedata.execut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;

import com.entity.LoginInfo;
import com.view.movedata.IQuery;
import com.view.movedata.ISaveData;
import com.view.movedata.exp.AsynchronousBathLoadDataBase;
import com.view.movedata.exp.entity.DataCache;
import com.view.movedata.exp.entity.MoveDataMsg;
import com.view.movedata.exp.entity.SubmitDataGroup;
import com.view.tool.HelpProperties;

public class MoveDataCtr{
		private ISaveData save;
		private IQuery exp;
		private DataCache<SubmitDataGroup> cath ; 
		private LoginInfo formLoginfo;
		private Thread queryTh;
		private Thread saveTh;
		private JTable table;
		private MoveDataMsg moveMsg;
		private LoginInfo toLoginIfo;
		private int row;
		public MoveDataCtr(LoginInfo formLoginfo,LoginInfo toLoginIfo,MoveDataMsg moveMsg , JTable table,int row){
			this.table = table;
			this.row = row;
			this.moveMsg = moveMsg;
			this.formLoginfo = formLoginfo;
			this.toLoginIfo = toLoginIfo;
			cath = new DataCache<SubmitDataGroup>();	
			save = new MoveSaveDataImp(this);
			exp = new MoveQueryDataImp(this);
			queryTh = new Thread(exp,moveMsg.getSelectTableName().toString());
			queryTh.start();
			Thread thread2 = new Thread(save,moveMsg.getSelectTableName().toString());
			thread2.start();
//			for (int i = 0; i < thread; i++) {
//				AsynchronousBathLoadDataBase.getAsynchronousBathLoadDataBase().submit(new Thread(save));
//			}
		}
		public MoveDataCtr(LoginInfo toLoginIfo,MoveDataMsg moveMsg , JTable table,int row){
			this.table = table;
			this.row= row;
			this.moveMsg = moveMsg;
			this.toLoginIfo = toLoginIfo;
			cath = new DataCache<SubmitDataGroup>();	
			save = new MoveSaveDataImp(this);
			exp = new MoveRandomDataImp(this);
			queryTh = new Thread(exp,moveMsg.getSelectTableName().toString());
			queryTh.start();
			Thread thread2 = new Thread(save,moveMsg.getSelectTableName().toString());
			thread2.start();
			//			for (int i = 0; i < thread; i++) {
//				AsynchronousBathLoadDataBase.getAsynchronousBathLoadDataBase().submit(new Thread(save));
//			}
		}
		
		public MoveDataCtr(LoginInfo toLoginIfo,MoveDataMsg moveMsg , JTable table,int row,String script){
			this.table = table;
			this.row= row;
			this.moveMsg = moveMsg;
			this.toLoginIfo = toLoginIfo;
			save = new MoveScript(this);
			Thread	queryTh = new Thread(save,moveMsg.getInsertTableName().toString());
			queryTh.start();
			//			for (int i = 0; i < thread; i++) {
//				AsynchronousBathLoadDataBase.getAsynchronousBathLoadDataBase().submit(new Thread(save));
//			}
		}
		public boolean isForceWhere(){
			return (boolean) this.table.getValueAt(row, 6);
		}
		public boolean isChangeWhere(){
    		Boolean isChangeWhere = (Boolean) table.getValueAt(row, 5);
    		if(isChangeWhere==null){
    			return false;
    		}
			return (boolean) this.table.getValueAt(row, 6);
		}
		public LoginInfo getFormLoginfo() {
			return formLoginfo;
		}
		public LoginInfo getToLoginfo() {
			return toLoginIfo;
		}
		public void setLoginfo(LoginInfo loginfo) {
			this.formLoginfo = loginfo;
		}
		public  DataCache<SubmitDataGroup> getCath() {
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
		public MoveDataMsg getMoveDataMsg(){
			return moveMsg;
		}
		
		public  void  setSpeed(int i){
			ExpMsg expMsg = (ExpMsg) table.getValueAt(row, 3);
//			System.out.println(i+"=========================");
			expMsg.setValue(i);
			table.setValueAt(expMsg, this.row, 3);
		}
		public String getTableName() {
			return moveMsg.getSelectTableName().toString();
		}
		
		public synchronized void setStartDate(){
			ExpMsg expMsg = (ExpMsg) table.getValueAt(row, 3);
			expMsg.setValue(0L);
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			expMsg.setStartDate(df.format(new Date()));
			table.setValueAt(expMsg, this.row, 3);
		}
		
		public synchronized void setEndDate(){
			System.out.println("½áÊø");
			ExpMsg expMsg = (ExpMsg) table.getValueAt(row, 3);
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			expMsg.setEndDate(df.format(new Date()));
			table.setValueAt(expMsg, this.row, 3);
		}
}

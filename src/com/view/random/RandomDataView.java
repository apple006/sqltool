package com.view.random;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.entity.LoginInfo;
import com.view.sqloperate.execut.Execute;

public class RandomDataView extends Execute{

		private String nameName;
		private String script;
		public RandomDataView(LoginInfo loginfo,String nameName){
			super(loginfo);
			this.nameName = nameName;
		}
		
		@Override
		public void run() {
			Connection connection= null;
			try {
				connection = getConnection();
				int ii = 0;
				Statement createStatement = connection.createStatement();
				String sql;
				long currentTimeMillis = System.currentTimeMillis();
				for (int i = 437376; i < 900000; i++) {
					
					 sql = "INSERT INTO TEXT(EMPNO,ENAME,JOB,MGR,SAL,COMM,A,B,C,D,E) VALUES ("+i+",'aaaaa','bbbb',22,11,null,'ggggg','fffff','eeeee','ddddd','cccc') ";
					createStatement.addBatch(sql);
					if(ii++==1000){
						long currentTimeMillis1 = System.currentTimeMillis()-currentTimeMillis;
						currentTimeMillis = System.currentTimeMillis();
						System.out.println("sql拼接时间 ："+currentTimeMillis1);
						ii = 0;
						createStatement.executeBatch();
						long currentTimeMillis2 = System.currentTimeMillis()-currentTimeMillis;
						System.out.println("提交时间："+currentTimeMillis2);
						currentTimeMillis = System.currentTimeMillis();
					}
				}
				createStatement.executeBatch();
				System.out.println("结束");
				createStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally{
				synchronized (this) {
					this.notify();
					releasConnection(connection);
				}
			}
		}
		
		public String  getTableDDL(){
			return script;
		}

}

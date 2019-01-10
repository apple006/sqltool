package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.entity.LoginInfo;

public class DropDML extends Execute implements DML{

	private String[] names;
	private String type;
	/**
	 * 
	 * @param loginfo
	 * @param names
	 * @param Type 参照接口DML中类型
	 */
	public DropDML(LoginInfo loginfo,String[] names,String type){
		super(loginfo);
		this.names = names;
		this.type = type;
	}
	
	@Override
	public void run() {
		Connection connection= null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			String script[] = new String[names.length];
			for (int i = 0; i < names.length; i++) {
				script[i] = "DROP"+ type+names[i].toUpperCase();
			}
			dao.executeBatchUpdata(connection, script);
		} catch (SQLException e) {
			SQLException nextException = e.getNextException();
			isSuccess = false;
			error.append(new Date().toLocaleString());	
			error.append(":     MESSAGE :"+e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		finally{
			synchronized (this) {
				try {
					this.notify();
					releasConnection(connection);
					connection.setAutoCommit(true);
					connection.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

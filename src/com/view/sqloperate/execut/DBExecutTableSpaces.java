package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.dao.entity.Table;
import com.entity.LoginInfo;

public class DBExecutTableSpaces extends Execute{

	private ArrayList<String> tableSpace;
	public DBExecutTableSpaces(LoginInfo loginfo){
		super(loginfo);
	
	}
	
	@Override
	public void run() {
		Connection connection = null;
		try {
			 connection = getConnection();
			 tableSpace = dao.getTableSpace(loginInfo.getUserName(),connection);
		} catch (SQLException e) {
			isSuccess = false;
			error.append(new Date().toLocaleString());	
			error.append(":     MESSAGE :"+e.getMessage());
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
				this.notify();
				releasConnection(connection);
			}
		}
	}
	
	public ArrayList<String> getTableSpaces() {
		return tableSpace;
	}
}

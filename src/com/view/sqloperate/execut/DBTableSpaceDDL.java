package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.dao.entity.Table;
import com.entity.LoginInfo;

public class DBTableSpaceDDL extends Execute{

	private String script;
	private String tableSpaceName;
	public DBTableSpaceDDL(String name,LoginInfo loginfo){
		super(loginfo);
		this.tableSpaceName =name;
	}
	
	@Override
	public void run() {
		Connection connection = null;
		try {
			 connection = getConnection();
			 script = dao.getTableSpaceDDL(tableSpaceName, connection);
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
	
	public String getTableSpaceDDL() {
		return script;
	}
}

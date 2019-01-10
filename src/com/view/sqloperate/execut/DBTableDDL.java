package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.dao.entity.Table;
import com.entity.LoginInfo;

public class DBTableDDL extends Execute{

	private String nameName;
	private String script;
	public DBTableDDL(LoginInfo loginfo,String nameName){
		super(loginfo);
		this.nameName = nameName;
	}
	
	@Override
	public void run() {
		Connection connection= null;
		try {
			connection = getConnection();
			script = dao.getTableDDL(nameName,connection);
			
		} catch (SQLException e) {
			isSuccess = false;
			error.append(new Date().toLocaleString());	
			error.append(":     MESSAGE :"+e.getMessage());
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

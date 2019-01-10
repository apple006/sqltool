package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.dao.entity.Table;
import com.entity.LoginInfo;

public class DBViewDDL extends Execute{

	private String viewName;
	private String script;
	public DBViewDDL(LoginInfo loginfo,String viewName){
		super(loginfo);
		this.viewName = viewName;
	}
	
	@Override
	public void run() {
		Connection connection=null;
		try {
			connection = getConnection();
			script = dao.getViewDDL(viewName,connection);
		
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
	
	public String  getViewDDL(){
		return script;
	}
}

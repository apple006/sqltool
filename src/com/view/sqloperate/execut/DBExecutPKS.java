package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.dao.entity.Column;
import com.entity.LoginInfo;

public class DBExecutPKS extends Execute {

	private HashMap<String, ArrayList<Column>> pks;
	public DBExecutPKS(LoginInfo loginInfo) {
		super(loginInfo);
	}

	@Override
	public void run() {
		Connection connection = null;
		try {
			error =new  StringBuffer();
			connection = getConnection();
			 pks = dao.getPk(connection);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			isSuccess = false;
//			error.append(new Date().toLocaleString());	
			error.append(":     MESSAGE :"+e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		finally{
			synchronized (this) {
				this.notify();
				releasConnection(connection);
			}
		}
	}
	
	public HashMap<String, ArrayList<Column>> getPks(){
		return pks;
	}

}

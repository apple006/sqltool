package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dao.entity.Column;
import com.entity.LoginInfo;


/**
 * в╟йн Dao
 * @author Administrator
 *
 */
public class DBExecutColumnsFromSql extends Execute{
	
	
	private ArrayList<Column> column;
	private String tableName;
	public DBExecutColumnsFromSql(LoginInfo loginfo,String tableName){
		super(loginfo);
		this.tableName =tableName;
	
	}
	
	@Override
	public void run() {
		Connection connection = null;
		try {
			connection = getConnection();
			column = dao.getColumnsFromSql(loginInfo.getUserName(), connection, tableName);
		} catch (SQLException e) {
			isSuccess = false;
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
	
	public ArrayList<Column>  getColumns(){
		return column;
	}
}

package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import com.dao.imp.Page;
import com.entity.LoginInfo;

/**
 * 执行增删改查DDL\DML语句
 * @author Administrator
 *
 */
public class ExecutSql extends Execute {

	private String sql;
	public ExecutSql(LoginInfo loginInfo, String sql) {
		super(loginInfo);
		this.sql = sql;
	}

	@Override
	public void run() {
		Connection connection = null;
		try {
			connection = getConnection();
			String userName = loginInfo.getUserName();
			String[] split = userName.split("#");
			if(split.length>1) {
				connection.getCatalog();
//				connection.setCatalog(split[1]);
//				connection.setClientInfo("user", split[1]);
			}
			table = dao.executeSql(connection,sql);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			isSuccess = false;
//			error.append(new Date().toLocaleString());	
			error.append(e.getMessage()+"");
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

	
}

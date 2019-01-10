package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.dao.imp.Page;
import com.entity.LoginInfo;

public class ExecuteUpdata extends Execute {

	private String []sqls;
	public ExecuteUpdata(LoginInfo loginInfo, String[] sqls) {
		super(loginInfo);
		this.sqls = sqls;
	}

	@Override
	public void run() {
		Connection connection=null;
		try {
			connection = getConnection();
			rows = dao.executeBatchUpdata(connection, sqls);
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			isSuccess = false;
//			error.append(new Date().toLocaleString());	
			error.append(":     MESSAGE :"+e.getMessage());
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		finally{
			synchronized (this) {
				releasConnection(connection);
				this.notify();
			}
		}
		
	}

}

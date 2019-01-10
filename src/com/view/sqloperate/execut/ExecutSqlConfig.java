package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.dao.imp.Page;
import com.entity.LoginInfo;
import com.view.system.perferenc.xml.XMLDDLConfig;

public class ExecutSqlConfig extends Execute {

	protected XMLDDLConfig xmlddlConfig = XMLDDLConfig.getXMLDDLConfig();
	private String name;
	public ExecutSqlConfig(LoginInfo loginInfo,String name) {
		super(loginInfo);
		this.name = name;
	}

	@Override
	public void run() {
		Connection connection = null;
		try {
			
			connection = getConnection();
			String sql = xmlddlConfig.getScript(dao.getDBName(),name).toUpperCase();

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
			error.append(":     MESSAGE :");
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

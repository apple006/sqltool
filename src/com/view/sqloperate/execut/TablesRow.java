package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dao.Dao;
import com.dao.entity.Table;
import com.entity.LoginInfo;
import com.listener.ExcuteListener;
import com.view.tool.HelpProperties;

public class TablesRow extends Execute implements DML{

	private Integer rowCount;
	private String tableName;
	
	/**
	 * 
	 * @param loginfo
	 * @param names
	 * @param Type 参照接口DML中类型
	 */
	public TablesRow(LoginInfo loginfo,String tableName){
		super(loginfo);
		this.tableName = tableName;
	}
	
	@Override
	public void run() {
		Connection connection= null;
		try {
			connection = getConnection();
			setRowCount(dao.getTablesRow(connection,tableName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			synchronized (this) {
				this.notify();
				releasConnection(connection);
			}
		}
//		ExecutorService threads  = Executors.newFixedThreadPool(new Integer(HelpProperties.GetValueByKey("keyvalue.properties", "countThread")));
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
}

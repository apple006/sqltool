package com.view.movedata.execut;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.entity.LoginInfo;
import com.excel.CheckException;
import com.excel.ExcelEntity;
import com.excel.ExcelReader;
import com.excel.ImpDefaultExcelCheckAndPack;
import com.view.sqloperate.execut.Execute;
import com.view.tool.HelpProperties;
import com.view.tool.HelpRslutToSql;

public class QuerySqlFileImp extends Execute{
	public QuerySqlFileImp(LoginInfo loginfo) {
		super(loginfo);
	}
	/**
	 * 块缓存数据大小
	 */
	private int cathSize = 2000;
	private ImpDataBaseCtr ctr;
	public QuerySqlFileImp(LoginInfo loginfo,ImpDataBaseCtr ctr){
		this(loginfo);
		this.ctr = ctr;
	}
	@Override
	public void run() {
		Connection connection = null;
		Statement createStatement = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			FileReader fileInputStream = new FileReader(new File(ctr.getFile().getPath()));
			BufferedReader reader = new BufferedReader(fileInputStream);
			 createStatement = connection.createStatement();
			int count =  new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "count")); 

			int rowCount = 0;
			String sql = null;
			while(true){
				sql = reader.readLine();
				if(sql==null){
					break;
				}
				createStatement.addBatch(sql);
				if(count==rowCount++){
					createStatement.executeBatch();
				}
			}	
			connection.commit();
			ctr.setMsg("导入成功");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			ctr.setMsg(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally{	
			releasConnection(connection);
			closeAll(createStatement);
		}
		
	}
	
}

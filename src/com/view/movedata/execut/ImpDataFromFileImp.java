package com.view.movedata.execut;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.entity.LoginInfo;
import com.view.sqloperate.execut.Execute;
import com.view.tool.HelpProperties;

public class ImpDataFromFileImp extends Execute{
	public ImpDataFromFileImp(LoginInfo loginfo) {
		super(loginfo);
	}
	/**
	 * 块缓存数据大小
	 */
	private int cathSize = 2000;
	private ImpDataBaseCtr ctr;
	public ImpDataFromFileImp(LoginInfo loginfo,ImpDataBaseCtr ctr){
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

package com.view.movedata.execut;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.entity.LoginInfo;
import com.excel.CheckException;
import com.excel.ExcelEntity;
import com.excel.ExcelReader;
import com.excel.ImpDefaultExcelCheckAndPack;
import com.view.sqloperate.execut.Execute;
import com.view.tool.HelpRslutToSql;

public class QueryExcelImp extends Execute{
	public QueryExcelImp(LoginInfo loginfo) {
		super(loginfo);
	}
	/**
	 * 块缓存数据大小
	 */
	private int cathSize = 2000;
	private ImpDataBaseCtr ctr;
	public QueryExcelImp(LoginInfo loginfo,ImpDataBaseCtr ctr){
		this(loginfo);
		this.ctr = ctr;
	}
	@Override
	public void run() {
		ExcelReader<String[]> exacelReader = null;
		Connection connection = null;
		PreparedStatement  prepareStatement= null;
		try {
			exacelReader = new ExcelReader<String[]>(ctr.getFile().getPath());
			exacelReader.setCheckAndPackModel(new ImpDefaultExcelCheckAndPack(exacelReader));
			ExcelEntity<String[]> readExcelContent = exacelReader.readExcelContent();
			List<String[]> list = readExcelContent.getList();
			
			
			String[] readExcelTitle = exacelReader.getReadExcelTitle();
			connection = getConnection();
			connection.setAutoCommit(false);
			prepareStatement = connection.prepareStatement(HelpRslutToSql.getInsertPrePareSql(ctr.getTableName(),readExcelTitle));
			int size = list.size();
			int countRow=0;
			
				
			for (int i = 0; i < size; i++) {
				ctr.setSpeed();
				countRow++;
				
				for (int j = 1; j <=readExcelTitle.length; j++) {
					prepareStatement.setString(j,list.get(i)[j-1]);
				}
				prepareStatement.addBatch();
				prepareStatement.clearParameters();
				if(countRow>cathSize){
					countRow = 0;
					prepareStatement.executeBatch();
					prepareStatement.clearBatch();
					connection.commit();
				}
			}
			prepareStatement.executeBatch();
			prepareStatement.clearBatch();
			connection.commit();
			ctr.setMsg("导入成功");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
//			ctr.setError();
			ctr.setMsg(e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		finally{
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

			releasConnection(connection);
			closeAll(prepareStatement);
		}
		
	}
	
}

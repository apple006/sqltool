package com.dao.imp;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.dao.entity.Column;
import com.dao.entity.Table;
import com.view.system.ToolLogger;
import com.view.tool.HelpProperties;

public class MySqlTemplate extends AbstractDao{

	
	public String getDBName() {
		return MYSQL;
	}
	
	@Override
	public String page(String sql) {
		if(sql.toUpperCase().indexOf("LIMIT")!=-1){
			return sql;
		}
		return super.page(sql);
	}
	
	public String getTableDDL(String tableName, Connection conn){
		String viewDdl = xmlddlConfig.getScript(getDBName(),"TABLE").replace("TABLE_NAME", tableName);
		Statement createStatement = null;
		ResultSet executeQuery= null;
		String ddl ="";
		try {
			createStatement = conn.createStatement();
//			createStatement.execute("set names  'utf8'");
			executeQuery = createStatement.executeQuery(viewDdl);
			while(executeQuery.next()){
				ddl = executeQuery.getString(2);
			}
		} catch (SQLException e) {
			ToolLogger.error(e.getMessage());

			e.printStackTrace();
		}
		finally{
			closeAll(executeQuery,createStatement);
		}
		return ddl;
	}
	
	
	public String getViewDDL(String viewName, Connection conn) {
		String viewDdl = xmlddlConfig.getScript(getDBName(),"VIEW").replace("VIEW_NAME", viewName);
		Statement createStatement = null;
		ResultSet executeQuery= null;
		String ddl = "";
		try {
			createStatement = conn.createStatement();
			executeQuery = createStatement.executeQuery(viewDdl);
			while(executeQuery.next()){
				ddl = executeQuery.getString(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			closeAll(executeQuery,createStatement);
		}
		return ddl;
	}
	
	@Override
	public String getTableName(String tableName) {
		// TODO Auto-generated method stub
		return tableName;
	}
	@Override
	public void tryQuery(Connection conn) {
		Statement createStatement = null;
		ResultSet executeQuery =null;
		try{
			createStatement = conn.createStatement();
			
			executeQuery = createStatement.executeQuery("select 1  ");
			executeQuery.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			closeAll(executeQuery,createStatement);
		}
	}
	
	@Override
	public String getROWID() {
		return "UUID";
	}
	@Override
	public int getFetchSize() {
		return Integer.MIN_VALUE;
	}

	@Override
	public ArrayList<Table> getSequence(String userName, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


}

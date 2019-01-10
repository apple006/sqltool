package com.dao.imp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import com.dao.entity.Table;
import com.ui.extensible.Cell;
import com.view.tool.HelpProperties;


public class OracleTemplate extends AbstractDao{

	
	public String getDBName() {
		return ORCL;
	}
	@Override
	public String getROWID() {
		return "ROWID";
	}
	

	@Override
	public int getFetchSize() {
		Integer size = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "fetchSize"));
		return size;
	}
	public ArrayList<Table> getTables(String userName,Connection conn)
			throws SQLException {
		if(userName.indexOf("#")==-1){
			return super.getTables(userName, conn);
		}
		String[] split = userName.split("#");
		String tablesSql = xmlddlConfig.getScript(getDBName(),"TABLES");
		ArrayList<Table>  tables = new ArrayList<Table>();
		Statement createStatement = null;
		ResultSet executeQuery =null;
		try{
			createStatement = conn.createStatement();
			
			executeQuery = createStatement.executeQuery(tablesSql+ " WHERE TABLE_TYPE='TABLE'"+(split.length==1?"":" AND OWNER='"+split[1]+"'"));
			String tableName = "";
			String ownerName = "";
			Table table ;
			while(executeQuery.next()){
				table = new Table();
				tableName = executeQuery.getString("TABLE_NAME");
				ownerName = executeQuery.getString("OWNER");
				table.setTableCode(split.length==1?tableName:split[1]+"."+tableName);
				tables.add(table);
			}
		}
		finally{
			closeAll(executeQuery,createStatement);
		}
		return tables;
	}
	
	
	public ArrayList<Table> getSequence(String userName,Connection conn)
			throws SQLException {
		
		if(userName.indexOf("#")==-1){
			return super.getTables(userName, conn);
		}
		String[] split = userName.split("#");
		String tablesSql = xmlddlConfig.getScript(getDBName(),"SEQUENCE");
		ArrayList<Table>  tables = new ArrayList<Table>();
		Statement createStatement = null;
		ResultSet executeQuery =null;
		try{
			createStatement = conn.createStatement();
			executeQuery = createStatement.executeQuery(tablesSql+ " WHERE TABLE_TYPE='TABLE'"+(split.length==1?"":" AND OWNER='"+split[1]+"'"));
			String tableName = "";
			String ownerName = "";
			Table table ;
			while(executeQuery.next()){
				table = new Table();
				tableName = executeQuery.getString(1);
				table.setTableCode(split.length==1?tableName:split[1]+"."+tableName);
				tables.add(table);
			}
		}
		finally{
			closeAll(executeQuery,createStatement);
		}
		return tables;
	}
	@Override
	public void tryQuery(Connection conn) throws SQLException {
		Statement createStatement = null;
		ResultSet executeQuery =null;
		try{
			createStatement = conn.createStatement();
			
			executeQuery = createStatement.executeQuery("select 1 from dual");
			String tableName = "";
			String ownerName = "";
			Table table ;
			executeQuery.next();
		} 
		finally{
			closeAll(executeQuery,createStatement);
		}
	}
}

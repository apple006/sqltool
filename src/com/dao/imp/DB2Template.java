package com.dao.imp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.dao.entity.Table;
import com.view.tool.HelpProperties;


public class DB2Template extends AbstractDao{

	public String getDBName() {
		return DB2;
	}
	@Override
	public String getROWID() {
		return "ROWID";
	}
	@Override
	public void tryQuery(Connection conn) {
		Statement createStatement = null;
		ResultSet executeQuery =null;
		try{
			createStatement = conn.createStatement();
			executeQuery = createStatement.executeQuery("select 1 from sysibm.sysdummy1 ");
			executeQuery.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			closeAll(executeQuery,createStatement);
		}
	}
	
	@Override
	public int getFetchSize() {
		Integer size = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "fetchSize"));
		return size;
	}
	@Override
	public ArrayList<Table> getSequence(String userName, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}

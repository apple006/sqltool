package com.dao;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dao.entity.Column;
import com.dao.entity.Table;
import com.dao.imp.Page;

@SuppressWarnings("unused")
public interface Dao extends ITemplate{
	
	
	/**
	 * 查询数据库内所有表信息
	 * @return
	 * @throws SQLException 
	 */
	public ArrayList<Table> getTables(String userName,Connection conn) throws SQLException;
	public ArrayList<Table> getSequence(String userName,Connection conn) throws SQLException;

	/**
	 * 查询数据库某个表列信息
	 * @return
	 * @throws SQLException 
	 */
	public ArrayList<Column> getColumns(String userName,Connection conn,String tableName) throws SQLException;
	public ArrayList<Column> getColumnsFromSql(String userName,Connection conn,String tableName) throws SQLException;

	/**
	 * 一般sql执行方法
	 * @param conn 
	 * @param sql
	 * @param page 
	 * @return
	 * @throws SQLException
	 */
	Table executeSql(Connection conn, String sql)  throws SQLException ;
	/**
	 * update语句处理
	 * @param conn 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	int[] executeBatchUpdata(Connection conn, String[] sql) throws SQLException;
//	/**
//	 * insert批处理语句处理
//	 * @param conn 
//	 * @param sql
//	 * @return
//	 * @throws SQLException
//	 */
//	int[] executeBatchInsert(Connection conn, String[] sql) throws SQLException;
	public int[] executePreparedUpdata(Connection connection, String[] sql, Table updateData ) throws SQLException;
			
	HashMap<String, ArrayList<Column>> getPk(Connection conn) throws SQLException;
	public ArrayList<Table> getViews(String userName,Connection conn)
			throws SQLException ;

	
	/**
	 * 查询创建view的DDL语句
	 * @param viewName
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public String getViewDDL(String viewName, Connection connection) throws SQLException;

	
	public String getTableDDL(String tableName, Connection connection);

	public ArrayList<String> getTableSpace(String userName, Connection connection) throws SQLException;
	String getTableSpaceDDL(String tableSpaceName, Connection conn)
			throws SQLException;
	public int getTablesRow(Connection connection,String tableName) throws SQLException;
	public int getTablesRowFromSql(Connection connection,String tableName) throws SQLException;

	Table getViewSessions(String viewName, Connection conn) throws SQLException;
	/**
	 * 区分不同数据库表名大小写问题
	 * @return
	 */
	String getTableName(String tableName);
	/**
	 * 自动默认加分页处理
	 * @param sql
	 * @return
	 */
	String page(String sql);
	
	/**
	 * 根据不同数据库实现rowid字段
	 * @return
	 */
	String getROWID();
	/**
	 * 数据量大时不一次性加载数据
	 * @param connection
	 * @param bigSqlTable
	 * @return
	 */
	public Statement getLazyStatement(Connection connection) throws SQLException;
	
	public int getFetchSize() ;
	String getSelectSql(String userName, Connection connection, String tableName) throws SQLException;
	public void tryQuery(Connection conn) throws SQLException;
}
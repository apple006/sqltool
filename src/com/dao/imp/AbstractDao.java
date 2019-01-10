package com.dao.imp;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.dao.Dao;
import com.dao.entity.Column;
import com.dao.entity.Table;
import com.sql.ISQLMachine;
import com.sql.OracleSQLMachinFacto;
import com.ui.extensible.Cell;
import com.ui.extensible.UITable;
import com.util.UtilDate;
import com.util.UtilString;
import com.view.system.perferenc.xml.XMLDDLConfig;
import com.view.tool.HelpProperties;

/**
 * 实现不同数据库公共方法 query、insert、update等
 * 
 * @author Administrator
 * 
 */
public abstract class AbstractDao implements Dao {

	private ISQLMachine ma;
	Logger logger = Logger.getLogger(getClass());
	protected XMLDDLConfig xmlddlConfig = XMLDDLConfig.getXMLDDLConfig();
	public AbstractDao(){
		ma = new OracleSQLMachinFacto();
	}
	@Override
	public int[] executeBatchUpdata(Connection conn, String[] sql) throws SQLException {
		int[] executeBatch = new int[]{};
		Statement createStatement = null;
		try{
			 createStatement = conn.createStatement();
			for (int i = 0; i < sql.length; i++) {
				createStatement.addBatch(sql[i]);
			}
			executeBatch = createStatement.executeBatch();
		}
		finally{
			close(createStatement);
		}
		return executeBatch;
	}

	@Override
	public Table executeSql(Connection conn, String sql) throws SQLException  {
		Statement createStatement = null;
		ResultSet resultSet = null;
		String page = page(sql);
		long b = System.currentTimeMillis();
		
		try {
			Integer timeOut = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "query_time_out"));
			createStatement = getLazyStatement(conn);
			createStatement.setQueryTimeout(timeOut);
			boolean execute = createStatement.execute(page);

			resultSet = createStatement.getResultSet();
			if(execute){
				Table packResultSet = this.packResultSet(resultSet,true);
				packResultSet.setStartTime(b);
				String lowerCase = sql.trim().toLowerCase();
				boolean startsWith = lowerCase.startsWith("select",lowerCase.indexOf("s") );
				closeAll(resultSet);
				if(!startsWith){
					return packResultSet;
				}
				DatabaseMetaData metaData = conn.getMetaData();
				String tableName = ma.getTableName(sql);
				setPrimaryKey(metaData, packResultSet, tableName);
				return packResultSet;
			}else{
			 	Table table = new Table();
				table.setResult_type(Table.RESULT_UPDATA);
				table.setUpdataCount(createStatement.getUpdateCount());
				table.setStartTime(b);
				return table; 
			}
		
		} 
		finally{
			closeAll(createStatement);
		}
		

	}
	private void setPrimaryKey(DatabaseMetaData metaData, Table packResultSet,
			String tableName) throws SQLException {
		ResultSet primaryKeys;
		if(!"".equals(tableName)){
			primaryKeys = metaData.getPrimaryKeys(null, null, getTableName(tableName));
			while(primaryKeys.next()){
				Column column = packResultSet.getColumn(primaryKeys.getString("COLUMN_NAME"));
				if(column!=null){
					column.setPrimaryKey(true);
					String pk_name = primaryKeys.getString("PK_NAME"); 
					column.setPk_name(pk_name);
				}
			}
		}
		Column rowid = packResultSet.getColumn("ROWID");
		if(rowid!=null){
			rowid.setPrimaryKey(true);
		}
	}

	/**
	 * 得到数据库中Table的数据
	 */
	public ArrayList<Table> getTables(String userName,Connection conn)
			throws SQLException {
		DatabaseMetaData metaData = null;
		ResultSet resultSet = null;
		ArrayList<Table>  tables = new ArrayList<Table>();
		try{
			 metaData = conn.getMetaData();
			 resultSet = metaData.getTables(null,userName, null,
					new String[] { "TABLE" });
			String tableName = "";
			Table table ;
			while(resultSet.next()){
				table = new Table();
				tableName = resultSet.getString("TABLE_NAME");
//			String tableName1 = resultSet.getString("TYPE_SCHEM");
//			if(){
//				
//			}
				table.setTableCode(tableName);
				tables.add(table);
			}
		}
		finally{
			close(resultSet);
		}
		return tables;
	}
	
	
	/**
	 * 得到数据库中Table的数据
	 */
	public ArrayList<Column> getColumns(String userName,Connection conn,String tableName)
			throws SQLException {
		String[] split = userName.split("#");
		Statement createStatement  =null;
		ResultSet executeQuery=null;
		ResultSetMetaData metaData  =null;
		ArrayList<Column> columns = new ArrayList<Column>();
		try{
			 createStatement = conn.createStatement();
			 executeQuery = createStatement.executeQuery("SELECT * FROM "+(split.length==2?split[1]+".":"")+tableName+" WHERE 1=2");
			 metaData = executeQuery.getMetaData();
			Column column ;
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				column = new Column();
				column.setColumnCode(metaData.getColumnName(i));
				column.setDataType(metaData.getColumnType(i));
				column.setColumnTypeName(metaData.getColumnTypeName(i));
				column.setNullable(metaData.isNullable(i));
				column.setColumnSize(metaData.getPrecision(i));
				column.setColumnScaleSize(metaData.getScale(i));
//				column.setPk_name(metaData.getScale(i));
				columns.add(column);
			}
		}finally{
			closeAll(metaData,executeQuery,executeQuery);
		}
		
		return columns;
	}
	
	@Override
	public String getSelectSql(String userName, Connection connection,String tableName) throws SQLException {
		ArrayList<Column> columns = this.getColumns(userName,connection,tableName);
		StringBuffer bufSql = new StringBuffer("SELECT ");
		
		int size = columns.size();
		for (int i = 0; i < size; i++) {
			bufSql.append(columns.get(i).getColumnCode().toUpperCase());
			if(i+1!=size){
				bufSql.append(",");
			}
		}
		bufSql.append(" FROM "+tableName.toUpperCase());

		return bufSql.toString();
	}
	
	/**
	 * 得到数据库中Table的数据
	 */
	public ArrayList<Column> getColumnsFromSql(String userName,Connection conn,String sql)
			throws SQLException {
		ResultSet executeQuery = null;
		ResultSetMetaData metaData =null; 
		Statement createStatement = null;
		ArrayList<Column> columns = new ArrayList<Column>();
		try{
			 createStatement = conn.createStatement();
			int indexOf = sql.toUpperCase().indexOf("WHERE");
			if(indexOf!=-1){
				sql += " AND 1=2";
			}else{
				sql+=" WHERE 1=2 ";
			}
			
			executeQuery = createStatement.executeQuery(sql);
			metaData = executeQuery.getMetaData();
			Column column ;
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				column = new Column();
				column.setColumnCode(metaData.getColumnName(i));
				column.setDataType(metaData.getColumnType(i));
				column.setColumnTypeName(metaData.getColumnTypeName(i));
				column.setNullable(metaData.isNullable(i));
				column.setColumnSize(metaData.getPrecision(i));
				column.setColumnScaleSize(metaData.getScale(i));
//				column.setPk_name(metaData.getScale(i));
				columns.add(column);
			}
		}
		finally{
			closeAll(metaData,executeQuery,executeQuery);
		}
		return columns;
	}
	
	/**
	 * 得到数据库中Table的数据
	 */
	public ArrayList<Table> getViews(String userName,Connection conn)
			throws SQLException {
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet resultSet = metaData.getTables(null, userName, "%",
				new String[] { "VIEW" });
		ArrayList<Table>  tables = new ArrayList<Table>();
		String tableName = "";
		Table table ;
		while(resultSet.next()){
			table = new Table();
			tableName = resultSet.getString("TABLE_NAME");
			table.setTableCode(tableName);
			tables.add(table);
		}
		
		close(resultSet);
		return tables;
	}

	@Override
	public HashMap<String,ArrayList<Column>> getPk(Connection conn) throws SQLException{
		HashMap<String ,ArrayList<Column>> pks = new HashMap<String, ArrayList<Column>>();
		ResultSet resultSet = null;
		try{
			DatabaseMetaData metaData = conn.getMetaData();
			 resultSet = metaData.getTables(null,metaData.getUserName(), "%",new String[] { "TABLE" });
			String tableName = "";
			ArrayList<Column>  colums ;
			Column column;
			while(resultSet.next()){
				colums = new ArrayList<Column>();
				tableName = resultSet.getString("TABLE_NAME");
				ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
				while(primaryKeys.next()){
					column = new Column();
					String column_name = primaryKeys.getString("COLUMN_NAME");
					String pk_name = primaryKeys.getString("PK_NAME"); 
					column.setColumnName(column_name);
					column.setPk_name(pk_name);
					colums.add(column);
				}
				pks.put(tableName, colums);
			}
		}finally{
			closeAll(resultSet);
		}
		
		return pks;
	}
	/**
	 * 
	 * @param resultSet
	 *            返回Map结果集Map<字段，字段下列的数据>
	 * @return
	 * @throws SQLException
	 */
	public Table packResultSet(ResultSet resultSet,boolean ispage) throws SQLException {
		Table table = new Table();
		if(resultSet==null){
			return table;
		}
		try{
			
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			Column column;
			for(int i=1;i<=columnCount;i++){
				
				column = new Column();
				String columnCode = metaData.getColumnName(i);
				int t = metaData.getColumnType(i);
				int nullable = metaData.isNullable(i);
				column.setColumnCode(columnCode);
				column.setDataType(t);
				column.setNullable(nullable);
				column.setNumber(i);
				table.addColumn(column);
			}
			int row = 0;
			Integer size = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "page_size"));
			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					Cell o = getResult(resultSet,table.getColumn(i).getDataType(),i);
					table.addColumnValue(i,o);
				}
				row++;
				if(size<=row){
					break;
				}
			}
			table.setSelectConut(row);
		}finally{
			close(resultSet);
		}
		

		return table;
	}
	

	@Override
	public int[] executePreparedUpdata(Connection connection, String sql[],
			Table updateData) throws SQLException {
		for (int n = 0; n < sql.length; n++) {
			prepInsert(connection, sql, updateData, n);
			prepUpdata(connection, sql, updateData, n);
			prepDelete(connection, sql, updateData, n);
		}
		return new int[0];
	}

	private void prepDelete(Connection connection, String[] sql,
			Table updateData, int n)  throws SQLException{
		if(n!=2) return;
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = connection.prepareStatement(sql[n]);
	
			int rowNum = updateData.getRowNum();
			Column[] columns = updateData.getColumns();
			int length = columns.length;
			boolean isExcute =false;
			int number = 1;
			for (int i = 0; i < rowNum; i++) {
				Cell cell = updateData.getValue(i,0);
				
				if(UITable.DELETE.equals(cell.getNew_value())){
					number=1;
					for (int j = 1; j < length; j++) {
						Cell value = updateData.getValue(i,j);
						if(columns[j].isPrimaryKey()){
							isExcute =true;
							setPrepare(prepareStatement,value.getOld_value(), columns[j].getDataType(), number);
							number++;
						}
					}
					prepareStatement.addBatch();
				}
			}
			if(!isExcute)return;
			prepareStatement.executeBatch();
		} catch (SQLException e) {	
			logger.error(e.getMessage()+"\nSQL:"+sql);
			throw new SQLException(e.getMessage());
		}
		finally{
			try {
				prepareStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void prepInsert(Connection connection, String[] sql,
			Table updateData, int n) {
		if(n!=0) return;
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = connection.prepareStatement(sql[n]);
			int rowNum = updateData.getRowNum();
			Column[] columns = updateData.getColumns();
			int length = columns.length;
			boolean isExcute =false;
			for (int i = 0; i < rowNum; i++) {
				Cell cell = updateData.getValue(i,0);
				if(UITable.ADD.equals(cell.getNew_value())){
					for (int j = 1; j < length; j++) {
						Cell value = updateData.getValue(i,j);
						isExcute =true;
						setPrepare(prepareStatement,value.getNew_value(), columns[j].getDataType(), j);
					}
					prepareStatement.addBatch();
				}
			}
			if(!isExcute)return;
			prepareStatement.executeBatch();
		
		} catch (SQLException e) {
			logger.error(e.getMessage()+"\nSQL:"+sql);
			e.printStackTrace();
		}
		finally{
			closeAll(prepareStatement);
		}
//		tab.setResult_type(Table.RESULT_TYPE);
//		tab.setUpdataCount(updateCount);
	}

	private void prepUpdata(Connection connection, String[] sql,
			Table updateData, int n) throws SQLException {
		if(n!=1)return;
		PreparedStatement prepareStatement = null;
		try{
			 prepareStatement = connection.prepareStatement(sql[n]);
			int rowNum = updateData.getRowNum();
			
			Column[] columns = updateData.getColumns();
			int length = columns.length;
			int temp = 0;
			int temp1 = 0;	
			boolean isExcute = false;
			for (int i = 0; i < rowNum; i++) {
				Cell cell = updateData.getValue(i, 0);
				temp1 = 0;	
				if(cell!=null&&UITable.EDIT.equals(cell.getNew_value())){
					for (int j = 1; j < columns.length; j++) {
						Column column = columns[j];
						Cell value = updateData.getValue(i, j);
						if(columns[j].getColumnCode().equals("new_member")){
							columns[j].getDataType();
						}
						if(value.isChange()){
							setPrepare(prepareStatement,  value.getNew_value(),column.getDataType(), j-temp);
						}else{
							setPrepare(prepareStatement,  value.getOld_value(),column.getDataType(), j-temp);
						}
						if(column.isPrimaryKey()){
							isExcute=true;
							setPrepare(prepareStatement,value.getOld_value(),column.getDataType(), temp1+length);
							temp1++;
						}
						
					}
					prepareStatement.addBatch();
					
				}
				
			}
			
			if(!isExcute)return;
			int[] executeBatch = prepareStatement.executeBatch();
			int rowCount = 0;
			for (int i = 0; i < executeBatch.length; i++) {
				rowCount += executeBatch[i];
			}
			updateData.setUpdataCount(rowCount);
		}finally{
			close(prepareStatement);
		}
	}
	
	private void setPrepare(PreparedStatement pre,Object object ,int type,int i) throws SQLException{
		if(UtilString.isNull(object)){
			setNull(pre,i, type);return;
		}
		switch(type){
		case Types.CHAR:
			setWherePrepare(pre,object,Types.CHAR,i);break;
		case Types.VARCHAR:
			setWherePrepare(pre,object,Types.VARCHAR,i);break;
		case Types.LONGVARCHAR:
			setWherePrepare(pre,object,Types.LONGVARCHAR,i);break;
		case Types.BIT:
			setWherePrepare(pre,object,Types.BIT,i);break;
		case Types.NUMERIC:
			setWherePrepare(pre,object,Types.NUMERIC,i);break;
		case Types.SMALLINT:
			setWherePrepare(pre,object,Types.SMALLINT,i);break;
		case Types.INTEGER:
			setWherePrepare(pre,object,Types.INTEGER,i);break;
		case Types.BIGINT:
			setWherePrepare(pre,object,Types.BIGINT,i);break;
		case Types.REAL:
		case Types.FLOAT:
			setWherePrepare(pre,object,Types.FLOAT,i);break;
		case Types.DOUBLE:
			setWherePrepare(pre,object,Types.DOUBLE,i);break;
		case Types.VARBINARY:
		case Types.BINARY:
			setWherePrepare(pre,object,Types.BINARY,i);break;
		case Types.DATE:
			setWherePrepare(pre,object,Types.DATE,i);break;
		case Types.TIME:
			setWherePrepare(pre,object,Types.TIME,i);break;
		case Types.TIMESTAMP:
			setWherePrepare(pre,object,Types.TIMESTAMP,i);break;
		case Types.CLOB:
			setWherePrepare(pre,object,Types.CLOB,i);break;
		case Types.BLOB:
			setWherePrepare(pre,object,Types.BLOB,i);break;
		case Types.ARRAY:
			setWherePrepare(pre,object,Types.ARRAY,i);break;
		case Types.REF:
			setWherePrepare(pre,object,Types.REF,i);break;
		case Types.STRUCT:
			setWherePrepare(pre,object,Types.STRUCT,i);break;
		default:
			pre.setString(i, object.toString());break;
		}
	}
	private void setWherePrepare(PreparedStatement pre,Object object ,int type,int i) throws SQLException{
		if(object==null||"".equals(object.toString().trim())){
			setNull(pre,i,type);return;
		}
		switch(type){
		
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			pre.setString(i, object.toString());break;
		case Types.BIT:
			pre.setBoolean(i, new Boolean(object.toString()));break;
		case Types.NUMERIC:
			pre.setBigDecimal(i, new BigDecimal(object.toString()));break;
		case Types.SMALLINT:
			pre.setShort(i,  new Short(object.toString()));break;
		case Types.INTEGER:
			pre.setInt(i, new Integer(object.toString()));break;
		case Types.BIGINT:
			pre.setLong(i,  new Long( object.toString()));break;
		case Types.REAL:
		case Types.FLOAT:
			pre.setFloat(i,  new Float(object.toString()));break;
		case Types.DOUBLE:
			pre.setDouble(i,   new Double(object.toString()));break;
		case Types.VARBINARY:
		case Types.BINARY:
			pre.setBytes(i, (byte[]) object);break;
		case Types.DATE:
			pre.setDate(i,  Date.valueOf(object.toString()));break;
		case Types.TIME:
			pre.setTime(i,   Time.valueOf(object.toString()));break;
		case Types.TIMESTAMP:
			pre.setTimestamp(i,  (Timestamp.valueOf(object.toString())) );break;
		case Types.CLOB:
			pre.setClob(i,(Clob) object);break;
		case Types.BLOB:
			pre.setBlob(i,(Blob) object);break;
		case Types.ARRAY:
			pre.setArray(i,(Array) object);break;
		case Types.REF:
			pre.setRef(i, (Ref) object);break;
		case Types.STRUCT:
			pre.setRef(i, (Ref) object);break;
		default:
			pre.setString(i, object.toString());break;
		}
	}
	private Cell getResult(ResultSet result,int type,int i) throws SQLException{
		if(Types.TIMESTAMP == type){
			try{
				result.getObject(i);
			}catch(SQLException e){
				Cell cell = new Cell("0000-00-00 00:00:00");
				return cell;
			}
			
		}
		if(result.getObject(i)==null){
			Cell cell = new Cell("");
			return cell;
		}
		Object o=null;
		switch(type){
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			o = result.getString(i);break;
		case Types.BIT:
			o = result.getBoolean(i);break;
		case Types.NUMERIC:
			o = result.getBigDecimal(i);break;
		case Types.SMALLINT:
			o = result.getShort(i);break;
		case Types.INTEGER:
			o = result.getBigDecimal(i);break;
		case Types.BIGINT:
			o = result.getLong(i);break;
		case Types.REAL:
		case Types.FLOAT:
			o = result.getFloat(i);break;
		case Types.DOUBLE:
			o = result.getDouble(i);break;
		case Types.VARBINARY:
		case Types.BINARY:
			o = result.getBytes(i);break;
		case Types.DATE:
			o = result.getDate(i);break;
		case Types.TIME:
			o = result.getTime(i);break;
		case Types.TIMESTAMP:
			o = UtilDate.getFomatDate(result.getTimestamp(i)) ;break;
		case Types.CLOB:
			o = result.getClob(i);break;
		case Types.BLOB:
			o = result.getBlob(i);break;
		case Types.ARRAY:
			o = result.getArray(i);break;
		case Types.REF:
			o = result.getRef(i);break;
		case Types.STRUCT:
			o = result.getRef(i);break;
		default:
			o = result.getString(i);break;
		}
		
		Cell cell = new Cell(o);
		return cell;
	}
	private void setNull(PreparedStatement pre,int n,int type) throws SQLException{
		switch(type){
		case Types.CHAR:
			pre.setNull(n,Types.CHAR);break;
		case Types.VARCHAR:
			pre.setNull(n,Types.VARCHAR);break;
		case Types.LONGVARCHAR:
			pre.setNull(n,Types.LONGVARCHAR );break;
		case Types.BIT:
			pre.setNull(n,Types.BIT );break;
		case Types.NUMERIC:
			pre.setNull(n,Types.NUMERIC );break;
		case Types.SMALLINT:
			pre.setNull(n,Types.SMALLINT );break;
		case Types.INTEGER:
			pre.setNull(n,Types.INTEGER );break;
		case Types.BIGINT:
			pre.setNull(n,Types.BIGINT );break;
		case Types.REAL:
			pre.setNull(n,Types.REAL );break;
		case Types.FLOAT:
			pre.setNull(n,Types.FLOAT );break;
		case Types.DOUBLE:
			pre.setNull(n,Types.DOUBLE );break;
		case Types.VARBINARY:
			pre.setNull(n,Types.VARBINARY );break;
		case Types.BINARY:
			pre.setNull(n,Types.BINARY );break;
		case Types.DATE:
			pre.setNull(n,Types.DATE );break;
		case Types.TIME:
			pre.setNull(n,Types.TIME );break;
		case Types.TIMESTAMP:
			pre.setNull(n,Types.TIMESTAMP );break;
		case Types.CLOB:
			pre.setNull(n,Types.CLOB );break;
		case Types.BLOB:
			pre.setNull(n,Types.BLOB );break;
		case Types.ARRAY:
			pre.setNull(n,Types.ARRAY );break;
		case Types.REF:
			pre.setNull(n,Types.REF );break;
		case Types.STRUCT:
			pre.setNull(n,Types.STRUCT );break;
		default:
			pre.setNull(n,Types.CHAR );break;
		}
	
	}
//	@Override
//	public int[] executeBatchInsert(Connection conn, String[] sql)
//			throws SQLException {
//		Statement createStatement = conn.createStatement();
//		for (int i = 0; i < sql.length; i++) {
//			createStatement.addBatch(sql[i]);
//		}
//		int[] executeBatch = createStatement.executeBatch();
//		return executeBatch;
//	}

	/**
	 * ，默认分页方式
	 * @param sql
	 * @param statement 
	 * @param ResultSet
	 * @throws SQLException 
	 */
//	public ResultSet setPaging(String sql,Statement statement) throws SQLException{
//		ResultSet executeQuery = statement.executeQuery(sql);
				
		
//		resultSet.getr
//		statement.setMaxRows(page.getEndIndex());
//		executeQuery.absolute(page.getBeginIndex());
//		return resultSet;
//	}
	// ///////////////////////////////////////////////////////////////

	public static void close(Statement statent) throws SQLException {
		if (statent != null) {
			statent.close();
		}
	}

	public static void close(ResultSet result) throws SQLException {
		if (result != null) {
			result.close();
		}
	}


	public static void closeAll(Object... objs) {
		for (Object obj : objs) {
			try {
				if (obj instanceof ResultSet)
					close((ResultSet) obj);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (obj instanceof Statement)
					close((Statement) obj);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (obj instanceof PreparedStatement)
					close((PreparedStatement) obj);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public String getViewDDL(String viewName, Connection conn) throws SQLException {
		String viewDdl = xmlddlConfig.getScript(getDBName(),"VIEW").replace("VIEW_NAME", viewName);
		Statement createStatement = null;
		ResultSet executeQuery= null;
		String ddl = "";
		try {
			createStatement = conn.createStatement();
			executeQuery = createStatement.executeQuery(viewDdl);
			Clob clob = null;
			while(executeQuery.next()){
				 clob = executeQuery.getClob(1);
			}
			if(clob!=null){
				ddl = clob.getSubString((long)1,(int)clob.length());
			}
		} 
		finally{
			closeAll(executeQuery,createStatement);
		}
		return ddl;
	}
	
	
	@Override
	public String getTableName(String tableName) {
		return tableName;
	}
	@Override
	public Table getViewSessions(String sql, Connection conn) throws SQLException {
	
		Statement createStatement = null;
		ResultSet executeQuery= null;
		try {
			createStatement = conn.createStatement();
			executeQuery = createStatement.executeQuery(sql);
			return packResultSet(executeQuery, false);
		} 
		finally{
			closeAll(executeQuery,createStatement);
			return new Table();
		}
	}
	
	public ArrayList<String> getTableSpace(String userName, Connection conn) throws SQLException{
		String tableSpace = xmlddlConfig.getScript(getDBName(),"TABLESPACE_NAME");
		Statement createStatement = null;
		ResultSet executeQuery= null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			createStatement = conn.createStatement();
			executeQuery = createStatement.executeQuery(tableSpace);
			while(executeQuery.next()){
				list.add(executeQuery.getString("TABLESPACE_NAME"));
			}
		} 
		finally{
			closeAll(executeQuery,createStatement);
		}
		return list;
	}
	
	public String getTableDDL(String tableName, Connection conn){
		String viewDdl = xmlddlConfig.getScript(getDBName(),"TABLE").replace("TABLE_NAME", tableName);
		Statement createStatement = null;
		ResultSet executeQuery= null;
		String ddl ="";
		try {
			createStatement = conn.createStatement();
			executeQuery = createStatement.executeQuery(viewDdl);
			Clob clob = null;
			while(executeQuery.next()){
				 clob = executeQuery.getClob(1);
			}
			if(clob!=null){
				ddl = clob.getSubString((long)1,(int)clob.length());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			closeAll(executeQuery,createStatement);
		}
		return ddl;
	}
	
	
	public void excute(String[] script,Connection conn) throws SQLException{
		Statement createStatement = null;
		try {
			createStatement = conn.createStatement();
			for (int i = 0; i < script.length; i++) {
				createStatement.addBatch(script[i]);
			}
			createStatement.executeBatch();
		} 
		finally{
			closeAll(createStatement);
		}
	}
	
	@Override
	public String getTableSpaceDDL(String tableSpaceName,Connection conn) throws SQLException{
		String viewDdl = xmlddlConfig.getScript(getDBName(),"TABLESPACE").replace("TABLESPACE_NAME", tableSpaceName);
		Statement createStatement = null;
		ResultSet executeQuery= null;
		String ddl = "";
		try {
			createStatement = conn.createStatement();
			executeQuery = createStatement.executeQuery(viewDdl);
			Clob clob = null;
			while(executeQuery.next()){
				 clob = executeQuery.getClob(1);
			}
			if(clob!=null){
				ddl = clob.getSubString((long)1,(int)clob.length());
			}
		} 
		finally{
			closeAll(executeQuery,createStatement);
		}
		return ddl;
	}
	
	public int getTablesRow(Connection connection,String tableName) throws SQLException{
		Statement  createStatement = null;
		ResultSet executeQuery = null;
		ResultSet primaryKeys = null;
		 DatabaseMetaData metaData=null;
		int row = 0;
		try {
			createStatement = connection.createStatement();
			  metaData = connection.getMetaData();
				primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
					executeQuery = createStatement.executeQuery("SELECT COUNT(1) FROM "+tableName);

			executeQuery.next();
			row = executeQuery.getInt(1);
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		finally{
			closeAll(executeQuery,createStatement,primaryKeys,metaData);
		}
		return row;
	}
	@Override
	public int getTablesRowFromSql(Connection connection,String sql) throws SQLException{
		Statement  createStatement = null;
		ResultSet executeQuery = null;
		int row = 0;
		try {
			createStatement = connection.createStatement();
//			int select = sql.toUpperCase().indexOf("SELECT");
//			int from = sql.toUpperCase().indexOf("FROM");
//			String start = sql.substring(0,select+6);
//			String end = sql.substring(from,sql.length());
//			sql = start+" count(1) "+ end;
			
			
			
			String upperCase = sql.toUpperCase();
//			String[] split = upperCase.split("FROM");
//			int start = upperCase.indexOf("SELECT")+6;
//			int fr = 0;
//			for (int i = 0; i < split.length; i++) {
//				fr = upperCase.indexOf("FROM",start);
//				String[] split2 = upperCase.substring(start, fr).split("FROM");
//				if(split2.length>1){
//					start = fr+4;
//				}else{
//					break;
//				}
//				if(upperCase.indexOf("FROM",start)==-1){
//					break;
//				}
//			}
//			
//			String substring = upperCase.substring(upperCase.indexOf("SELECT")+6, fr);
//			upperCase.replace(substring, " count(1) ");
			
			String sql1 = "SELECT COUNT(1) FROM ("+upperCase+")";
			executeQuery = createStatement.executeQuery(sql1);

			executeQuery.next();
			row = executeQuery.getInt(1);
		} 
		finally{
			closeAll(executeQuery,createStatement);
		}
		return row;
	}
	
	@Override
	public String page(String sql) {
		String sqlPage = sql;
		String sqlCopy = sql.replaceAll(" ","").replace("(", "").toLowerCase();
		
		boolean endsWith = sqlCopy.endsWith("dual");
		if(endsWith){
			return sql;
		}
		if(sql!=null&&(sqlCopy.startsWith("select"))){
			Integer size = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "page_size"));
			 sqlPage = (xmlddlConfig.getScript(getDBName(),"PAGE_SIZE").replaceFirst("TABLE_NAME", sql)).replace("PAGE_SIZE", size+"");
		}
		return sqlPage;
	}
	public Statement getLazyStatement(Connection connection)
			throws SQLException {
		Statement createStatement2 = connection.createStatement();
		createStatement2.setFetchSize(getFetchSize());
		createStatement2.setFetchDirection(ResultSet.FETCH_REVERSE); 
		return createStatement2;
	}
}

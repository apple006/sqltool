package com.view.tool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.Properties;

import com.view.movedata.execut.MoveDataCtr;
import com.view.movedata.exp.entity.ColumnType;
import com.view.movedata.exp.entity.DataFromBean;
import com.view.movedata.exp.entity.MoveDataMsg;
import com.view.movedata.exp.entity.NowDate;
import com.view.movedata.exp.entity.OddColumnMsg;
import com.view.movedata.exp.entity.RandomTest;

public class HelpRslutToSql {
	private static final String BINDVARIABLES = "bindvariables.properties";

	/**
	 * 
	 * 
	 * @param metaData
	 *            ResultSetMetaData
	 * @param tableName
	 *            表名
	 * @param columnCount
	 *            列数
	 * @return "insert into <b>表名</b> values("
	 * @throws SQLException
	 */
	public static String getStartInsert(ResultSetMetaData metaData, String tableName, int columnCount)
			throws SQLException {
		StringBuffer col = new StringBuffer("INSERT  INTO  ");
		col.append(tableName);
		col.append("  (");
		for (int i = 1; i < columnCount; i++) {
			col.append(metaData.getColumnName(i));
			col.append(",");
		}
		col.append(metaData.getColumnName(columnCount));
		col.append("  )  ");
		col.append("VALUES  (");
		return col.toString();
	}

	/**
	 * 将 ResultSet返回的记过封装成insert语句 values()部分与 getStartInsert方法使用
	 * 
	 * @param executeQuery
	 *            ResultSet结果集
	 * @param strBuf
	 *            StringBuffer
	 * @param columnCount
	 * @throws SQLException
	 */
	public static void getEndInsert(ResultSet executeQuery, StringBuffer strBuf, int columnCount) throws SQLException {
		for (int i = 1; i < columnCount; i++) {
			Object object = executeQuery.getObject(i);
			if (object == null) {
				strBuf.append(object);
			} else {
				strBuf.append("'");
				strBuf.append(object);
				strBuf.append("'");
			}
			strBuf.append(",");

		}
		if (executeQuery.getObject(columnCount) == null) {
			strBuf.append(executeQuery.getObject(columnCount));
		} else {
			strBuf.append("'");
			strBuf.append(executeQuery.getObject(columnCount));
			strBuf.append("'");
		}
		strBuf.append(");");
		strBuf.append("\n");
	}

	/**
	 * 获得查询语句
	 * 
	 * @param moveDataMsg
	 * @return
	 */
	public static String getSelect(MoveDataCtr crt) {

		MoveDataMsg moveDataMsg = crt.getMoveDataMsg();
		String select = getSelect(moveDataMsg);
		String sql = select;
		boolean changeWhere = crt.isForceWhere();
		if (changeWhere) {
			sql = addForceWhere(select);
		}
		return sql;
	}

	public static String getSelect(MoveDataMsg moveDataMsg) {
		if (MoveDataMsg.FROM_DATA_SQL == moveDataMsg.getType()) {
			String sql = ((DataFromBean) moveDataMsg.getSelectTableName()).getSql();

			// OddColumnMsg[] oddColumns = moveDataMsg.getOddColumns();
			// StringBuffer select = new StringBuffer(" ");
			// for (int i = 0; i < oddColumns.length-1; i++) {
			// if(ColumnType.DEFULTCOLUMN.equals(oddColumns[i].getDefindMate().getType())){
			// select.append(oddColumns[i].getFcolumnName());
			// select.append(",");
			// }
			// }
			//

			// String upperCase = sql.toUpperCase();
			// String[] split = upperCase.split("FROM");
			// int start = upperCase.indexOf("SELECT")+6;
			// int fr = 0;
			// for (int i = 0; i < split.length; i++) {
			// fr = upperCase.indexOf("FROM",start);
			// String[] split2 = upperCase.substring(start, fr).split("FROM");
			// if(split2.length>1){
			// start = fr+4;
			// }else{
			// break;
			// }
			// if(upperCase.indexOf("FROM",start)==-1){
			// break;
			// }
			// }
			//
			// String substring =
			// upperCase.substring(upperCase.indexOf("SELECT")+6, fr);
			// String replace = upperCase.replace(substring, select);
			//
			// String addForceWhere = HelpRslutToSql.addForceWhere( sql);
			return addDefultWhere(sql);
		} else {
			String selectTableName = moveDataMsg.getSelectTableName().toString();
			OddColumnMsg[] oddColumns = moveDataMsg.getOddColumns();
			StringBuffer select = new StringBuffer("SELECT ");
			for (int i = 0; i < oddColumns.length - 1; i++) {
				if (ColumnType.DEFULTCOLUMN.equals(oddColumns[i].getDefindMate().getType())) {
					select.append(oddColumns[i].getFcolumnName());
					select.append(",");
				}
			}
			if (ColumnType.DEFULTCOLUMN.equals(oddColumns[oddColumns.length - 1].getDefindMate().getType())) {
				select.append(oddColumns[oddColumns.length - 1].getFcolumnName());
			} else {
				select.deleteCharAt(select.length() - 1);
			}
			select.append(" FROM ");
			select.append(selectTableName);
			select.append(" WHERE 1=1 ");

			String sql = select.toString();
			String addDefultWhere = addDefultWhere(sql);
			return addDefultWhere;
		}

	}

	public static String addForceWhere(String sql) {
		Properties properties = HelpProperties.getAll(BINDVARIABLES);
		Iterator<Object> iterator = properties.keySet().iterator();
		String key;
		String val;
		boolean b = false;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			val = properties.getProperty(key);
			String[] split = val.split("#");
			// if(ColumnType.DEFULT_WHERE.equals(split[0])){
			// sql.replaceAll(key, split[1]);
			// }
			if (ColumnType.FORCE_WHERE.equals(split[0])) {
				// sql+= " AND "+split[1];
				sql = " SELECT * FROM (" + sql + ") WHERE  " + split[1];
				;

				b = true;
			}

		}
		// if(b){
		// sql= " SELECT * FROM ("+sql+") WHERE "+split[1];;
		// }
		return sql.replace("GETHASHCODE", "getHashCode");
	}

	public static String addDefultWhere(String sql) {
		Properties properties = HelpProperties.getAll(BINDVARIABLES);
		Iterator<Object> iterator = properties.keySet().iterator();
		String key;
		String val;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			val = properties.getProperty(key);
			String[] split = val.split("#");
			// if(ColumnType.DEFULT_WHERE.equals(split[0])){
			// sql.replaceAll(key, split[1]);
			// }
			if (ColumnType.DEFULT_WHERE.equals(split[0])) {
				if(key.indexOf("$")!=-1){
					sql = sql.replaceAll("\\" + key, split[1]);
				}else{
					sql = sql.replaceAll( key, split[1]);
				}

			}

		}
		return sql;
	}

	/**
	 * 根据产存的结果集ResultSet 与列数columnCount 返回这一个行的一个信息Object数组
	 * 
	 * @param executeQuery
	 * @param columnCount
	 * @param moveDataMsg
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static Object[] getArrayValue(ResultSet executeQuery, MoveDataMsg moveDataMsg) throws SQLException {
		OddColumnMsg[] oddColumns = moveDataMsg.getOddColumns();
		Object[] o = new Object[oddColumns.length];
		for (int i = 0, j = 1; i < o.length; i++) {
			if (ColumnType.DEFULTCOLUMN.equals(oddColumns[i].getDefindMate().getType())) {
				int columnType = executeQuery.getMetaData().getColumnType(i+1);
				if (Types.CLOB == columnType) {
					Clob clob = executeQuery.getClob(oddColumns[i].getFcolumnName().toString());
					if(clob==null){
						o[i] = null;
						continue;
					}
					Reader characterStream = clob.getCharacterStream();
					BufferedReader br = new BufferedReader(characterStream);
					String s = null;
					StringBuffer sb = new StringBuffer();
					try {
						s = br.readLine();
						while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
							sb.append(s);
							s = br.readLine();
						}
						o[i] = new Object[]{columnType,sb.toString()};
					} catch (IOException e) {
						e.printStackTrace();
					}finally {
						try {
							characterStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}else if(Types.CLOB == columnType){
					Blob blob = executeQuery.getBlob(oddColumns[i].getFcolumnName().toString());
					if(blob==null){
						o[i] = null;
						continue;
					}
					InputStream binaryStream = blob.getBinaryStream();
					BufferedInputStream in= new BufferedInputStream(binaryStream);
					int read =0 ;
					StringBuffer sb = new StringBuffer();
					try {
						byte[] b = new byte[1024];
						while (read!=-1) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
							read = in.read(b);
							sb.append(String.valueOf(b));
						}
						o[i] = new Object[]{columnType,sb.toString()};
					} catch (IOException e) {
						e.printStackTrace();
					}
					finally {
						try {
							binaryStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}else{
					o[i] = executeQuery.getObject(oddColumns[i].getFcolumnName().toString());
				}
			} else if (oddColumns[i].getDefindMate().getType().equals(ColumnType.FINAL)) {
				o[i] = oddColumns[i].getFcolumnName();
			} else if (oddColumns[i].getDefindMate().getType().equals(ColumnType.GROUPCOLUMN)) {
				o[i] = ((UUID) oddColumns[i].getFcolumnName()).getStrPk();
			} else if (oddColumns[i].getDefindMate().getType().equals(ColumnType.TEST)) {
				o[i] = (((RandomTest<String>) oddColumns[i].getFcolumnName()).getRandomTest());
			} else if (oddColumns[i].getDefindMate().getType().equals(ColumnType.NOW_TIME)) {
				o[i] = (((NowDate) oddColumns[i].getFcolumnName()).getNowDate());
			}
		}
		return o;
	}

	/**
	 * 根据MoveDataMsg 信息转换sql语句 如 insert into <tableName> values(<?,?)
	 * 
	 * @param moveDataMsg
	 * @return
	 */
	public static String getInsertPrepare(MoveDataMsg moveDataMsg) {

		OddColumnMsg[] oddColumns = moveDataMsg.getOddColumns();
		String[] columns = new String[oddColumns.length];
		for (int i = 0; i < oddColumns.length; i++) {
			columns[i] = oddColumns[i].getTcolumnName();
		}
		String isertPrePareSql = getInsertPrePareSql(moveDataMsg.getInsertTableName().toString(), columns);
		return isertPrePareSql;
	}
	public static String getScriptSql(MoveDataMsg moveDataMsg) {
		String sql = ((DataFromBean)moveDataMsg.getInsertTableName()).getSql();
		String addDefultWhere = addDefultWhere(sql);
		return addDefultWhere;
	}

	public static String getInsertPrePareSql(String tableName, String[] oddColumns) {
		StringBuffer insert = new StringBuffer("INSERT INTO ");
		insert.append(tableName);
//		insert.append(" partition(P_TRANS_QUERY_201702) ");
		insert.append(" ( ");
		for (int i = 0; i < oddColumns.length - 1; i++) {
			insert.append(oddColumns[i]);
			insert.append(",");
		}
		insert.append(oddColumns[oddColumns.length - 1]);
		insert.append(" ) VALUES ( ");
		for (int i = 0; i < oddColumns.length - 1; i++) {
			insert.append("?");
			insert.append(",");
		}
		insert.append("?");
		insert.append(")");
		return insert.toString();
	}
}

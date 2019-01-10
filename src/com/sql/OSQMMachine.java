package com.sql;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dao.entity.Column;
import com.dao.entity.Table;
import com.ui.extensible.UITable;

abstract class OSQMMachine implements ISQLMachine{

	
//	public Table
	@Override
	public String[] getSqls(Table updateData){
		String sqls [] =new String[3];
		sqls[0] = getInsertSQL(updateData);
		sqls[1] = getUpdataSQL(updateData);
		sqls[2] = getDeleteSQL(updateData);
		return sqls;
	}
	private String getDeleteSQL(Table updateData) {
		String tableName = updateData.getTableCode();
		Column[] columns = updateData.getColumns();
		String deleteSql = getDeleteSql(tableName, columns);
		return deleteSql;
	}
	public String getInsertSQL(Table updateData){
		
		String tableName = updateData.getTableCode();
		Column[] columns = updateData.getColumns();
		String insertSql = getInsertSql(tableName, columns);
		
		return insertSql;
	}
	public String getUpdataSQL(Table updateData){
		
		String tableName = updateData.getTableCode();
		Column[] columns = updateData.getColumns();
		String insertSql = getUpdataSql(tableName, columns);
		
		return insertSql;
	}

	private String getUpdataSql(String tableName, Column[] colum) {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append(tableName);
		sql.append(" SET ");
		for (int i = 0; i < colum.length-1; i++) {
			if(UITable.IS_EDIT_TYPE.equals(colum[i].getColumnCode())){
				continue;
			}
			sql.append(tableName);
			sql.append(".");
			sql.append(colum[i].getColumnCode());
			sql.append("=?,");
		}
			
		sql.append(tableName);
		sql.append(".");
		sql.append(colum[colum.length-1].getColumnCode());
		sql.append("=? ");
		sql.append(" WHERE  ");
		
		ArrayList<String> columnPk = new ArrayList<String>();
		for (int i = 0; i < colum.length; i++) {
			if(UITable.IS_EDIT_TYPE.equals(colum[i].getColumnCode())){
				continue;
			}
			if(colum[i].isPrimaryKey()){
				columnPk.add(colum[i].getColumnCode());
			}
		}
		for (int i = 0; i < columnPk.size()-1; i++) {
			String column = columnPk.get(i);
			sql.append(tableName);
			sql.append(".");
			sql.append(column);
			sql.append(" = ? AND ");
		}
		if(!columnPk.isEmpty()){
			sql.append(tableName);
			sql.append(".");
			sql.append(columnPk.get(columnPk.size()-1));
			sql.append(" =  ? ");
		}
		
		return sql.toString();
	}
	private String getInsertSql(String tableName, Column[] colum) {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO ");
		sql.append(tableName);
		sql.append(" ( ");
		for (int i = 0; i < colum.length-1; i++) {
			if(UITable.IS_EDIT_TYPE.equals(colum[i].getColumnCode())){
				continue;
			}
			sql.append(colum[i].getColumnCode());
			sql.append(",");
		}
		if(!UITable.IS_EDIT_TYPE.equals(colum[colum.length-1].getColumnCode())){
			sql.append(colum[colum.length-1].getColumnCode());
		}
		else{
			sql.deleteCharAt(sql.length()-1);
		}
		sql.append(" ) ");
		sql.append(" VALUES ( ");
		
		for (int i = 0; i < colum.length-1; i++) {
			if(UITable.IS_EDIT_TYPE.equals(colum[i].getColumnCode())){
				continue;
			}
			sql.append("?");
			sql.append(",");
		}
		if(!UITable.IS_EDIT_TYPE.equals(colum[colum.length-1].getColumnCode())){
			sql.append("?");
		}else{
			sql.deleteCharAt(sql.length()-1);
		}
		sql.append(" ) ");
		
		return sql.toString();
	}
	private String getDeleteSql(String tableName, Column[] colum) {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ");
		sql.append(tableName);
		sql.append(" WHERE  ");
		ArrayList<String> column = new ArrayList<String>();
		for (int i = 0; i < colum.length; i++) {
			if(UITable.IS_EDIT_TYPE.equals(colum[i].getColumnCode())){
				continue;
			}
			if(colum[i].isPrimaryKey()){
				column.add(colum[i].getColumnCode());
			}
		}
		for (int i = 0; i < column.size()-1; i++) {
			sql.append(column.get(i));
			sql.append(" = ? ");
			sql.append(" AND ");
		}
		if(!column.isEmpty()){
			sql.append(column.get(column.size()-1));
			sql.append(" = ? ");
		}
		return sql.toString();
	}
	
	public String getTableName(String sql){
		Pattern p = Pattern.compile("\\W*select.+from\\W+(\\w+)\\W*.*",Pattern.CASE_INSENSITIVE );//定义整则表达式
		Matcher m = p.matcher(sql);
		while(m.find()){
			 return m.group(1);
		}
		return "";
	}
}

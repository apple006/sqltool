package com.sql.entity;

public class InsertEntity {
	
	
	private String tableName;
	
	public InsertEntity(String tableName){
		this.tableName = tableName;
	}
	
	public String getInsertInto(String tableName,String[] colum,String[] value){
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO ");
		sql.append(tableName);
		sql.append(" ( ");
		for (int i = 0; i < colum.length-1; i++) {
			String string = colum[i];
			sql.append(i);
			sql.append(",");
		}
		sql.append(colum[colum.length-1]);
		sql.append(" ) ");
		sql.append(" VALUES ( ");
		
		for (int i = 0; i < value.length-1; i++) {
			sql.append(value[i]);
			sql.append(",");
		}
		sql.append(value[value.length-1]);
		sql.append(" ) ");
		return sql.toString();
	}
	
}

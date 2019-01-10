package com.view.tool.entity;

import java.util.Hashtable;

public class PDTableEntity {

	private String tableName ;
	private String tableCode ;
	private Hashtable<String,PDColumnEntity> columns;
	
	public PDTableEntity(String tableName, String tableCode) {
		this.tableName = tableName;
		this.tableCode = tableCode;
		columns = new Hashtable<String,PDColumnEntity>();
	}
	
	public void addColumn(PDColumnEntity column){
		columns.put(column.getColumnCode(), column);
	}
	
	public PDColumnEntity getColumn(String columnCode){
		return columns.get(columnCode)==null?new PDColumnEntity():columns.get(columnCode);
	}

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	
	
	
}

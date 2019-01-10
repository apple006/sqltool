package com.view.tool.entity;

public class PDColumnEntity {
	private String columnName;
	private String columnCode;
	private String columnComment;
	private String dataType;
	private String length;
	
	public PDColumnEntity(){
		
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public PDColumnEntity(String columnName, String columnCode,
			String dataType, String length) {
		this.columnName = columnName;
		this.columnCode = columnCode;
		this.dataType = dataType;
		this.length = length;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnCode() {
		return columnCode;
	}

	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getLength() {
		return length;
	}

	public void setColumnLength(String length) {
		this.length = length;
	}


	
}

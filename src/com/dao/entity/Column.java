package com.dao.entity;

public class Column implements Cloneable{
	private String columnCode ; 
	private String columnName ; 

	private String remarks;
	private String column_ref;
	private String columnTypeName;
	private int dataType;
	private int number;
	//列的大小
	private int columnSize;
	//对于 char 类型，该长度是列中的最大字节数 
	private String charOctetLength;
	// 小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型，则返回 Null。 
	private String decimal_digits;
	private int nullable;
	//IS_AUTOINCREMENT String => 指示此列是否自动增加 
	private String isAutoincrement;
	private boolean isPrimaryKey;
	private String pk_name;
	private boolean getImportedKeys;
	private int scale;
	
	
	
	public String getColumnCode() {
		return columnCode;
	}
	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public String getPk_name() {
		return pk_name;
	}
	public void setPk_name(String pk_name) {
		this.pk_name = pk_name;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getColumn_ref() {
		return column_ref;
	}
	public void setColumn_ref(String column_ref) {
		this.column_ref = column_ref;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public void setColumnSize(int i) {
		this.columnSize = i;
	}
	public String getCharOctetLength() {
		return charOctetLength;
	}
	public void setCharOctetLength(String charOctetLength) {
		this.charOctetLength = charOctetLength;
	}
	public String getDecimal_digits() {
		return decimal_digits;
	}
	public void setDecimal_digits(String decimal_digits) {
		this.decimal_digits = decimal_digits;
	}
	public int getNullable() {
		return nullable;
	}
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}
	public String getIsAutoincrement() {
		return isAutoincrement;
	}
	public void setIsAutoincrement(String isAutoincrement) {
		this.isAutoincrement = isAutoincrement;
	}
	public boolean isGetImportedKeys() {
		return getImportedKeys;
	}
	public void setGetImportedKeys(boolean getImportedKeys) {
		this.getImportedKeys = getImportedKeys;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO 自动生成的方法存根
		Table table = new Table();
		return super.clone();
	} 
	
	@Override
	public String toString() {
		if(null!=getColumnName()){
			return  getColumnCode()+"("+getColumnName()+")";
		}
		return  getColumnCode();
	}
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}
	
	public String getColumnTypeName(){
		return this.columnTypeName;
	}
	public int getColumnSize() {
		return columnSize;
	}
	public void setColumnScaleSize(int scale) {
		this.scale = scale;
	}
	
	public int getColumnScaleSize(){
		return this.scale;
	}
}

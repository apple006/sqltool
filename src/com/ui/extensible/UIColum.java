package com.ui.extensible;
public class UIColum{
	private boolean pk;
	private boolean f_pk;
	private String colum_name;
	private String colum_type;
	private String colum_length;
	public UIColum(){
		
	}
	public UIColum(String colum_name){
		this.colum_name = colum_name;
	}
	public boolean isPk() {
		return pk;
	}
	public void setPk(boolean pk) {
		this.pk = pk;
	}
	public boolean isF_pk() {
		return f_pk;
	}
	public void setF_pk(boolean f_pk) {
		this.f_pk = f_pk;
	}
	public String getColum_name() {
		return colum_name;
	}
	public void setColum_name(String colum_name) {
		this.colum_name = colum_name;
	}
	public String getColum_type() {
		return colum_type;
	}
	public void setColum_type(String colum_type) {
		this.colum_type = colum_type;
	}
	public String getColum_length() {
		return colum_length;
	}
	public void setColum_length(String colum_length) {
		this.colum_length = colum_length;
	}
	
}
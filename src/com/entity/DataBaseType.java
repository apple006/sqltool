package com.entity;

public class DataBaseType {
	private String name="";
	private String classUrl="" ;
	public DataBaseType(String name, String classUrl) {
		this.name = name;
		this.classUrl = classUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name==null){
			this.name="";
		}else{
			this.name = name;
		}
	}
	public String getClassUrl() {
		return classUrl;
	}
	public void setClassUrl(String classUrl) {
		this.classUrl = classUrl;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}

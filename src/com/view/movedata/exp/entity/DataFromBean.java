package com.view.movedata.exp.entity;

import java.io.Serializable;

import com.util.UtilDate;

public class DataFromBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String sql;
	public DataFromBean(String name,String sql){
		this.setName(name);
		this.sql  = sql;
	}
	
	public String getSql(){
		return sql;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

package com.entity;

import java.sql.Connection;


/**
 * jdbc jar°üÐÅÏ¢
 * @author Administrator
 *
 */
public class DriverPathInfo {
	private String name ="";
	private String urlFormat ="";
	private String driverClass ="";
	private String fileUrl="";
	
	
	public DriverPathInfo(String name, String urlFormat,String driverClass,String fileUrl) {
		this.name = name;
		this.urlFormat = urlFormat;
		this.driverClass  = driverClass;
		this.fileUrl = fileUrl;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUrlFormat() {
		return urlFormat;
	}


	public void setUrlFormat(String urlFormat) {
		this.urlFormat = urlFormat;
	}


	public String getDriverClass() {
		return driverClass;
	}


	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}


	public String getFileUrl() {
		
		return fileUrl;
	}


	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
	
}

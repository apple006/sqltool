package com.entity;



/**
 * 连接数据库信息
 * @author Administrator
 *
 */
public class PreferencInfo {
	private String name ="";
	private String view ="";
	
	
	public PreferencInfo(String name, String view) {
		this.name = name;
		this.view = view;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return  name;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	
	
}

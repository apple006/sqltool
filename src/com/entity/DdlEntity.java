package com.entity;

import java.util.HashMap;

public class DdlEntity {


	private HashMap<String, String> script;
	public DdlEntity(){
		script = new HashMap<String, String>();
	}
	
	public void setScript(String name,String script){
		this.script.put(name, script);
	}
	public String getScript(String name){
		return script.get(name);
	}
	
}

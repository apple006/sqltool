package com.dao.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Result {
	private Map<String, List<String>> map;
	private String tableName;
	private String insertSql;
	private String updateSql;
	public Map<String, List<String>> getMap() {
		return map;
	}
	public void setMap(Map<String, List<String>> map) {
		this.map = map;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getInsertSql() {
		return insertSql;
	}
	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}
	public String getUpdateSql() {
		return updateSql;
	}
	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}
	public void clearData() {
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()){
			map.get(iterator.next()).clear();
		}
	}
	
}

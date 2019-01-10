package com.excel;

import java.util.HashMap;
import java.util.Map;

public class RowEntity {
	private Map<String,String> row = new HashMap<String, String>();
	void put(String col,String value) throws CheckException{
		if(row.containsKey(col)){
			throw new CheckException("Excel中存在相同的列名！");
		}else{
			row.put(col, value);
		}
		
	}
	/**
	 * @param colName 列名
	 * @return 所在行对用列名的值
	 * @throws Exception 
	 */
	public String getValue(String colName) throws CheckException{
		if(row.containsKey(colName.toUpperCase())){
			return row.get(colName.toUpperCase())==null?"": row.get(colName.toUpperCase()).trim();
		}else{
			throw new CheckException("Excel中不存在["+colName+"]列名！");
		}
	}
}

package com.excel;

import java.util.ArrayList;
import java.util.List;

public class ExcelEntity<T> {
	private int i=0;
	private List<T> rows ;
	public ExcelEntity(){
		rows = new ArrayList<T>();
	}
	
	void add(T row){
		rows.add(row);
	}
	

	/**
	 *
	 * @return Excel中数据行数（不包括表头）
	 */
	public int getRowNum(){
		return rows.size();
	}
	
	/**
	 *
	 * @return 得到所在行信息
	 */
	public T getRowEntity(int lineNumber){
		return rows.get(lineNumber);
	}
	
	public void clear(){
		rows.clear();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getList(){
		return  rows;
	}
}

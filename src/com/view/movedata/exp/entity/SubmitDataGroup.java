package com.view.movedata.exp.entity;

import java.util.ArrayList;


/**
 * 批量插入数据的块数据
 * @author wanghh
 *
 */
public class SubmitDataGroup {
	private int type[];
	private ArrayList<Object[]> object ;
	public SubmitDataGroup(int[] type){
		this.type = type;
		object = new ArrayList<Object[]>();
	}
	
	public void addRow(Object[] row){
		object.add(row);
	}
	public int getSize(){
		return object.size();
	}
	public Object[] getRow(int row){
		return object.get(row);
	}
	
	public void clear(){
		object.clear();
	}
	
	public int getType(int i){
		return type[i];
	}
	
	public boolean isEmpty(){
		return object.isEmpty();
	}
}

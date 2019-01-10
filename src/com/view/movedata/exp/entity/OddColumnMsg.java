package com.view.movedata.exp.entity;

import java.io.Serializable;

/**
 * 数据迁移时两个数据间列比对
 * @author whh
 *
 */
public class OddColumnMsg implements Serializable{

	private Object fcolumnName;
	private String tcolumnName;
	private ColumnType defindMate;
	
//	public final static int 0
	/**
	 * 
	 * @param ColumnType 默认转换规则（新表新列要默认值）
	 * @param fromCName	来自那个字段
	 * @param toCName	转换到那个字段 	
	 */
	public OddColumnMsg(ColumnType defindMate,Object fromCName,String toCName){
		this(fromCName,toCName);
		this.defindMate = defindMate;
	}
	public OddColumnMsg(Object fromCName,String toCName){
		this.fcolumnName = fromCName;
		this.tcolumnName = toCName;
	}
	public Object getFcolumnName() {
		return fcolumnName;
	}
	public void setFcolumnName(Object fcolumnName) {
		this.fcolumnName = fcolumnName;
	}
	public String getTcolumnName() {
		return tcolumnName;
	}
	public void setTcolumnName(String tcolumnName) {
		this.tcolumnName = tcolumnName;
	}
	public ColumnType getDefindMate() {
		return defindMate;
	}
	
}

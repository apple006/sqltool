package com.view.movedata.exp.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.dao.entity.Column;


/**
 * 数据迁移时记录对比信息
 * @author wanghh
 *
 */
public class MoveDataMsg implements  Serializable{

	public final static int FROM_DATA_RANDOM = 0;
	public final static int FROM_DATA_TABLE = 1;
	public final static int FROM_DATA_SQL = 2;
	private Object value1;
	private Object value2;
	private Object value3;
	private Object value4;
	private Object value5;

	private boolean isRun = true;
	private int rowCount;
	private Object selectTableName;
	private Object insertTableName;
	private int type;
	private int threads = 3;
	private ArrayList<OddColumnMsg> pp;
	
	public MoveDataMsg(int type,int rowCount){
		pp = new ArrayList<OddColumnMsg>();
		this.rowCount = rowCount;
	}
	/**
	 * 默认type 为导出对象为 Table
	 */
	public MoveDataMsg(){
		pp = new ArrayList<OddColumnMsg>();
		type = FROM_DATA_TABLE;
	}
	
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public OddColumnMsg[] getOddColumns(){
		return pp.toArray(new OddColumnMsg[0]);
	}

	public Object getSelectTableName() {
		return selectTableName;
	}

	public void setSelectTableName(Object selectTableName) {
		this.selectTableName = selectTableName;
	}

	public Object getInsertTableName() {
		return insertTableName;
	}

	public void setInsertTableName(Object isnertTableName) {
		this.insertTableName = isnertTableName;
	}

	public ArrayList<OddColumnMsg> getPp() {
		return pp;
	}

	public void setOdd(ArrayList<OddColumnMsg> pp) {
		this.pp = pp;
	}

	public void setFromColumns(ArrayList<Column> fcolumns) {
		
	}
	
	@Override
	public String toString() {
		return "对比信息";
	}
	public int getThreads() {
		return threads;
	}
	public void setThreads(int threads) {
		this.threads = threads;
	}
	public boolean isRun() {
		return isRun;
	}
	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	public Object getValue1() {
		return value1;
	}
	public void setValue1(Object value1) {
		this.value1 = value1;
	}
	public Object getValue2() {
		return value2;
	}
	public void setValue2(Object value2) {
		this.value2 = value2;
	}
	public Object getValue3() {
		return value3;
	}
	public void setValue3(Object value3) {
		this.value3 = value3;
	}
	public Object getValue4() {
		return value4;
	}
	public void setValue4(Object value4) {
		this.value4 = value4;
	}
	public Object getValue5() {
		return value5;
	}
	public void setValue5(Object value5) {
		this.value5 = value5;
	}
	

}

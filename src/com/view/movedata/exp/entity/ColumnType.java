package com.view.movedata.exp.entity;

import java.io.Serializable;

/**
 * 数据迁移或者压力测试时使用的构造类型
 * @author wanghh
 *
 */
public class ColumnType implements Serializable{
	public static final String DEFULTCOLUMN = "默认字段";
	public static final String FINAL = "常量值";
	public static final String GROUPCOLUMN = "增长字段";
	public static final String NOW_TIME = "当前时间";
	public static final String TEST = "随机值";
	public static final String CUSTOM_MADE = "订制数据来源";
	public static final String ALL_CHANGE = "全局变量设定";
	public static final String FORCE_WHERE = "强制条件";
	public static final String CHANGE_WHERE = "强制增量条件";
	public static final String DEFULT_WHERE = "常量绑定";

	private String type;
	public ColumnType(String type){
		this.type = type;
	}

	public String getType(){
		return this.type;
	}
	@Override
	public String toString() {
		return type;
	}
}

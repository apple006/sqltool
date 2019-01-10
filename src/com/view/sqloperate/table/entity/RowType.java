package com.view.sqloperate.table.entity;
public class  RowType{
	private Object o;
	private int type ;
	public static int TABLESTRUCTURE = 0;
	public static int TABLEDATA = 1;
	public static int VIEW = 2;
	public static int PROCEDURES = 3;
	public static int FUNCTIONS = 4;
	public static int INDEXS = 5;

	public RowType(int type,Object o){
		this.type = type;
		this.o = o;
	}
	
	public int getType(){
		return type;
	}
	public Object getObject(){
		return o;
	}
}
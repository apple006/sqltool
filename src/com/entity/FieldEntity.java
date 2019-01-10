package com.entity;

public class FieldEntity {
	public  final static String FORMAT = "日期格式化";
	public  final static String TOBEIGDATA = "转存大对象数据";
	public  final static String DATATODATA = "数据替换";
	private String expField;
	private String impField;
	private String opertaion;//操作方式
	private String aim;//操作目标
	
	public FieldEntity(String expField, String impField,String opertaion,String aim){
		this.expField = expField;
		this.impField = impField;
		this.opertaion = opertaion;
		this.aim = aim;
	}
	/**
	 * 如果是路径的化
	 * @return 返回 D:/...
	 */
	public String getPath(){
		return aim.substring(0, aim.lastIndexOf("/"))+"/";
	}
	public String getName(){
		return aim.substring(aim.indexOf("/")+1);
	}
	public String getExpField() {
		return expField;
	}
	public void setExpField(String expField) {
		this.expField = expField;
	}
	public String getImpField() {
		return impField;
	}
	public void setImpField(String impField) {
		this.impField = impField;
	}
	public String getOpertaion() {
		return opertaion;
	}
	public void setOpertaion(String opertaion) {
		this.opertaion = opertaion;
	}
	public String getAim() {
		return aim;
	}
	public void setAim(String aim) {
		this.aim = aim;
	}
	
	
}

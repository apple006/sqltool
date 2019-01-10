package com.excel;

public interface IExcelCheckAndPack {
	String success = "success";
	/**
	 * Excel 校验数据
	 * @param row 当前行数据
	 * @return    检验后提示信息，如果校验成功请放回success
	 * @throws Exception 
	 */
	public String check(RowEntity row) throws CheckException;
	/**
	 * Excel 将当前行信息转换为指定VO对象
	 * @param readExcelTitle 
	 * @param row 当前行数据
	 * @return
	 */
	public Object pack( RowEntity row);
}

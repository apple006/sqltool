package com.sql;

import com.dao.entity.Table;

public interface ISQLMachine {

	final String INSERT=" INSERT ";
	final Object INTO = " INTO ";
	/**
	 * 拼接INSERT语句
	 * @param tableName 表名
	 * @param colum     列名
	 * @param value     值
	 * @return
	 */
	String[] getSqls(Table updateData);
	String getTableName(String sql);
}

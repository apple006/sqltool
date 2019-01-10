package com.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.dao.entity.Table;
import com.entity.LoginInfo;

public interface DBManager {
	
	/**
	 * 将结果即放入登录树中
	 * @param resultSet 结果集
	 * @param info      user信息
	 * @throws SQLException
	 */
	void tableResultPutDBTree(ArrayList<Table> listTable, LoginInfo info)
			throws SQLException; 
}

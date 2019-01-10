package com.dao.entity;

import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.SQLTool;

import com.dao.imp.Page;
import com.entity.LoginInfo;
import com.ui.extensible.UITabbedPane;
import com.view.sqloperate.Cache;
import com.view.sqloperate.QuerySqlTab;

/**
 * sql语句执行后结果集收集，分发的管理者
 * 
 * @author Administrator
 * 
 */
public class ResultManager {
	private static ResultManager manager;
	private QuerySqlTab editSqlTab;
	private static Object o = new Object();
	private ResultManager() {getSqlEdit();};
	public static  ResultManager getResultManager(){
		if(manager ==null){
			synchronized (o) {
				if(manager==null){
					manager = new ResultManager();
				}
			}
		}
		return manager;
	}
	
	/**
	 * 
	 * @param editSqlTab 
	 * 			sql语句查询类句柄
	 * @param resultSet
	 * 			查询数据返回结果集
	 * @param sql
	 * 			存留翻页使用的sql语句
	 * @param error
	 * 			如果查询出错则将错误信息设置回前台界面
	 * @param isSuccess
	 * 			是否为成功
	 * @throws SQLException
	 */
	public void setResultData(QuerySqlTab editSqlTab, Table resultSet,String error, boolean isSuccess) {
		editSqlTab.setResultInfo(resultSet,error,isSuccess);
	}
	public void getSqlEdit(){
		UITabbedPane tabQuerySql = SQLTool.getSQLTool().getToolFrame().getSqlView().getTabQuerySql();
		Component selectedComponent = tabQuerySql.getSelectedComponent();
		editSqlTab = (QuerySqlTab)selectedComponent;
	}
	/**
	 * 设置执行sql语句后影响的行数
	 * @param editSqlTab 
	 * @param rows
	 * @param error  执行后错误提醒信息
	 * @param isSuccess 执行是否出错
	 */
//	public void setResultInfluenceRows(QuerySqlTab editSqlTab, int[] rows, String error,boolean isSuccess) {
//		editSqlTab.setResultInfluenceRows(rows,error,isSuccess);
//	}
	public void setResultPks(LoginInfo connInfo,
			HashMap<String, ArrayList<Column>> pks, String error,
			boolean success) {
		Cache.getCache().loadPks(connInfo,pks);
	}
}

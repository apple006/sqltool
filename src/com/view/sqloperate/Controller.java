package com.view.sqloperate;

import java.awt.Component;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dao.ConnectionPool;
import com.dao.Dao;
import com.dao.ManagerThreadPool;
import com.dao.entity.Column;
import com.dao.entity.OracleDBManager;
import com.dao.entity.ResultManager;
import com.dao.entity.Table;
import com.dao.imp.Page;
import com.entity.LoginInfo;
import com.sql.OracleSQLMachinFacto;
import com.ui.extensible.UITabbedPane;
import com.view.movedata.exp.entity.MoveDataMsg;
import com.view.sqloperate.execut.DBExecutColumns;
import com.view.sqloperate.execut.DBExecutColumnsFromSql;
import com.view.sqloperate.execut.DBExecutKeysRedis;
import com.view.sqloperate.execut.DBExecutPKS;
import com.view.sqloperate.execut.DBExecutSequence;
import com.view.sqloperate.execut.DBExecutTableSpaces;
import com.view.sqloperate.execut.DBExecutTables;
import com.view.sqloperate.execut.DBExecutViews;
import com.view.sqloperate.execut.DBSelectSql;
import com.view.sqloperate.execut.DBTableDDL;
import com.view.sqloperate.execut.DBTableSpaceDDL;
import com.view.sqloperate.execut.DBViewDDL;
import com.view.sqloperate.execut.DML;
import com.view.sqloperate.execut.DropDML;
import com.view.sqloperate.execut.ExecutSql;
import com.view.sqloperate.execut.ExecutSqlConfig;
import com.view.sqloperate.execut.ExecutePreparedUpdata;
import com.view.sqloperate.execut.SqlAnalyzer;
import com.view.sqloperate.execut.TablesRow;
import com.view.sqloperate.execut.TablesRowFromSql;
import com.view.system.dialog.WaringMsg;

import main.SQLTool;

public class Controller {

	private static final int THREADS_NUM = 10;
	private static Object o = new Object();
	private static Controller controller;
	private ManagerThreadPool pools = ManagerThreadPool.getManagerThreadPool();
	private ExecutorService threads;
	private SqlAnalyzer analyzer = new SqlAnalyzer();

	private Controller() {
		threads = Executors.newFixedThreadPool(THREADS_NUM);
	}

	public synchronized void loginIn(final LoginInfo loginIfo)
			throws MalformedURLException, ClassNotFoundException, SQLException {
		threads.execute(new Runnable() {
			@Override
			public void run() {
				try {
					pools.createThreadPool(loginIfo);
					loginIfo.setIsLive(ConnectionPool.IS_LIVE);

					UITabbedPane tabQuerySql = SQLTool.getSQLTool().getToolFrame().getSqlView().getTabQuerySql();
					for (int i = 0; i < tabQuerySql.getTabCount(); i++) {
						tabQuerySql.getTitleAt(i);
						Component component = tabQuerySql.getComponentAt(i);
						if (component instanceof QuerySqlTab) {
							QuerySqlTab tab = (QuerySqlTab) component;
							tab.referDrowDown();
						}
						// component
					}
				} catch (Exception e) {
					loginIfo.setIsLive(ConnectionPool.IS_DIE);
					loginIfo.setMessage(e.getMessage());
					e.printStackTrace();
				}

			}

		});
	}

	/**
	 * 单例Controller构造
	 * 
	 * @return Controller单例
	 */
	public static Controller newController() {
		if (controller == null) {
			synchronized (o) {
				controller = new Controller();
			}
		}
		return controller;
	}

	/**
	 * 
	 * @param connInfo
	 *            登录信息
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void getTables(LoginInfo connInfo) throws InterruptedException, SQLException {
		DBExecutTables dbExecutTables = new DBExecutTables(connInfo);
		threads.execute(dbExecutTables);
		synchronized (dbExecutTables) {
			dbExecutTables.wait();
			OracleDBManager.getOracleDBManager().tableResultPutDBTree(dbExecutTables.getTables(), connInfo);
		}
	}

	public ArrayList<Table> getResTables(LoginInfo connInfo) throws InterruptedException, SQLException {
		DBExecutTables dbExecutTables = new DBExecutTables(connInfo);
		threads.execute(dbExecutTables);
		synchronized (dbExecutTables) {
			dbExecutTables.wait();
			return dbExecutTables.getTables();
		}
	}
	public ArrayList<Table> getResSequence(LoginInfo connInfo) throws InterruptedException, SQLException {
		DBExecutSequence dbExecutTables = new DBExecutSequence(connInfo);
		threads.execute(dbExecutTables);
		synchronized (dbExecutTables) {
			dbExecutTables.wait();
			return dbExecutTables.getTables();
		}
	}
	/**
	 * 
	 * @param connInfo
	 *            登录信息
	 * @return
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public ArrayList<Column> getColumns(LoginInfo connInfo, String tableName)
			throws InterruptedException, SQLException {
		DBExecutColumns dbExecutColumns = new DBExecutColumns(connInfo, tableName);
		threads.execute(dbExecutColumns);
		synchronized (dbExecutColumns) {
			dbExecutColumns.wait();
			return dbExecutColumns.getColumns();
			// OracleDBManager.getOracleDBManager().tableResultColums(dbExecutColumns.getColumns(),
			// connInfo);
		}
	}

	/**
	 * 
	 * @param connInfo
	 *            登录信息
	 * @return
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public ArrayList<Column> getColumnsFromSql(LoginInfo connInfo, String sql)
			throws InterruptedException, SQLException {
		DBExecutColumnsFromSql dbExecutColumns = new DBExecutColumnsFromSql(connInfo, sql);
		threads.execute(dbExecutColumns);
		synchronized (dbExecutColumns) {
			dbExecutColumns.wait();
			return dbExecutColumns.getColumns();
			// OracleDBManager.getOracleDBManager().tableResultColums(dbExecutColumns.getColumns(),
			// connInfo);
		}
	}

	/**
	 * 执行一般查单据询语句
	 * 
	 * @param connInfo
	 *            登录信息
	 * @param editSqlTab
	 *            结果集处理类
	 * @param sql
	 *            要sql语句
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void executNextPage(LoginInfo connInfo, QuerySqlTab editSqlTab, Page page)
			throws SQLException, InterruptedException {
		// ExecutNextPage executing = new ExecutNextPage(connInfo, page);
		// threads.execute(executing);
		// synchronized (executing) {
		// executing.wait();
		// ResultManager.getResultManager().setResultData(editSqlTab,executing.getResultSet(),sql,executing.getError(),executing.isSuccess());
		//
		// }
	}

	/**
	 * 
	 * @param connInfo
	 *            登录信息
	 * @param editSqlTab
	 *            编辑语句tab
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void executQuerySqlTab(LoginInfo connInfo, QuerySqlTab editSqlTab)
			throws InterruptedException, SQLException {
		String str = editSqlTab.getEditSqlText().getSelectedText();
		if (str == null) {
			str = editSqlTab.getEditSqlText().getText();
		}
		if (str == null) {
			str = "";
		}
		
//		queryColumnsForSql(vo.getSql());(\\w+)?,?\\s*(\\w+)\\s*
		
		String userName = connInfo.getUserName();
		String[] split3 = userName.split("#");
		if (Dao.REDIS.equals(connInfo.getDataType())||Dao.REDISSENTINELPOOL.equals(connInfo.getDataType())) {
			String[] sqls = analyzer.converter(str);
			for (int i = 0; i < sqls.length; i++) {
				if (sqls[i].length() == 0) {
					continue;
				}
				DBExecutKeysRedis executeSelect = new DBExecutKeysRedis(connInfo, sqls[i]);
				threads.execute(executeSelect);
				synchronized (executeSelect) {
					executeSelect.wait();
					if (executeSelect.isSuccess()) {
						Table table = executeSelect.getTable();
						table.setTableCode(analyzer.getTableName(sqls[i]));
						// table.setStartTime(startTime);
						ResultManager.getResultManager().setResultData(editSqlTab, table, executeSelect.getError(),
								executeSelect.isSuccess());
					} else {
						ResultManager.getResultManager().setResultData(editSqlTab, null, executeSelect.getError(),
								executeSelect.isSuccess());
					}
				}
			}
		} else {
			if (!"".equals(str)) {
				String[] sqls = analyzer.converter(str);
				for (int i = 0; i < sqls.length; i++) {
					if (sqls[i].length() == 0) {
						continue;
					}
					if(split3.length>1) {
						
						String columsAliasRegEx1 =  "(from|into)([\\s*\\w*,)]*)(?:where|left|start|on|$|;)";
						Pattern columsAliaspattern = Pattern.compile(columsAliasRegEx1,Pattern.CASE_INSENSITIVE);
						Matcher columsAliasmatcher = columsAliaspattern.matcher(str);
						HashSet<String> tableName = new HashSet<>();
						while (columsAliasmatcher.find()) {
							String group = columsAliasmatcher.group(2);
							String[] split = group.trim().split(",");
							for (int j = 0; j < split.length; j++) {
								String[] split2 = split[j].trim().split("\\s+");
//								if(split2.length==2) {
//								columsAliasMap.put(split2[1].trim().replaceAll("\\w*\\.", ""), split2[0].trim().replaceAll("\\w*\\.", ""));
									tableName.add( split2[0].trim().replaceAll("\\w*\\.", ""));
//								}else {
//									tableName.add( split2[0].trim().replaceAll("\\w*\\.", ""));

//								}
							}
						}
						String[] array = tableName.toArray(new String[0]);
						for (int j = 0; j < array.length; j++) {
							sqls[i] = sqls[i].replaceAll(array[j], split3[1]+"."+array[j]);
						}
					}
					System.out.println(sqls[i]);

					ExecutSql executeSelect = new ExecutSql(connInfo, sqls[i]);
					threads.execute(executeSelect);
					synchronized (executeSelect) {
						try {
							executeSelect.wait();
							if (executeSelect.isSuccess()) {
								Table table = executeSelect.getTable();
								table.setTableCode(analyzer.getTableName(sqls[i]));
								ResultManager.getResultManager().setResultData(editSqlTab, table,
										executeSelect.getError(), executeSelect.isSuccess());
							} else {
								ResultManager.getResultManager().setResultData(editSqlTab, null,
										executeSelect.getError(), executeSelect.isSuccess());
							}
						} catch (InterruptedException e) {
							ResultManager.getResultManager().setResultData(editSqlTab, null, "系统错误：" + e.getMessage(),
									executeSelect.isSuccess());
						}
					}
				}
			}
		}

	}

	public Table executSql(LoginInfo connInfo, String sql) throws InterruptedException, SQLException {
		ExecutSql executeSelect = new ExecutSql(connInfo, sql);
		threads.execute(executeSelect);
		synchronized (executeSelect) {
			executeSelect.wait();
			Table table = executeSelect.getTable();
			table.setTableCode(analyzer.getTableName(sql));
			return table;
		}
	}

	public void executPreparedUpdate(LoginInfo connInfo, QuerySqlTab editSqlTab, Table updateData)
			throws InterruptedException, SQLException {
		OracleSQLMachinFacto oralcSql = new OracleSQLMachinFacto();
		String[] sqls = oralcSql.getSqls(updateData);
		ExecutePreparedUpdata executing = new ExecutePreparedUpdata(connInfo, sqls, updateData);
		threads.execute(executing);
		synchronized (executing) {
			executing.wait();
			if (executing.isSuccess()) {
				Table table = executing.getTable();
				table.setResult_type(Table.RESULT_UPDATA);
				ResultManager.getResultManager().setResultData(editSqlTab, table, executing.getError(),
						executing.isSuccess());

			} else {
				ResultManager.getResultManager().setResultData(editSqlTab, null, executing.getError(),
						executing.isSuccess());
			}

		}
	}

	public void getPks(LoginInfo connInfo) throws InterruptedException {
		DBExecutPKS pk = new DBExecutPKS(connInfo);
		threads.execute(pk);
		synchronized (pk) {
			pk.wait();
			ResultManager.getResultManager().setResultPks(connInfo, pk.getPks(), pk.getError(), pk.isSuccess());
		}
	}

	public void getViews(LoginInfo info) throws InterruptedException {
		DBExecutViews dbExecutViews = new DBExecutViews(info);
		threads.execute(dbExecutViews);
		synchronized (dbExecutViews) {
			dbExecutViews.wait();
			OracleDBManager.getOracleDBManager().viewResultPutDBTree(dbExecutViews.getViews(), info);
		}
	}

	public void getViewDDL(String viewName, LoginInfo info) throws InterruptedException {
		DBViewDDL ddlview = new DBViewDDL(info, viewName);
		threads.execute(ddlview);
		synchronized (ddlview) {
			ddlview.wait();
			OracleDBManager.getOracleDBManager().viewDDLResult(ddlview.getViewDDL(), info);
		}
	}

	public void getTableDDL(String tableName, LoginInfo info) throws InterruptedException {
		DBTableDDL ddlview = new DBTableDDL(info, tableName);
		threads.execute(ddlview);
		synchronized (ddlview) {
			ddlview.wait();
			OracleDBManager.getOracleDBManager().viewDDLResult(ddlview.getTableDDL(), info);
		}
	}

	public void getSelectSql(LoginInfo info, String tableName) throws InterruptedException {
		DBSelectSql selectSql = new DBSelectSql(info, tableName);
		threads.execute(selectSql);
		synchronized (selectSql) {
			selectSql.wait();
			OracleDBManager.getOracleDBManager().viewDDLResult(selectSql.getSelectSql(), info);
		}
	}

	public void getTableSpaces(LoginInfo info) throws InterruptedException {
		DBExecutTableSpaces tableSpace = new DBExecutTableSpaces(info);
		threads.execute(tableSpace);
		synchronized (tableSpace) {
			tableSpace.wait();
			OracleDBManager.getOracleDBManager().tableSpacesResult(tableSpace.getTableSpaces(), info);
		}
	}

	public void getTableSpaceDDL(String name, LoginInfo info) throws InterruptedException {

		DBTableSpaceDDL tableSpaceDdl = new DBTableSpaceDDL(name, info);
		threads.execute(tableSpaceDdl);
		synchronized (tableSpaceDdl) {
			tableSpaceDdl.wait();
			OracleDBManager.getOracleDBManager().tableSpaceDDLResult(tableSpaceDdl.getTableSpaceDDL(), info);
		}
	}

	/**
	 * 
	 * @param node
	 *            要删除的名字 如表名
	 * @param info
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void dropTables(String[] node, LoginInfo info) throws InterruptedException, SQLException {
		DropDML tableSpaceDdl = new DropDML(info, node, DML.TABLE);
		threads.execute(tableSpaceDdl);
		synchronized (tableSpaceDdl) {
			tableSpaceDdl.wait();
			OracleDBManager.getOracleDBManager().refresh(info);
		}
	}

	/**
	 * 
	 * @param info
	 * @throws InterruptedException
	 */
	public int getTablesRow(LoginInfo info, String tableName, boolean count) throws InterruptedException {
		if (count) {
			TablesRow tanleRow = new TablesRow(info, tableName);
			threads.execute(tanleRow);
			synchronized (tanleRow) {
				tanleRow.wait();
				return tanleRow.getRowCount();
			}
		}
		return 0;
	}

	public int getTablesRowFromSql(LoginInfo info, String sql) throws InterruptedException {
		TablesRowFromSql tanleRow = new TablesRowFromSql(info, sql);
		threads.execute(tanleRow);
		synchronized (tanleRow) {
			tanleRow.wait();
			if (tanleRow.isSuccess()) {
				return tanleRow.getRowCount();
			} else {
				WaringMsg.showMessageDialog(tanleRow.getError());
			}
			return 0;
		}
	}

	public void getOdd(LoginInfo flogin, LoginInfo tlogin, String fTable, String tTable)
			throws InterruptedException, SQLException {
		MoveDataMsg msg = new MoveDataMsg();
		msg.setSelectTableName(fTable);
		msg.setInsertTableName(tTable);

		ArrayList<Column> fcolumns = getColumns(flogin, fTable);
		ArrayList<Column> tcolumns = getColumns(tlogin, tTable);
		msg.setFromColumns(fcolumns);
		// msg.setToColumns(tcolumns);

	}

	public Table ExecutSqlConfig(LoginInfo connInfo, String name) throws InterruptedException {

		ExecutSqlConfig executeSelect = new ExecutSqlConfig(connInfo, name);
		threads.execute(executeSelect);
		synchronized (executeSelect) {
			executeSelect.wait();
			Table table = executeSelect.getTable();
			return table;
		}
	}


}

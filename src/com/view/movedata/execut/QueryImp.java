package com.view.movedata.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.view.movedata.IQuery;
import com.view.sqloperate.execut.Execute;
import com.view.tool.HelpProperties;
import com.view.tool.HelpRslutToSql;

public class QueryImp extends Execute implements IQuery{
		/**
		 * 每次从数据库中取出数据大小
		 */
		private int fetchSize = 5000;
		/**
		 * 块缓存数据大小
		 */
		private int cathSize = 1000;
		/**
		 * 缓存多少条块数据
		 */
		private int count = 500;
		private ExpDataToFileCtr ctr;
		public QueryImp(ExpDataToFileCtr ctr){
			super(ctr.getLoginfo());
			fetchSize = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "fetchSize"));
			cathSize = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "cathSize"));
			count =  new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "count")); 
			this.ctr = ctr;
		}
		
		@Override
		public void run() {
			Connection connection = null;
			Statement createStatement = null;
			ResultSet executeQuery = null;
			ResultSetMetaData metaData = null;
			try {

				connection = getConnection();
				
				createStatement = dao.getLazyStatement(connection);	
				executeQuery = createStatement.executeQuery("SELECT * FROM "+ctr.getTableName());
				metaData = executeQuery.getMetaData();
				int columnCount = metaData.getColumnCount();
				String startInsert = HelpRslutToSql.getStartInsert(metaData, ctr.getTableName(),columnCount);
				
				StringBuffer strBuf = new StringBuffer();
				int countRow=0;
				while(executeQuery.next()){
					strBuf.append(startInsert);

					HelpRslutToSql.getEndInsert(executeQuery, strBuf, columnCount);
					ctr.setSpeed();
					countRow++;
					if(countRow>=cathSize){
						countRow=0;
						ctr.getCath().push(strBuf.toString().trim());
						strBuf.setLength(0);
						synchronized (ctr.getCath()) {
							ctr.getCath().notify();
						}
					}

					if(ctr.getCath().size()>count){
						synchronized (ctr.getCath()) {
							ctr.getCath().wait();
						}
					}
				}
				ctr.getCath().push(strBuf.toString());
				
//				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		
			finally{
					releasConnection(connection);
					closeAll(executeQuery,metaData,createStatement);
					synchronized (ctr.getCath()) {
						ctr.getCath().notify();
					}
			}
		}


		
		
		public static void closeAll(Object... objs) {
			for (Object obj : objs) {
				try {
					if (obj instanceof ResultSet)
						close((ResultSet) obj);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (obj instanceof Statement)
						close((Statement) obj);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				try {
					if (obj instanceof PreparedStatement)
						close((PreparedStatement) obj);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		public static void close(Statement statent) throws SQLException {
			if (statent != null) {
				statent.close();
			}
		}

		private static void close(ResultSet result) throws SQLException {
			if (result != null) {
				result.close();
			}
		}

		private static void close(Connection conn) throws SQLException {
			if (conn != null) {
				conn.close();
			}
		}
		
}

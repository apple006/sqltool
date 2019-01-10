package com.view.movedata.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.view.movedata.IQuery;
import com.view.movedata.exp.entity.SubmitDataGroup;
import com.view.sqloperate.execut.Execute;
import com.view.tool.HelpProperties;
import com.view.tool.HelpRslutToSql;

public class MoveQueryDataImp extends Execute implements IQuery{
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
		private MoveDataCtr ctr;
		public MoveQueryDataImp(MoveDataCtr ctr){
			super(ctr.getFormLoginfo());
			fetchSize = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "fetchSize"));
			cathSize = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "cathSize"));
			this.ctr = ctr;
		}
		
		@Override
		public void run() {
			Connection connection = null;
			Statement createStatement = null;
			ResultSet executeQuery = null;
			ResultSetMetaData metaData = null;
			try {
				System.out.println("准备Connection");

				connection = getConnection();
				System.out.println("得到Conneqction");
				
				ctr.setStartDate();
				createStatement = dao.getLazyStatement(connection);	
				executeQuery = createStatement.executeQuery(HelpRslutToSql.getSelect(ctr));
				metaData = executeQuery.getMetaData();
				int columnCount = metaData.getColumnCount();
				int type[] = new int[columnCount];
				for (int i = 0; i < columnCount; i++) {
					type[i] = metaData.getColumnType(i+1);
				}
				SubmitDataGroup group = new SubmitDataGroup(type);
				int countRow=0;
//				long currentTimeMillis = System.currentTimeMillis();
				while(executeQuery.next()){

					if(!ctr.getMoveDataMsg().isRun()){
						break;
					}
					group.addRow(HelpRslutToSql.getArrayValue(executeQuery,ctr.getMoveDataMsg()));
//					com.view.movedata.exp.entity.DataCache@1515a03
					countRow++;
					if(countRow>=cathSize){
						countRow=0;
						ctr.getCath().push(group);
//						System.out.println("query"+group.getSize());
						group = new SubmitDataGroup(type);
//						long currentTimeMillis1 = System.currentTimeMillis();
//						System.out.println(currentTimeMillis1-currentTimeMillis);
//						currentTimeMillis = System.currentTimeMillis();
					}
				}
				ctr.getCath().push(group);
				System.out.println("读取完成");
				
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
				SubmitDataGroup group = new SubmitDataGroup(null);
				ctr.getCath().push(group);
				closeAll(executeQuery,metaData,createStatement);
				releasConnection(connection);
			}
		}


		
		
}

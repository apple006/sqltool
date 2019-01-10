package com.view.movedata.execut;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.entity.LoginInfo;
import com.view.movedata.ISaveData;
import com.view.movedata.exp.AsynchronousBathLoadDataBase;
import com.view.movedata.exp.entity.DataFromBean;
import com.view.movedata.exp.entity.SubmitDataGroup;
import com.view.sqloperate.execut.Execute;
import com.view.tool.HelpRslutToSql;



public class MoveScript extends Execute implements ISaveData{
	Logger logger = Logger.getLogger(getClass());
	FileWriter wirte = null;

	private MoveDataCtr ctr;
	public MoveScript(MoveDataCtr ctr){
		super(ctr.getToLoginfo());
		this.ctr = ctr;
	}
	
	@Override
	public void run() {
		try {
			int threads = ctr.getMoveDataMsg().getThreads();
			String sql = HelpRslutToSql.getScriptSql(ctr.getMoveDataMsg());
			ArrayBlockingQueue<Integer>  connections=new  ArrayBlockingQueue<Integer>(threads);
			String fialPath = System.getProperty("user.dir");	
			Object insertTableName = ctr.getMoveDataMsg().getInsertTableName();
			File file= new File( fialPath+"\\logs\\"+insertTableName+".log");
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdir();
			}
			file.createNewFile();
			wirte = new FileWriter(file);
			wirte.write("执行语句:\r\n");
			wirte.write(sql);
			wirte.write("\r\n");
			wirte.write("执行失败信息如下\r\n");
			wirte.flush();
			final String[] split = sql.split(";");
			for (int i = 0; i < split.length; i++) {
				
				try {
					connections.put(1);
					AsynchronousBathLoadDataBase.getAsynchronousBathLoadDataBase().submit(new E(split[i],connections));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while(connections.size()!=0){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			wirte.write("执行完成");
			wirte.flush();
			ctr.setEndDate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			ctr.getMoveDataMsg().setRun(false);
			if(wirte!=null){
				try {
					wirte.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class E implements Callable{
		
		private String sql;
		private ArrayBlockingQueue<Integer> connections;
		public E(String sql,ArrayBlockingQueue<Integer> connections){
			this.sql = sql;
			this.connections =connections;

		}
		@Override
		public String call() throws Exception {
				// TODO Auto-generated method stub
				long currentTimeMillis = System.currentTimeMillis();
				Connection connection = null;
				Statement statement = null;
				try {
					connection = getConnection();
					statement = connection.createStatement();
					int executeUpdate = statement.executeUpdate(sql);
					long currentTimeMillis1 = System.currentTimeMillis();
					connection.commit();
					ctr.setSpeed(1);
					currentTimeMillis1 = System.currentTimeMillis();
					System.out.println(Thread.currentThread()+" "+ ctr.getMoveDataMsg().getInsertTableName()+"===条数据提交时间"+(currentTimeMillis1-currentTimeMillis)); 
				} catch (Exception e) {
					e.printStackTrace();
					wirte.write(e.getMessage());
					wirte.write("\r\n");
					wirte.flush();
				}
				finally {
					connections.poll();
					closeAll(statement);
					releasConnection(connection);
				}
		
				return "";
		}
	}
}

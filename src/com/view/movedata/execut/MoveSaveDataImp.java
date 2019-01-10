package com.view.movedata.execut;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import com.entity.LoginInfo;
import com.view.movedata.ISaveData;
import com.view.movedata.exp.AsynchronousBathLoadDataBase;
import com.view.movedata.exp.entity.SubmitDataGroup;
import com.view.sqloperate.execut.Execute;
import com.view.tool.HelpRslutToSql;



public class MoveSaveDataImp extends Execute implements ISaveData{
	Logger logger = Logger.getLogger(getClass());
	FileWriter wirte = null;

	public MoveSaveDataImp(LoginInfo loginfo) {
		super(loginfo);
	}
	private MoveDataCtr ctr;
	public MoveSaveDataImp(MoveDataCtr ctr){
		super(ctr.getToLoginfo());
		this.ctr = ctr;
	}
	
//	@Override
//	public void saveData() {
//	}
	@Override
	public void run() {
		int threads = ctr.getMoveDataMsg().getThreads();
		ArrayBlockingQueue<Integer>  connections=new  ArrayBlockingQueue<Integer>(threads);
		String fialPath = System.getProperty("user.dir");	
		Object insertTableName = ctr.getMoveDataMsg().getInsertTableName();
		File file= new File( fialPath+"\\logs\\"+insertTableName+".log");
		try {
		
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdir();
			}
			file.createNewFile();
			wirte = new FileWriter(file);
			wirte.write("执行语句:\r\n");
			wirte.write(HelpRslutToSql.getInsertPrepare(ctr.getMoveDataMsg()));
			wirte.write("\r\n");
			wirte.write("执行失败信息如下\r\n");
			wirte.flush();
			while(ctr.getMoveDataMsg().isRun()){
				SubmitDataGroup pop = ctr.getCath().pop();
				int colSize= ctr.getMoveDataMsg().getOddColumns().length;
				int size = pop.getSize();
	
				if(size==0){
					break;
				}
				Runnable th = new Runnable() {
					
					@Override
					public void run() {
						int[] executeBatch = null ;
//						long currentTimeMillis = System.currentTimeMillis();
						Connection connection = null;
						PreparedStatement prepareStatement = null;
						Object insertTableName2 = ctr.getMoveDataMsg().getInsertTableName();
						long currentTimeMillis1 =0;
						try {
							connection = getConnection();
	//						connection.setAutoCommit(false);
//							System.out.println(HelpRslutToSql.getInsertPrepare(ctr.getMoveDataMsg()).replaceAll(",DESC,", ",\"DESC\",").replaceAll(",TYPE,", ",\"TYPE\","));

							prepareStatement = connection.prepareStatement(HelpRslutToSql.getInsertPrepare(ctr.getMoveDataMsg()).replaceAll(",DESC,", ",\"DESC\",").replaceAll(",TYPE,", ",\"TYPE\","));
							for (int i = 0; i < size; i++) {
								for (int j = 1; j <=colSize; j++) {
									Object[] arr = null;
									Object object = pop.getRow(i)[j-1];
									if(object instanceof Object[]){
										arr = (Object[])object;
										if (Types.CLOB == (int)arr[0]) {
											if(arr[1]==null){
												Clob b= null;
												prepareStatement.setClob(j,b);
												continue;
											}
											Clob clob = connection.createClob();  
											clob.setString(1, arr[1].toString());
											prepareStatement.setClob(j,clob);
										}else if(Types.BLOB == (int)arr[0]){
											if(arr[1]==null){
												Blob blob = connection.createBlob();  
												blob.setBytes(1, arr[1].toString().getBytes());
												prepareStatement.setBlob(j,blob);
												continue;
											}
											Blob clob=new javax.sql.rowset.serial.SerialBlob(arr[1].toString().getBytes());  
											prepareStatement.setBlob(j,clob);
										}
									}else{
										prepareStatement.setObject(j,pop.getRow(i)[j-1]);
									}
								} 
								prepareStatement.addBatch();
							}
							executeBatch = prepareStatement.executeBatch();
							String info ="";
//							for (int i = 0; i < size; i++) {
//								if(executeBatch[i]<=0&&executeBatch[i]!=-2){
//									for (int j = 1; j <=colSize; j++) {
//										if(colSize==j){
//											info+=pop.getRow(i)[j-1];
//										}else{
//											info+=pop.getRow(i)[j-1]+",";
//										}
//	//									System.out.println(ctr.getMoveDataMsg().getOddColumns()[0].getDefindMate()+"  "+pop.getRow(i)[j-1]);
//									}
//									wirte.write(info);
//									wirte.write("\r\n");
//									wirte.flush();
//								}
//							}
							connection.commit();
							prepareStatement.clearBatch();
							prepareStatement.clearParameters();
							prepareStatement.clearWarnings();
							ctr.setSpeed(size);
//							connections.poll();
//							currentTimeMillis1 = System.currentTimeMillis();
						} catch (Exception e) {
							e.printStackTrace();
							String info= "";
							for (int i = 0; i < size; i++) {
									for (int j = 1; j <=colSize; j++) {
										if(colSize==j){
											info+=pop.getRow(i)[j-1];
										}else{
											info+=pop.getRow(i)[j-1]+",";
										}
		//								System.out.println(ctr.getMoveDataMsg().getOddColumns()[0].getDefindMate()+"  "+pop.getRow(i)[j-1]);
									}
									try {
										wirte.write(info);
										wirte.write(" 错误信息 "+e.getMessage());

										wirte.write("\r\n");
										wirte.flush();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
							}
						}
						finally {
							connections.poll();
//							currentTimeMillis1 = System.currentTimeMillis();

//							System.out.println(Thread.currentThread()+" "+ insertTableName2+"==="+size+"条数据提交时间"+(currentTimeMillis1-currentTimeMillis)+"  "+connection); 

							releasConnection(connection);
							closeAll(prepareStatement);
						}
					}
				};
				try {
					connections.put(1);
					AsynchronousBathLoadDataBase.getAsynchronousBathLoadDataBase().submit(th);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while(connections.size()!=0){
				try {
					Thread.sleep(2000);
//					System.out.println("====");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			wirte.write("执行完成");
			wirte.flush();
		} catch (IOException e) {
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
			ctr.setEndDate();
	}
}

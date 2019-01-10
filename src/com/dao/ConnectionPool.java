package com.dao;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongBinaryOperator;

import com.entity.DriverPathInfo;
import com.entity.LoginInfo;
import com.view.system.perferenc.xml.XMLJdbcConfig;
import com.view.tool.HelpProperties;

import main.DriverUtil;

public class ConnectionPool {
	private Hashtable<String, DriverPathInfo> driverPathsArr = XMLJdbcConfig.getXMLJdbcConfig().getDriverPathsArr();

	public static final int IS_LIVE = 4;
	public static final int IS_DIE = 5;
	public static final int DENG = 0;
	//连接池名称
	private String name;
	// 默认最小连接数
	private int minPoolSize;
	// 默认最大连接数
	private int maxPoolSize = 3;
	// 空闲连最小连接数
	private int acquireIncrement = 2;
	// 现有可用连接数
	private AtomicLong currentSize ;
	private LoginInfo connInfo;
	private ArrayBlockingQueue<Connection>  connections;

	/**
	 * 
	 * @param conn 创建连接的ConnectionInfo信息包括 用户名、密码、Url、数据库名称等数据库信息
	 * @param name
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws MalformedURLException 
	 * @throws InterruptedException 
	 * @throws Exception 
	 */
	public  ConnectionPool(LoginInfo connInfo) throws MalformedURLException, ClassNotFoundException, SQLException, InterruptedException {
		this.name = connInfo.getName();
		this.connInfo = connInfo;
		currentSize = new AtomicLong();
		minPoolSize=new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "pool.minPoolSize"));
		maxPoolSize = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "pool.maxPoolSize"));
		connections=new  ArrayBlockingQueue<Connection>(300);

		createConnection(maxPoolSize);
		
	
		
		
	}
	

	/**
	 * 
	 * @param i   创建连接数
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws MalformedURLException 
	 * @throws InterruptedException 
	 * @throws Exception 
	 *            
	 */
	private  void createConnection(int i) throws MalformedURLException, ClassNotFoundException, SQLException, InterruptedException {
		for (int j = 0; j < i; j++) {
				connections.put(connection());
		}
		
		this.connInfo.setIsLive(IS_LIVE);
	}
	private Connection connection() throws MalformedURLException, ClassNotFoundException, SQLException  {
//		System.out.println("加载驱动...");
		DriverPathInfo driverPathInfo = XMLJdbcConfig.getXMLJdbcConfig().getDriverPaths().get(connInfo.getDriverClass()); 
		 DriverUtil.getDynamic(driverPathInfo.getFileUrl());
		Class.forName(driverPathInfo.getDriverClass());
		currentSize.incrementAndGet();
		String url = driverPathsArr.get(driverPathInfo.getName()).getUrlFormat().replaceFirst("ip", connInfo.getIp()).replaceFirst("port", connInfo.getPort()).replaceFirst("example", connInfo.getExample());
		Connection conn = DriverManager.getConnection(url, connInfo.getUserName().split("#")[0], connInfo.getPassword());
//		System.out.println("连接成功...");
		return conn;
	}
	
	/**
	 * 得到该连接池的一个Connection连接
	 * @return Connection
	 * @throws InterruptedException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws MalformedURLException 
	 * @throws Exception 
	 */
	public  Connection getConnection() throws InterruptedException, MalformedURLException, ClassNotFoundException, SQLException   {
		Connection conn = null;
//		currentSize.accumulateAndGet(1, new LongBinaryOperator() {
//			@Override
//			public long applyAsLong(long left, long right) {
//				if (connections.isEmpty()) {
//					if(currentSize.get()<maxPoolSize){
//						try {
//							connections.put(connection());
//						} catch (MalformedURLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (ClassNotFoundException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						return left+right;
//					}
//				}
//				return currentSize.get();
//			}
//		});
		if(connections.size()<minPoolSize){
			createConnection(minPoolSize-connections.size());
		}
		return connections.take();
	}
	
	public void tryQuery(){
		Connection take=null;
			for (int i = 0; i < connections.size(); i++) {
				try {
					take = connections.take();
					System.out.println("try Connection start:"+connections.size());
					DatabaseFactory dataBaseFactory = DatabaseFactory.getDataBaseFactory();
					Dao dao = dataBaseFactory.createDataBaseTemplate(connInfo);
					dao.tryQuery(take);
					System.out.println("try Connection end");
					releasConnection(take);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	public  void releasConnection(Connection conn){
		try {
//			System.out.println("releasConnection"+conn.isClosed()+"  "+connections+ "  "+this.connInfo.getName());
			if(conn!=null&&!conn.isClosed()){
				connections.put(conn);
			}else{
				currentSize.decrementAndGet();
			}
		} catch (InterruptedException | SQLException e) {
			e.printStackTrace();
		}
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public int getAcquireIncrement() {
		return acquireIncrement;
	}

//	public int getCurrentSize() {
//		return currentSize;
//	}


	public LoginInfo getConnInfo() {
		return connInfo;
	}


	public void close() {
		Connection take=null;
		while(currentSize.get()!=0){
			try {
				take = connections.take();
				take.close();
				currentSize.decrementAndGet();
				System.out.println("try Connection close");
			} catch (InterruptedException | SQLException e) {
				e.printStackTrace();
			}

		}

	}
	
	
}

package com.dao;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.entity.LoginInfo;
import com.redis.JedisUtil;

import redis.clients.jedis.Jedis;

/**
 * 线程池管理类，可以管理不同数据库线程池，也可以管理相同数据库不同线程池 单例类
 * 
 * @author wanghhh
 *
 */
public class ManagerThreadPool {
	private TreeMap<String, ConnectionPool> pools = new TreeMap<String, ConnectionPool>();
	private static Object o = new Object();
	private static ManagerThreadPool managerThreadPool = new ManagerThreadPool();
	private TreeMap<String, JedisUtil> reidsPools = new TreeMap<String, JedisUtil>();
	private ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();

	private ManagerThreadPool() {
	newSingleThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						
						Thread.sleep(1000*60*5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Collection<ConnectionPool> values = pools.values();
					ConnectionPool[] pools = values.toArray(new ConnectionPool[0]);
					for (int i = 0; i < pools.length; i++) {
						pools[i].tryQuery();
					}
				}
			}
		});
	}

	/**
	 * 得到单例线程池管理类
	 * 
	 * @return ManagerThreadPool
	 */
	public static ManagerThreadPool getManagerThreadPool() {
		if (managerThreadPool == null) {
			synchronized (o) {
				managerThreadPool = new ManagerThreadPool();
			}
		}
		return managerThreadPool;
	}

	/**
	 * 根据线程池名字和ConnectionInfo类型创建线程池
	 * 
	 * @param poolName
	 *            线程池名
	 * @param info
	 *            ConnectionInfo 信息
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 * @throws InterruptedException
	 */
	public void createThreadPool(LoginInfo info) throws MalformedURLException, ClassNotFoundException, SQLException, InterruptedException {
		if ("redis".equals(info.getDataType())||"redis-sentinelPool".equals(info.getDataType())) {
			JedisUtil redis = new JedisUtil(info);
			reidsPools.put(info.getPoolName(), redis);

		} else {
			ConnectionPool pool = new ConnectionPool(info);
			pools.put(info.getPoolName(), pool);
		}

	}

	public void releasConnection(LoginInfo info, Connection conn) {
		ConnectionPool connectionPool = pools.get(info.getPoolName());
		if(connectionPool!=null){
			connectionPool.releasConnection(conn);
		}
	}

	/**
	 * 根据不同线程池名称得到Connection
	 * 
	 * @param threadPollName
	 *            线程池名字
	 * @return 返回 Connection
	 * @throws InterruptedException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 */
	public Connection getConnection(LoginInfo loginInfo)
			throws InterruptedException, MalformedURLException, ClassNotFoundException, SQLException {
		ConnectionPool conPool = pools.get(loginInfo.getPoolName());
		if(conPool==null){
			return null;
		}
		Connection connection = conPool.getConnection();
//		System.out.println(connection);

		return connection;
	}

	public Jedis getRedisConnection(LoginInfo loginInfo) {
		JedisUtil conPool = reidsPools.get(loginInfo.getPoolName());
		Jedis jedis = conPool.getJedis();
		if("".equals(loginInfo.getPassword())||null==loginInfo.getPassword()){
			
		}else{
//			jedis.auth("a*#sIYn$iP#Rvx&*rEdIs$%^%z"); 
		}
		return jedis;
	}

	public boolean isHave(String poolName) {
		boolean isHavs = false;
		ConnectionPool connectionPool = pools.get(poolName);
		if (connectionPool != null) {
			isHavs = true;
		}
		return isHavs;
	}

	public void close(LoginInfo loginfo, Jedis jedis) {
		JedisUtil conPool = reidsPools.get(loginfo.getPoolName());
		conPool.close(jedis);
	}
	
	public void close(LoginInfo loginfo) {
		ConnectionPool connectionPool = pools.get(loginfo.getPoolName());
		if(connectionPool==null){
			return;
		}
		connectionPool.close();
		pools.remove(loginfo.getPoolName());
		
	}
}

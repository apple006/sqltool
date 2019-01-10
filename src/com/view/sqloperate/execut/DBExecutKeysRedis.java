package com.view.sqloperate.execut;

import java.util.Iterator;
import java.util.Set;

import com.dao.ManagerThreadPool;
import com.dao.entity.Column;
import com.dao.entity.Table;
import com.dao.imp.RedisTemplate;
import com.entity.LoginInfo;
import com.ui.extensible.Cell;

import redis.clients.jedis.Jedis;


/**
 * в╟йн Dao
 * @author Administrator
 *
 */
public class DBExecutKeysRedis implements Runnable{
	
	
	private Table table;
	private LoginInfo loginfo;
	private String sql;
	private ManagerThreadPool managerThreadPool;
	private boolean isSuccess=true;
	private String error;
	public DBExecutKeysRedis(LoginInfo loginfo,String keys){
		this.sql = keys;
		this.loginfo = loginfo;
		managerThreadPool = ManagerThreadPool.getManagerThreadPool();
	}
	
	@Override
	public void run() {
		Jedis connection = null;
		try {
			connection = getConnection();
			RedisTemplate redis = new RedisTemplate();
			String replaceAll = sql.replaceAll( "\\s+"," ");
			String[] split = replaceAll.split(" ");
			table = redis.execute(split, connection);
			isSuccess = true;

		}
		catch (Exception e) {
			isSuccess = false;
			table = new Table();
			e.printStackTrace();
			error=":     MESSAGE :"+e.getMessage();
		}
		finally{
			synchronized (this) {
				this.notify();
			}
			if(connection!=null){
				close(connection);
			}
		}
	}
	
	private Jedis getConnection() {
		return managerThreadPool.getRedisConnection(loginfo);
	}
	private void close(Jedis jedis ) {
		managerThreadPool.close(loginfo,jedis);
	}

	public Table  getTable(){
		return table;
	}

	public boolean isSuccess() {
		return isSuccess ;
	}

	public String getError() {
		return error;
	}
}

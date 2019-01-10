package com.redis;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.entity.LoginInfo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class JedisUtil {

	// private  String JEDIS_SLAVE;

	public Pool<Jedis> jedisPool;

	public JedisUtil(LoginInfo info) {
		Configuration conf = Configuration.getInstance();
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(10);// 20
		config.setMaxTotal(50); 
		config.setMinIdle(10); 
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setTestWhileIdle(true);
		config.setMinEvictableIdleTimeMillis(60000l);
		config.setTimeBetweenEvictionRunsMillis(3000l);
		config.setNumTestsPerEvictionRun(-1);
		
		if("redis".equals(info.getDataType())){
			if( info.getPassword()!=null&&!"".equals(info.getPassword().trim())){
				jedisPool = new JedisPool(config, info.getIp(), Integer.valueOf(info.getPort()), 100000, info.getPassword(), 0, null);
			}else{
				jedisPool = new JedisPool(config, info.getIp(), Integer.valueOf(info.getPort()), 100000);
			}

		}else if("redis-sentinelPool".equals(info.getDataType())){
			Set<String> set = new HashSet<String>();
			String[] split = info.getIp().split(";");
			for (int i = 0; i < split.length; i++) {
				set.add(split[i]);
			}
			jedisPool = new JedisSentinelPool(info.getUserName(), set,config,10000,info.getPassword());
		}
	}
	
	

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public  String get(String key) {

		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}

		return value;
	}

	public Jedis getJedis(){
		
		return jedisPool.getResource();

	}
	public  void close(Jedis jedis) {
		try {
			jedisPool.returnResource(jedis);

		} catch (Exception e) {
			if (jedis.isConnected()) {
				jedis.quit();
				jedis.disconnect();
			}
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public  byte[] get(byte[] key) {

		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}

		return value;
	}

	public  void set(byte[] key, byte[] value) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	public  void set(byte[] key, byte[] value, int time) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			jedis.expire(key, time);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	public  void hset(byte[] key, byte[] field, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	public  void hset(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public  String hget(String key, String field) {

		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}

		return value;
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public  byte[] hget(byte[] key, byte[] field) {

		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}

		return value;
	}

	public  void hdel(byte[] key, byte[] field) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hdel(key, field);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	/**
	 * 存储REDIS队列 顺序存储
	 * 
	 * @param byte[]
	 *            key reids键名
	 * @param byte[]
	 *            value 键值
	 */
	public  void lpush(byte[] key, byte[] value) {

		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.lpush(key, value);

		} catch (Exception e) {

			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			// 返还到连接池
			close(jedis);

		}
	}

	/**
	 * 存储REDIS队列 反向存储
	 * 
	 * @param byte[]
	 *            key reids键名
	 * @param byte[]
	 *            value 键值
	 */
	public  void rpush(byte[] key, byte[] value) {

		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.rpush(key, value);

		} catch (Exception e) {

			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			// 返还到连接池
			close(jedis);

		}
	}

	/**
	 * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
	 * 
	 * @param byte[]
	 *            key reids键名
	 * @param byte[]
	 *            value 键值
	 */
	public  void rpoplpush(byte[] key, byte[] destination) {

		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.rpoplpush(key, destination);

		} catch (Exception e) {

			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			// 返还到连接池
			close(jedis);

		}
	}

	/**
	 * 获取队列数据
	 * 
	 * @param byte[]
	 *            key 键名
	 * @return
	 */
	public  List<byte[]> lpopList(byte[] key) {

		List<byte[]> list = null;
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			list = jedis.lrange(key, 0, -1);

		} catch (Exception e) {

			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			// 返还到连接池
			close(jedis);

		}
		return list;
	}

	/**
	 * 获取队列数据
	 * 
	 * @param byte[]
	 *            key 键名
	 * @return
	 */
	public  byte[] rpop(byte[] key) {

		byte[] bytes = null;
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			bytes = jedis.rpop(key);

		} catch (Exception e) {

			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {

			// 返还到连接池
			close(jedis);

		}
		return bytes;
	}

	public  void hmset(Object key, Map<String, String> hash) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hmset(key.toString(), hash);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis);

		}
	}

	public  void hmset(Object key, Map<String, String> hash, int time) {
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.hmset(key.toString(), hash);
			jedis.expire(key.toString(), time);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis);

		}
	}

	public  List<String> hmget(Object key, String... fields) {
		List<String> result = null;
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			result = jedis.hmget(key.toString(), fields);

		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis);

		}
		return result;
	}

	public  Set<String> hkeys(String key) {
		Set<String> result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.hkeys(key);

		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis);

		}
		return result;
	}

	public  List<byte[]> lrange(byte[] key, int from, int to) {
		List<byte[]> result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.lrange(key, from, to);

		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis);

		}
		return result;
	}

	// public  Map<byte[],String> hgetAll(byte[] key) {
	// Map<byte[],String> result = null;
	// Jedis jedis = null;
	// try {
	// jedis = jedisPool.getResource();
	// result = jedis.hgetAll(key);
	// } catch (Exception e) {
	// //释放redis对象
	// jedisPool.returnBrokenResource(jedis);
	// e.printStackTrace();
	//
	// } finally {
	// //返还到连接池
	// close(jedis);
	// }
	// return result;
	// }

	public  void del(byte[] key) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	public  long llen(byte[] key) {

		long len = 0;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.llen(key);
		} catch (Exception e) {
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
		return len;
	}

}
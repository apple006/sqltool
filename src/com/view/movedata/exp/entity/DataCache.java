package com.view.movedata.exp.entity;

import java.util.concurrent.ArrayBlockingQueue;

import com.view.tool.HelpProperties;

/**
 * 数据导出时缓存数据
 * @author wanghh
 *
 * @param <T>
 */
public class DataCache<T> {
	
	private ArrayBlockingQueue<T>  sql =new  ArrayBlockingQueue<T>(new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "count")));

	public  void push(T string) {
		try {
			sql.put(string);
//			System.out.println(sql.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public int size() {
		return sql.size();
	}

	public T pop() {
		try {
//			System.out.println("=="+sql.size());
			return sql.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}

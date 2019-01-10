package com.dao;

import java.sql.Connection;

public interface ITemplate {
	final  static String SQLSERVER = "SQLSERVER";
	final  static String ORCL="orcl";
	final  static String REDIS="redis";
	final  static String REDISSENTINELPOOL="redis-sentinelPool";

	final  static String DB2 ="db2";
	final  static String MYSQL ="mysql";
	public String getDBName() ;
}

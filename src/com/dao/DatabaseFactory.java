package com.dao;

import java.util.HashMap;
import java.util.Hashtable;

import com.entity.DataBaseType;
import com.entity.LoginInfo;
import com.view.system.perferenc.xml.XMLJdbcConfig;

public class DatabaseFactory {
	private static DatabaseFactory factory ;
	private static Object o = new Object();
	private HashMap<LoginInfo,Dao> map = new HashMap<>();
	private DatabaseFactory(){
		
	}
	
	public static DatabaseFactory getDataBaseFactory(){
		if(factory == null){
			synchronized (o) {
				if(factory == null){
					factory = new DatabaseFactory();
				}
			}
		}
		return factory;
	}
	
//	public Dao createDataBase(int type) throws Exception{
//		Dao dao = null;
//		switch (type) {
//		case  Dao.ORCL: dao = new OracleTemplate();
//			break;
//
//		}
//		if(dao==null)
//			throw new Exception("没有此类型数据模版");
//		return dao;
//	}

	public Dao createDataBaseTemplate(LoginInfo loginInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Dao dao = map.get(loginInfo);
		
		if(dao==null){
			synchronized (o) {
				dao = map.get(loginInfo);
				if(dao==null){
					XMLJdbcConfig xmlJdbcConfig = XMLJdbcConfig.getXMLJdbcConfig();
					Hashtable<String, DataBaseType> ta = xmlJdbcConfig.getDataBaseTypes();
					DataBaseType dataBaseType = ta.get(loginInfo.getDataType());
					String classUrl = dataBaseType.getClassUrl();
					Class className = Class.forName(classUrl);
					dao = (Dao) className.newInstance();
					map.put(loginInfo, dao);
				}
			}
		}
		return dao;
	}
}

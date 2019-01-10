package com.view.sqloperate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.tree.DefaultMutableTreeNode;

import com.dao.Dao;
import com.dao.entity.Column;
import com.dao.entity.Table;
import com.entity.LoginInfo;
import com.prompt.DBKeyWords;

public class Cache {
	private Map <String,HashMap<String,Table>> tablesMap = new HashMap<String,HashMap<String ,Table>>();
	//根据数据库类型缓存关键字
	private Map <String,String[]> keyMap = new HashMap<String,String[]>();
	private Map <String,HashMap<String,ArrayList<Column>>> columns = new HashMap<String,HashMap<String ,ArrayList<Column>>>();
	private Map <String,Boolean> load = new HashMap<String, Boolean>();
 
	private static Cache cache = new Cache();
	
	public static Cache getCache(){
		return cache;
	}
	
	private Cache(){
		initCache();
		initReidsCache();
	}
	public void setLoade(LoginInfo info ,boolean isLoad){
		load.put(info.getDate(), isLoad);
	}
	public ArrayList<Column> getColumns(LoginInfo info,String tableName) throws SQLException{
		HashMap<String, ArrayList<Column> > column;
		ArrayList<Column> colunmns = new ArrayList<Column>();
		try {
			if(columns.containsKey(info.getDate())){
				if(columns.get(info.getDate()).containsKey(tableName)){
					return columns.get(info.getDate()).get(tableName);
				}else if(columns.get(info.getDate()).containsKey(tableName.toUpperCase())){
					return columns.get(info.getDate()).get(tableName.toUpperCase());
				}else{
					ArrayList<Column> array = Controller.newController().getColumns(info, tableName);
					columns.get(info.getDate()).put(tableName, array);
					return array;
				}
			}else{
				ArrayList<Column> array = Controller.newController().getColumns(info, tableName);
				HashMap<String, ArrayList<Column>> hashMap = new HashMap<String, ArrayList<Column>>();
				hashMap.put(tableName, array);
				columns.put(info.getDate(), hashMap);
				return array;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return colunmns;
	}
	public void loadTableName(LoginInfo info,ArrayList<Table> listTable){
//		if(tablesMap.containsKey(info)){
//			tablesMap.clear();
//		}
		String userName = info.getUserName();
		String[] split = userName.split("#");
		load.put(info.getDate(), true);
		HashMap<String, Table > tables;
		int size = listTable.size();
		for (int i = 0; i < size; i++) {
			tables = tablesMap.get(info.getDate());
			if(tables==null){
				tables = new HashMap<String, Table>();
				tables.put(listTable.get(i).getTableCode().replaceFirst(split.length==2?split[1]+".":"", ""), listTable.get(i));
				tablesMap.put(info.getDate(), tables);
			}else{
				tables.put(listTable.get(i).getTableCode().replaceFirst(split.length==2?split[1]+".":"", ""), listTable.get(i));
				tablesMap.put(info.getDate(), tables);
			}
		}
		if(columns.containsKey(info.getDate())){
			columns.get(info.getDate()).clear();
		}
		
	}
	
	
	public int getTablesSize(LoginInfo loginfo){
		return tablesMap.get(loginfo.getDate())==null?0:tablesMap.get(loginfo.getDate()).size();
	}
	public int getKeyWordsSize(LoginInfo loginfo){
		return keyMap.get(loginfo.getDataType())==null?0:keyMap.get(loginfo.getDataType()).length;
	}
	/**
	 * 得到Table的节点
	 * @param info 登录信息
	 * @param needAddNode  遍历的节点
	 * @param set
	 */
	private void searchTables(LoginInfo info,DefaultMutableTreeNode needAddNode,TreeSet set){
		Enumeration breadthFirstEnumeration = needAddNode.children();
		while (breadthFirstEnumeration.hasMoreElements()) {
			DefaultMutableTreeNode nextElement = (DefaultMutableTreeNode) breadthFirstEnumeration.nextElement();
			if ((nextElement.getUserObject()).toString().equals("Table")) {
				Enumeration tableName = nextElement.children();
				while(tableName.hasMoreElements()){
					DefaultMutableTreeNode next = (DefaultMutableTreeNode) tableName.nextElement();
					if(next.getUserObject() instanceof Table ){
						set.add((Table)(next.getUserObject()));
					}
				}
				break;
			}
			break;
		}
	}
	
	public  void initCache(){
		keyMap.put(Dao.ORCL, DBKeyWords.getDBKeyWorods().getKeyWords());
	}
	public  void initReidsCache(){
		keyMap.put(Dao.REDIS, DBKeyWords.getDBKeyWorods().getRedisKeyWords());
	}
	public String[] getKeyWords(LoginInfo info){
		if(Dao.REDIS.equals(info.getDataType())||Dao.REDISSENTINELPOOL.equals(info.getDataType())){
			return keyMap.get(Dao.REDIS);
		}
		return keyMap.get(Dao.ORCL);
	}

	public Table[] getTable(LoginInfo info) {
		HashMap<String, Table> hashMap = tablesMap.get(info.getDate());
		if(hashMap==null){
			return new Table[0];
		}
		Iterator<String> iterator = hashMap.keySet().iterator();
		Table[] table = new Table[hashMap.size()];
		String next;
		int i=0;
		while(iterator.hasNext()){
			 next = iterator.next();
			 table[i++] = hashMap.get(next);
		}
		return table;
	}
	
	public boolean isTable(LoginInfo loginfo, String table){
		if(tablesMap.containsKey(loginfo.getDate())){
			return tablesMap.get(loginfo.getDate()).containsKey(table)||tablesMap.get(loginfo.getDate()).containsKey(table.toUpperCase());
		}else{
			return false;
		}
	}

	public void loadPks(LoginInfo connInfo, HashMap<String, ArrayList<Column>> pks) {
		HashMap<String, Table> hashMap = tablesMap.get(connInfo.getDate());
		if(hashMap!=null){
			Iterator<String> iterator = hashMap.keySet().iterator();
			String next;
			while(iterator.hasNext()){
				next = iterator.next();
				Table table = hashMap.get(next);
				ArrayList<Column> arrayList = pks.get(next);
				for (int i = 0; i < arrayList.size(); i++) {
					Column column = table.getColumn(arrayList.get(i).getColumnName());					
					column.setPrimaryKey(true);
				}
			}
		}
	}

	public Table getTable(LoginInfo loginInfo, String tableName) {
		 HashMap<String, Table> hashMap = tablesMap.get(loginInfo.getDate());
		 if(hashMap==null){
			 return new Table();
		 }
		 Table table = hashMap.get(tableName);
		 if(table==null){
			 return new Table();
		 }
		 return table;
		
	}

	public boolean getIsLoad(LoginInfo loginfo) {
		Boolean isLoad = load.get(loginfo.getDate());
		if(isLoad==null){
			return true;
		}
		return isLoad;
	}

	public void loadViewName(LoginInfo info, ArrayList<Table> views) {
		HashMap<String, Table > tables;
		int size = views.size();
		for (int i = 0; i < size; i++) {
			tables = tablesMap.get(info.getDate());
			if(tables==null){
				tables = new HashMap<String, Table>();
				tables.put(views.get(i).getTableCode(), views.get(i));
				tablesMap.put(info.getDate(), tables);
			}else{
				tables.put(views.get(i).getTableCode(), views.get(i));
				tablesMap.put(info.getDate(), tables);
			}
		}
		if(columns.containsKey(info.getDate())){
			columns.get(info.getDate()).clear();
		}
	}
	
}

package com.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import com.dao.Dao;

public class ExpAndImpInfo {
	private String[] expTables;
	private String[] impTables;
	private String expDate;
	private String impDate;
	private int pageSize = 500;
	private int end = pageSize;
	private int begin = 0;
	private List conditional = new ArrayList<String>();
	private Map<String, FieldEntity> fields = new HashMap<String, FieldEntity>();

	public ExpAndImpInfo(String expTable, String impTable, String expDate,
			String impDate) {
		this.expDate = expDate;
		this.impDate = impDate;
	}

	public ExpAndImpInfo(String[] expTables, String[] impTables,
			String expDate, String impDate) {
		this.expTables = expTables;
		this.impTables = impTables;
		this.expDate = expDate;
		this.impDate = impDate;
	}


	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getImpDate() {
		return impDate;
	}

	public void setImpDate(String impDate) {
		this.impDate = impDate;
	}

	public int getSqlSize() {
		return fields.size();
	}

	public String getPageQuerySql(Dao dao) {
//			if (dao.getDataBaseType() == Dao.SQLSERVER) {
				Iterator<String> iterator = fields.keySet().iterator();
				StringBuffer sql = new StringBuffer("  ");
				int size = fields.size();
				for (int i = 0; iterator.hasNext() && i < size - 1; i++) {
					sql.append(" tt.");
					sql.append(iterator.next().replace(".", "#"));
					sql.append(" , ");
				}
				sql.append(" tt.");
				sql.append(iterator.next().replace(".", "#"));

				StringBuffer pagsql = new StringBuffer("select ");

				pagsql.append(sql);
				pagsql.append(" from ( select row_number()over(order by tempColumn ) tempRowNumber,t.* from "
						+ "(select top "
						+ (begin + pageSize)
						+ " tempColumn=0,a.* from ( ");
				String sqlQuery = getSQLQuery(false);
				pagsql.append(sqlQuery);
			
				pagsql.append(" ) a)t) tt where tempRowNumber>" + begin);
				System.out.println(pagsql.toString());
				return pagsql.toString();
//			}
//			if (dao.getDataBaseType() == Dao.ORCL) {


//				StringBuffer sql = new StringBuffer(
//						"select b.* from (select t.*,rownum rn from  ( ");
//				String orclQuery = getOrclQuery(false);
//				sql.append(orclQuery);
//				
//				sql.append(")t)b where b.rn<");
//				sql.append(end);
//				sql.append(" and b.rn>");
//				sql.append(begin);
//				begin = begin + pageSize;
//				end = pageSize + begin;
//				System.out.println(sql.toString());
//				return sql.toString();
//			}
//		return "";
	}

	public String getQuerySql(Dao dao){
//		if(dao.getDataBaseType() == Dao.SQLSERVER){
//			return getSQLQuery(true);
//		}
//		if(dao.getDataBaseType() == Dao.ORCL){
//			return getOrclQuery(true);
//		}
		return "";
	}
	private String getOrclQuery(boolean count) {
		int conds = conditional.size();
		Iterator<String> iterator = fields.keySet().iterator();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		int size = fields.size();
		
		for (int i = 0; iterator.hasNext() && i < size - 1&&!count; i++) {
			sql.append(iterator.next());
			sql.append(" , ");
		}
		if(count){
			sql.append(" count(*) c");
		}
		else{
			sql.append(iterator.next());
		}
		sql.append(" from ");
		sql.append(expTables[0]);
		for(int i=1;i<expTables.length;i++){
			
			sql.append(" inner join    ");
			sql.append(expTables[i]);
			sql.append("  on  ");
			sql.append(conditional.get(i-1));
			sql.append("   ");
		}
		return sql.toString();
	}
	
	private String getSQLQuery(boolean count) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		Iterator<String> iterator = fields.keySet().iterator();
		int size = fields.size();
		String next = null;
		
		for (int i = 0; iterator.hasNext() && i < size - 1&&!count; i++) {
			next = iterator.next();
			sql.append(next);
			
			sql.append(" '");
			sql.append(next.replace(".", "#"));
			sql.append("' ");
			sql.append(" , ");
		}
		if(count){
			sql.append(" count(*) c");
		}
		else{
			next = iterator.next();
			sql.append(next);
			sql.append(" '");
			sql.append(next.replace(".", "#"));
			sql.append("' ");
		}
		sql.append(" from ");
		sql.append(expTables[0]);
		for(int i=1;i<expTables.length;i++){
			sql.append(" inner join   ");
			sql.append(expTables[i]);
			sql.append(" on  ");
			sql.append(conditional.get(i-1));
			sql.append("   ");
		}
		return sql.toString();
	}

	public int getBegin() {

		return begin - pageSize;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List getConditional() {
		return conditional;
	}

	public void setConditional(List conditional) {
		this.conditional = conditional;
	}

	public String getInserSql() {
		
		Iterator<String> iterator = fields.keySet().iterator();

		StringBuffer sql = new StringBuffer(" insert into ");
		sql.append(impTables[0]);
		sql.append("(");
		int hasDataImp = 0;
		int size = fields.size();
		for (int i = 0; iterator.hasNext() && i < size - 1; i++) {
			String next = iterator.next();
			FieldEntity fieldEntity = fields.get(next);
			if (fieldEntity.getOpertaion().equals(FieldEntity.TOBEIGDATA)) {
				hasDataImp++;
				continue;
			}
			sql.append(fieldEntity.getImpField());
			sql.append(" , ");
		}
		sql.append(fields.get(iterator.next()).getImpField());
		sql.append(") values(");

		for (int i = 0; i < size - 1 - hasDataImp; i++) {
			sql.append("?,");
		}
		sql.append("?)");
	//	System.out.println(sql.toString());
		return sql.toString();
	}

	/**
	 * 得到所有需要操作Fileds的值
	 * 
	 * @return
	 */
	public Map<String, FieldEntity> getFields() {
		return fields;
	}

	public void setFields(Map<String, FieldEntity> fields) {
		this.fields = fields;
	}

	public void getNextPage(int i) {
		begin = pageSize * i;
		end = pageSize * (i + 1);
	}

	public void setCondition(List conditional) {
		this.conditional = conditional;
	}

}

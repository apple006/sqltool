
package com.dao.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ui.extensible.Cell;
/**
 * 传递结果集对象介质
 * @author Administrator
 *
 */
public class Table implements Cloneable {
	public static final int RESULT_TYPE=0;
	public static final int RESULT_UPDATA=1;

	private int result_type = RESULT_TYPE;
	/**
	 * 表类别（可为 null）
	 */
	private String tableCat;
	/**
	 * 表模式（可为 null）
	 */
	private String tableSchem;
	/**
	 * 表名称
	 */
	private String tableName;
	
	
	/**
	 * 表类型、典型的类型是
	 * "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"
	 * "ALIAS" 和 "SYNONYM"。
	 */
	private String tableType;
	/**
	 * 表的解释性注释
	 */
	private String remarks;
	private Map<String,Column> columName;
	private Map<Integer,List<Cell>> colums;
	private Map<Integer,Column > columEntity;
	private int updateCount;
	private String tableCode;
	private int selectCount;
	private long startTime = System.currentTimeMillis();
	public Table(){
		colums = new HashMap<Integer, List<Cell>>();
		columEntity = new HashMap<Integer, Column>();
		columName = new HashMap<String, Column>();
	}
	public Table(String tableCode) {
		this.tableCode=tableCode;
		colums = new HashMap<Integer, List<Cell>>();
		columEntity = new LinkedHashMap<Integer, Column>();
		columName = new HashMap<String, Column>();
	}
	public void addRow(){
		
	}
	
	public int getResult_type() {
		return result_type;
	}
	public void setResult_type(int result_type) {
		this.result_type = result_type;
	}
	public void addColumn(Column column){
		
		colums.put(column.getNumber(),new LinkedList<Cell>());
		columEntity.put(column.getNumber(), column);
		columName.put(column.getColumnCode(),column);
	}
	public List<Cell> getColumnValus(Column columns){
		return colums.get(columns.getNumber());
	}
	
	public Cell[] getRow( int row){
		Cell[]  cell = new Cell[colums.size()];
		Column[] columns = getColumns();
		for (int i = 0; i < columns.length; i++) {
			cell[i] = colums.get(columns[i].getNumber()).get(row);
		}
		return cell;
	}
	
	
	public String getTableCat() {
		return tableCat;
	}

	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}



	public boolean hasPk(){
		Set<String> keySet = columName.keySet();
		Iterator<String> iterator = keySet.iterator();
		boolean isPk = false;
		while(iterator.hasNext()){
			Column column = columName.get(iterator.next());
			if(column.isPrimaryKey()){
				isPk = true;
				break;
			}
		}
		return isPk;
	}
	
	
	public void setPks(String [] pks){
		for (int i = 0; i < pks.length; i++) {
			columName.get(pks[i]).setPrimaryKey(true);
		}
	}

	public String getTableSchem() {
		return tableSchem;
	}




	public void setTableSchem(String tableSchem) {
		this.tableSchem = tableSchem;
	}




	public String getTableCode() {
		return tableCode==null?"":tableCode;
	}




	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}




	public String getTableType() {
		return tableType;
	}




	public void setTableType(String tableType) {
		this.tableType = tableType;
	}




	public String getRemarks() {
		return remarks;
	}




	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

//	@Override
//	public String toString() {
////		PDTableEntity table = PowerDesigner.initPowerDesigner().getTable(getTableCode());
////		if(table==null){
////			return getTableCode();
////		}
////		String tableName2 = table.getTableName();
//		if(getTableName()==null){
//			getTableCode();
//		}
//		return getTableCode()+"("+tableName2+")";
//	}
	public String getTableName(){
		return tableName;
	}
	
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	/**
	 * 
	 * @param columnName
	 * @param value
	 */
	public void addColumnValue(Integer number, Cell value) {
		colums.get(number).add(value);
	}
	public Column[] getColumns() {
		Set<Integer> keySet = columEntity.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		Column [] co =new Column[columEntity.size()];
		int i=0;
		while(iterator.hasNext()){
			Integer next = iterator.next();
			co[i++] = columEntity.get(next);
			
		}
		return co;
	}
	public void clear() {
		Set<Integer> keySet = columEntity.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		while(iterator.hasNext()){
			Integer next = iterator.next();
			colums.get(next).clear();
		}
	}
	/**
	 * 
	 * @param i 行
	 * @param col 列
	 * @return
	 */
	public Cell getValue(int i,String col) {
		if(columName.get(col)==null){
			return null;
		}
		List<Cell> list = colums.get(columName.get(col).getNumber());
		return list==null?new Cell(""):list.get(i);
	}
	
	@Override
	public Table clone() throws CloneNotSupportedException {
		Table table = new Table();
		table.setTableCat(this.getTableCat());
		table.setTableCode(this.getTableCode());
		table.setTableSchem(this.getTableSchem());
		table.setTableType(this.getTableType());
		Column[] old = this.getColumns();
		for (int i = 0; i < old.length; i++) {
			Column clone = (Column) old[i].clone();
			table.addColumn(clone);
		}
		return table;
	}
	public Column getColumn(int i) {
		return columEntity.get(i);
	}
	
	public Set<Integer> getKeySet(){
		return columEntity.keySet();
	}
	public Column getColumn(String column_name) {
		return	columName.get(column_name);
	}
	public Cell getValue(int i, int j) {
		List<Cell> list = colums.get(j);
		return list==null?new Cell(""):list.get(i);
	}
	public void setUpdataCount(int updateCount) {
		this.updateCount = updateCount;
	}
	public int getUpdataCount() {
		return this.updateCount ;
	}
	public void setSelectConut(int row) {
		this.selectCount = row;
	}
	public int getSelectConut() {
		return  this.selectCount;
	}
	public int getRowNum() {
		int n = 0;
		Set<Integer> keySet = columEntity.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		while(iterator.hasNext()){
			Integer next = iterator.next();
			n = colums.get(next).size();
			break;
		}
		return n;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
}

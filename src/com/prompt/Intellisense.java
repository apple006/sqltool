package com.prompt;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import main.SQLTool;

import com.dao.Dao;
import com.dao.entity.Column;
import com.dao.entity.Table;
import com.entity.LoginInfo;
import com.view.sqloperate.Cache;
import com.view.tool.HelpProperties;
import com.view.tool.PowerDesigner;
import com.view.tool.entity.PDColumnEntity;
import com.view.tool.entity.PDTableEntity;

/**
 * 智能补全操作
 */
public class Intellisense extends JWindow{
	private JList  list;
	private boolean isShow;
	private JScrollPane scrollPane;
	private DefaultListModel listModel;
	
	private LinkedList<PromptLabel> promps = new LinkedList<PromptLabel>();
	private Cache  cache = Cache.getCache();
	private JTextComponent comp;
	 class MyCellRenderer extends JLabel implements ListCellRenderer {

	     public Component getListCellRendererComponent(
	       JList list,              // the list
	       Object value,            // value to display
	       int index,               // cell index
	       boolean isSelected,      // is the cell selected
	       boolean cellHasFocus)    // does the cell have focus
	     {
	         
	         if(value instanceof PromptLabel){
	        	 PromptLabel label = (PromptLabel) value;
	        	 label.setSelect(true);
	        	 setText(label.toString());
		         setIcon(label.getIcon());
		         if (isSelected) {
		             setBackground(list.getSelectionBackground());
		             setForeground(list.getSelectionForeground());
		         } else {
		             setBackground(list.getBackground());
		             setForeground(list.getForeground());
		         }
		         setEnabled(list.isEnabled());
		         setFont(list.getFont());
		         setOpaque(true);
	         }
	         return this;
	     }
	 }

	
	public Intellisense(){
		this.setBounds(7, 7, 7, 7);
		
		scrollPane = new JScrollPane();
		list = new JList();
		
		list.setCellRenderer(new MyCellRenderer());
		scrollPane.getViewport().setView(list);
		list.setBorder(new LineBorder(Color.gray, 2,false));
		list.setSelectedIndex(0);
		this.add(scrollPane);
		
	}

	public Intellisense(JTextComponent comp,GraphicsConfiguration editSql) {
		super(editSql);
		this.comp = comp;
		this.setSize(300, 200);
		scrollPane = new JScrollPane();
		listModel = new DefaultListModel();
		list = new JList();
		list.setCellRenderer(new MyCellRenderer());
		scrollPane.getViewport().setView(list);
		list.setBorder(new LineBorder(Color.gray, 2,false));
		list.setSelectedIndex(0);
		this.add(scrollPane);
	}

	@Override
	public synchronized void addMouseListener(MouseListener paramMouseListener) {
		list.addMouseListener(paramMouseListener);
	}
	
	private boolean cacheIsChange(LoginInfo loginfo){
		return cache.getIsLoad(loginfo);
			
	}
	public void loadKeyWords(LoginInfo loginfo){

		String[] keyWords = cache.getKeyWords(loginfo); 
		for (int i = 0; i < keyWords.length; i++) {
			promps.add(new PromptLabel(keyWords[i],"",PromptLabel.FUNCTION));
		}
	}
	
	public void clearPromps(){
		promps.clear();
	}
	/**
	 * 下载表名信息
	 */
	public void loadTableNameWords(LoginInfo loginfo){
		Table[] table = cache.getTable(loginfo); 
		String userName = loginfo.getUserName();
		String[] split = userName.split("#");
		String pdm_show = HelpProperties.GetValueByKey("keyvalue.properties", "pdm_show");
		for (int i = 0; i < table.length; i++) {
			if("Y".equals(pdm_show)){
				PDTableEntity tableEntity = PowerDesigner.initPowerDesigner().getTable(loginfo,table[i].getTableCode().replaceFirst(split.length==2?split[1]+".":"", ""));
				promps.add(new PromptLabel(table[i].getTableCode().replaceFirst(split.length==2?split[1]+".":"", ""),tableEntity.getTableName(),PromptLabel.TABLE));
			}else{
				promps.add(new PromptLabel(table[i].getTableCode().replaceFirst(split.length==2?split[1]+".":"", ""),"",PromptLabel.TABLE));
			}
		}
	}
	
	private void mergeArray(LoginInfo loginfo){
		cache.setLoade(loginfo,false);
		promps.clear();
		loadKeyWords(loginfo);
		loadTableNameWords(loginfo);
		Collections.sort(promps, new PrompSort());
		list.setListData(promps.toArray(new PromptLabel[0]));
	}
	public boolean change(String key,LoginInfo loginfo, String sql){
		String userName = loginfo.getUserName();
		int indexOf = key.indexOf(".");
		String[] split = key.split("\\.");
		if(split.length==0){
			return true;
		}
		if(Dao.REDIS.equals(loginfo.getDataType())||Dao.REDISSENTINELPOOL.equals(loginfo.getDataType())){
			list.removeAll();
			LinkedList<PromptLabel> redisPromps = new LinkedList<PromptLabel>();
			String[] keyWords = cache.getKeyWords(loginfo); 
			for (int i = 0; i < keyWords.length; i++) {
				redisPromps.add(new PromptLabel(keyWords[i],"",PromptLabel.FUNCTION));
			}
			PromptLabel[] filtering = filtering(split[0],redisPromps);
			list.setListData(filtering);
			list.setSelectedIndex(0);
			return true;
		}
		if(cacheIsChange(loginfo)){
			mergeArray(loginfo);
		}
		list.removeAll();
		String tableName =null;
		if(indexOf==-1){
			PromptLabel[] filtering = filtering(split[0],promps);
			list.setListData(filtering);
		}else{
			
			if(split.length>2){
				list.removeAll();
				return true;
			}else{
				if(key.endsWith(".")){
					ArrayList<Column> columns = null;
					try {
						if(cache.isTable(loginfo,split[0])){
								columns = cache.getColumns(loginfo, split[0]);
						}else{
							String refTableName = getRefTableName(sql,split[0]);
							columns = cache.getColumns(loginfo, refTableName);
							tableName = refTableName;
						}
						if(columns==null){
							list.setListData(new PromptLabel[0]);
							return true;
						}
						PromptLabel[] columnName = new PromptLabel[columns.size()];
						String pdm_show = HelpProperties.GetValueByKey("keyvalue.properties", "pdm_show");
						
						tableName = tableName==null?key.replaceAll("\\.*", ""):tableName;
						for (int i = 0; i < columns.size(); i++) {
							PDColumnEntity table = null;
							if("Y".equals(pdm_show)){
								 table = PowerDesigner.initPowerDesigner().getTable(loginfo,tableName,columns.get(i).getColumnCode());
							}
							columnName[i] = (new PromptLabel(columns.get(i).getColumnCode(),table==null?"":table.getColumnComment(),PromptLabel.COLUMN));
						}
						list.setListData(columnName);
					} catch (SQLException e) {
						PromptLabel[] columnName = new PromptLabel[1];
						columnName[0] = new PromptLabel(e.getMessage(), "",PromptLabel.ERROR);
						list.setListData(columnName);

						e.printStackTrace();
					}
				}else{
					String pdm_show = HelpProperties.GetValueByKey("keyvalue.properties", "pdm_show");
					ArrayList<Column> columns = null;
					try {
						if(cache.isTable(loginfo,split[0])){
								columns = cache.getColumns(loginfo, split[0]);
						}else{
							String refTableName = getRefTableName(sql,split[0]);
							columns = cache.getColumns(loginfo, refTableName);
						}
						if(columns==null){
							return true;
						}
						LinkedList<PromptLabel> arrColumn = new LinkedList<PromptLabel>();
						tableName = key.substring(0, key.indexOf("."));
						for (int i = 0; i < columns.size(); i++) {
							PDColumnEntity table = null;
							if("Y".equals(pdm_show)){
								 table = PowerDesigner.initPowerDesigner().getTable(loginfo,tableName,columns.get(i).getColumnCode());
							}
							arrColumn.add(new PromptLabel(columns.get(i).getColumnCode(),table==null?"":table.getColumnComment(),PromptLabel.COLUMN));
						}
	//					for (int i = 0; i < columns.size(); i++) {
	//						arrColumn.add((new PromptLabel(columns.get(i).getColumnCode(),"",PromptLabel.COLUMN)));
	//					}
						PromptLabel[] filtering = filtering(split[1],arrColumn);
						list.setListData(filtering);
					} catch (SQLException e) {
						PromptLabel[] columnName = new PromptLabel[1];
						columnName[0] = new PromptLabel(e.getMessage(), "",PromptLabel.ERROR);
						list.setListData(columnName);
						e.printStackTrace();
					}
				}
		}
		}
//		scrollPane.getViewport().setView(list);
//		this.add(scrollPane);
		list.setSelectedIndex(0);
		return true;
 	}
	
	private PromptLabel [] filtering(String word,LinkedList<PromptLabel> array ){
		String replaceAll = word.replaceAll("[(),\\.{}\\[\\]\\']+", "");

		ArrayList<PromptLabel> arr = new ArrayList<PromptLabel>();
		for (int i = 0; i < array.size(); i++) {
			if(word.startsWith("*")){
				String newWord = replaceAll.replaceAll("\\*", "");
				
				boolean matches = array.get(i).getCode().toUpperCase().matches("\\w*"+newWord.toUpperCase()+"\\w*");
				if(matches){
					arr.add(array.get(i));
				}
			}
			else{
				boolean matches = array.get(i).getCode().toUpperCase().startsWith(replaceAll.toUpperCase());
				if(matches){
					arr.add(array.get(i));
				}
			}
		}
		return arr.toArray(new PromptLabel[0]);
	}
//	\\s+(\\w+)\\s+|
	public String getRefTableName(String sql,String refTable){
//		Pattern pat = Pattern.compile("\\s+(\\(.+\\))\\s+"+refTable+"\\s*|\\W+(\\w+)\\s+"+refTable+"[\\W;\\)\\(]+",Pattern.CASE_INSENSITIVE);
		String tableNamesRegEx1 =  "(from)([\\s*\\w*,)]*)(?:where|left|start|on)";
	    Pattern pattern = Pattern.compile(tableNamesRegEx1,Pattern.CASE_INSENSITIVE);
	    String[] split = sql.split("\\s+"+refTable+"\\s+");
	    if(split.length>1) {
	    	String[] split2 = split[split.length-2].split("\\s+");
	    	if(split2.length>0) {
	    		return split2[split2.length-1];
	    	}
	    	return "";
	    }
//	    Matcher matcher = pattern.matcher(vo.getSql());
//	    String tempTableName,tempStr;
//	    List<Map<String, Object>> columns =  new ArrayList<>();
//	    Set setTableNames =  new HashSet<>();
//	    Set setCloumnNames =  new HashSet<>();
//	    Map<String, String> queryColumnsForSql = queryColumnsForSql(vo.getSql());
//
//	    while(matcher.find()){
//	    	tempStr = matcher.group(2);
//	    	String[] split = tempStr.trim().split("[,\\s]");
//	    	for (int i = 0; i < split.length; i++) {
//	    		String str =  split[i].trim().replaceAll("\\s+\\w+", "");
//	    		if(setTableNames.add(str)) {
//	    			List<Map<String, Object>> queryColumns = queryColumns(str);
//	    			for (int j = 0; j < queryColumns.size(); j++) {
//	    				if(setCloumnNames.add(queryColumns.get(j).get("COLUMNNAME"))){
//	    					String columsAlias = columsAliasMap.get(queryColumns.get(j).get("COLUMNNAME"));
//	    					if(queryColumns.get(j).get("COLUMNNAME").equals("NOTITY_FINISH_TIME")) {
//	    						queryColumns.get(j).get("COLUMNNAME");
//	    					}
//	    					if(columsAlias!=null) {
//	    						if(queryColumnsForSql.containsKey(columsAlias)) {
//	    							Map<String, Object> map2 = queryColumns.get(j);
//	    							map2.put(columsAlias, queryColumns.get(j));
//	    							columns.add(map2);
//	    						}
//	    					}else 
//	    						if(queryColumnsForSql.containsKey(queryColumns.get(j).get("COLUMNNAME"))) 
//	    							columns.add(queryColumns.get(j));
//	    				}
//					}
//	    		}
//			}
//	    }
		return "";
	}
	public static void main(String[] args) {
		Intellisense in = new Intellisense();
		StringBuffer  sql = new StringBuffer("SELECT c. from PRP_BASERATE_SET c ");
//		sql.append(" SELECT ");
//		sql.append("    *  ");
//		sql.append(" FROM  ");
//		sql.append("     PRP_ECCOST_SET acc ");
//		sql.append(" WHERE  ");
//		sql.append("   acc.pk_prd IN  ");
//		sql.append("   (  ");
//		sql.append("      SELECT  ");
//		sql.append("          pk_ftp_prd  ");
//		sql.append("     FROM  ");
//		sql.append("        rm_prd  ");
//		sql.append("    WHERE  ");
//		sql.append("        pk_prd_set =  ");
//		sql.append("       (  ");
//		sql.append("           SELECT  ");
//		sql.append("               pk_ftp_prd_set  ");
//		sql.append("            FROM  ");
//		sql.append("                 rm_prd_set  ");
//		sql.append("             WHERE  ");
//		sql.append("                pk_ftp_prd_set =  ");
//		sql.append("             (  ");
//		sql.append("                   SELECT  ");
//		sql.append("                       pk_loan_prd_set  ");
//		sql.append("                   FROM  ");
//		sql.append("                       prp_price_set  prd");
//		sql.append("                   where  prd.pk_corp='");
//		sql.append("AAAAAAAAAAAAAAAAA");
//		sql.append("' ))  ");
//		sql.append("     AND (  ");
//		sql.append(32);
//		sql.append("      >term_start AND  ");
//		sql.append(60);
//		sql.append("         <=term_end) )  ");
//		sql.append(" AND pk_coa =  '");
		in.getRefTableName(sql.toString(),"c");
	}
	public JScrollPane getScrollPane(){
		return scrollPane;
	}
	public JList getList(){
		return list;
	}
	public void nextRow(){
		list.setSelectedIndex(list.getSelectedIndex()+1);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		Point p = list.indexToLocation(list.getSelectedIndex());
		int value = scrollBar.getValue();
		if(value+scrollBar.getHeight()<=p.y){
			scrollBar.setValue(list.indexToLocation(list.getSelectedIndex()-10).y);
		}
	}
	public void upRow(){
		if(list.getSelectedIndex()==-1)
			return;
		list.setSelectedIndex(list.getSelectedIndex()-1);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		int value = scrollBar.getValue();
		Point p = list.indexToLocation(list.getSelectedIndex());
		
		if(value>=p.y){
			scrollBar.setValue(p.y);
		}
	}

	@Override
	public void setVisible(boolean arg0) {
		isShow = arg0;
		if(isShow){
			referLocation();
		}
		list.setSelectedIndex(0);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setValue(0);
		super.setVisible(arg0);
		
	}
	public boolean isShow(){
		return isShow;
	}
	
	public void referLocation() {
		Rectangle r = null;
		int size =comp.getFontMetrics(comp.getFont()).getHeight();
		try {
			r = comp.modelToView(comp.getCaretPosition());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		Point convertPoint = SwingUtilities.convertPoint(comp,
				(int) r.getX()
						+ SQLTool.getSQLTool().getToolFrame()
								.getFrame().getX(), (int) r.getY()
						+ SQLTool.getSQLTool().getToolFrame()
								.getFrame().getY(), SQLTool
						.getSQLTool().getToolFrame().getFrame());
		
		this.setLocation((int) convertPoint.getX(),
				(int) convertPoint.getY() + size);
	}
	
	class PrompSort implements Comparator<PromptLabel>{

		@Override
		public int compare(PromptLabel o1, PromptLabel o2) {
			if(o1.getType()>o2.getType()){
				return 1;
			}else{
				if(o1.getType()<o2.getType()){
					return -1;
				}else {
						return o1.getCode().compareTo(o2.getCode());
				}
			}
			
//			return o1.getCode().compareTo(o2.getCode());
		}
	}
	
	/**
	 * 
	 * @return 所选择提示信息单词
	 */
	public String getSelectWord(){
		Object selectedValue = list.getSelectedValue();
		if(selectedValue instanceof PromptLabel){
			return ((PromptLabel)selectedValue).getCode();
		}
		if(list.getSelectedValue()==null){
			return "";
		}
		return list.getSelectedValue().toString();
	}
	
	
}

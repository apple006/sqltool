package com.ui;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MyTableColor extends DefaultTableCellRenderer {

	private Map<String, Integer> map;

	public MyTableColor(Map<String, Integer> map) {
		this.map = map;
	}
	public MyTableColor() {
		this.map = new HashMap<>();
	}
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table,  
            Object value, boolean isSelected, boolean hasFocus,  
            int row, int column) {  
    	if(map.containsKey(table.getValueAt(row, 0))){
    		if(((Integer)map.get(table.getValueAt(row, 0)))>1){
    			setBackground(Color.BLUE);
    		}else{
    			setBackground(Color.green);
    		}
    	}else{
    		setBackground(Color.WHITE);

    	}
     return super.getTableCellRendererComponent(table, value,  
               isSelected, hasFocus, row, column);  
     
    }
	public void put(String key) {
		map.put(key, map.get(key)==null?1:map.get(key)+1);
	}
	public void remove(String key) {
		if(map.get(key)!=null&&map.get(key)>1){
			map.put(key, map.get(key)==null?1:map.get(key)-1);
			return;
		}
		this.map.remove(key);
	}
}

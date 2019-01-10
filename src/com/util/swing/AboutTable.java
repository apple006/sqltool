package com.util.swing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public  class  AboutTable {
	public static DefaultTableModel initTableRoot(List<Map<String,String>> list){
		Iterator<String> iterator = list.get(0).keySet().iterator();
		List<String> ls =  new ArrayList<String>();
		while (iterator.hasNext()) {
			 ls.add(iterator.next());
		}
		int n = ls.size();
		String str = null;
		DefaultTableModel mode = new DefaultTableModel(ls.toArray(), 0);
		int size = list.size();
		String [] arr = new String [size] ;
		for (int i = 1; i <size; i++) {
			for(int j = 0;j<n;j++){
				arr[j] =list.get(i).get(ls.get(j));
			}
			mode.addRow(arr);
		}
		return mode;
	}
}

package com.ui.extensible;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class EditTableCellRenderer extends DefaultTableCellRenderer {
	public EditTableCellRenderer() {
	}

	
	
	public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column){
        Component cell = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);  
        	this.setColor(cell, table, isSelected, hasFocus, row, column);
    		Cell valueAt = (Cell) table.getValueAt(row, column);
    		if(valueAt==null){
    			return this;
    		}
    		if(valueAt.getNew_value()==null){
    			if(valueAt.getOld_value()!=null)
    				setText(valueAt.getOld_value().toString());
    			else{
    				setText("null");
    			}
    		}else{
    			if(valueAt.getNew_value().toString().length()>0){
    				setText(valueAt.getNew_value().toString());
    			}else{
    				setText("null");
    			}
    		}
            return this;
    
	}

	/*
	 * …Ë÷√—’…´
	 */
	private void setColor(Component component, JTable table,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Cell valueAt = (Cell) table.getValueAt(row, 0);
		if (isSelected) {
			Color selectionBackground = table.getSelectionBackground();
			component.setBackground(selectionBackground);
			if (valueAt == null || "".equals(valueAt)) {
				component.setBackground(selectionBackground);
			} else {
				component.setBackground(Color.green);
			}
		} else {
			if (valueAt != null
					&& (UITable.ADD.equals(valueAt.getNew_value()) || UITable.EDIT
							.equals(valueAt.getNew_value()))) {
				component.setBackground(Color.yellow);
			}
			if (valueAt != null && (UITable.DELETE.equals(valueAt.getNew_value()))) {
				component.setBackground(Color.red);
			}
		}
		table.repaint();
	}

	

}

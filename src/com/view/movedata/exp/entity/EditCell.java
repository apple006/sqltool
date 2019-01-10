package com.view.movedata.exp.entity;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class EditCell  extends DefaultTableCellRenderer{
	private JPanel panel;
	private JLabel label;
	public EditCell(){
		   panel = new JPanel(new BorderLayout());
	       panel.setOpaque(true);
	       label = new JLabel();
	       panel.add(label,BorderLayout.CENTER);
	       panel.add(new JButton("±à¼­"),BorderLayout.EAST);
	}
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
    	label.setText(value==null?"":value.toString());
    	setValue(value);
        return  panel;
    }
}
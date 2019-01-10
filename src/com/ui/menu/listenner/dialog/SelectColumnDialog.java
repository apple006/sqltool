package com.ui.menu.listenner.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.dao.entity.Column;

public class SelectColumnDialog extends JDialog{

	private static final int WIDE = 400;
	private static final int HEIGHT = 300;
	private JButton ok = new JButton("确定");
	private JButton close = new JButton("取消");
	private boolean isOk ;
	private DefaultTableModel tableModel;
	private JTable table;
	private Column[] colums;

	public SelectColumnDialog(Frame  comp ,String text){
		super(comp,text,true);
		this.setSize(WIDE, HEIGHT);
		tableModel = new DefaultTableModel();
		tableModel.addColumn("");
		tableModel.addColumn("列名");
		table = new JTable(tableModel);
		TableColumn column1 = table.getColumnModel().getColumn(0); 
		column1.setMaxWidth(30);
		column1.setPreferredWidth(30);
		column1.setWidth(30);
		column1.setMinWidth(30);
		setLocationRelativeTo(null);

		JScrollPane scrollpane1 = new JScrollPane(table,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		TableColumn column = table.getColumnModel().getColumn(0); 
		column.setCellEditor(table.getDefaultEditor(Boolean.class));   
		column.setCellRenderer(table.getDefaultRenderer(Boolean.class));
		setLayout(new BorderLayout());
		add(scrollpane1,BorderLayout.CENTER);
		JPanel iniBut =getButs();
		add(iniBut,BorderLayout.SOUTH);
	}

	private JPanel getButs() {
		JPanel  panel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isOk = true;
				SelectColumnDialog.this.setVisible(false);
			}
		});
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isOk = false;
				SelectColumnDialog.this.setVisible(false);
			}
		});
		panel.add(ok);
		panel.add(close);
		return panel;
	}
	
	public String[] showDialog(Column[] colums) throws InterruptedException {
		this.colums = colums;
		for (int i = 0; i < colums.length; i++) {
			tableModel.addRow(new Object[]{false,colums[i].toString()});;
		}
		super.show();
		if(isOk){
			isOk = false;
			return getOK();
		}
		return new String[]{};
	}	

	
	public String[] getOK(){
		this.setVisible(false);
		ArrayList<String> arr = new ArrayList<String>();
		int rowCount = table.getRowCount();
		for (int i = 0; i <rowCount; i++) {
			if((Boolean) table.getValueAt(i, 0)){
				arr.add(colums[i].getColumnCode());
			}
		}
		return arr.toArray(new String[0]);
	}

}





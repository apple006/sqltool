package com.view.movedata.exp.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.view.movedata.exp.entity.ColumnType;
import com.view.movedata.exp.entity.RandomTest;
import com.view.movedata.exp.ui.IPop;
import com.view.sqloperate.LineNumberHeaderView;
import com.view.system.dialog.WaringMsg;
import com.view.tool.HelpProperties;
import com.view.tool.IUtilCheck;

public class BindVariables implements IPop,IUtilCheck{
	private JPanel panel1;
	private DefaultTableModel listValue;
	private JTable listTable;
	private JTextField codeText;
	private JRadioButton common;
	private JRadioButton excel;
	private JRadioButton sql;
	private JLabel jLabel;
	private JComboBox connectionList;
	private final String BINDVARIABLES ="bindvariables.properties";
	public BindVariables(Object object){
		panel1 = new JPanel(new BorderLayout());
	
		
		listValue = new DefaultTableModel();
		listTable = new JTable(listValue);
		
		listValue.addColumn("绑定变量名");
		listValue.addColumn("类型");
		listValue.addColumn("绑定变量值");
		listValue.addColumn("增量值");
		
		
		TableColumn edit = listTable.getColumnModel().getColumn(1);
		JComboBox jComboBox = new JComboBox(new Object[]{new ColumnType(ColumnType.DEFULT_WHERE),new ColumnType(ColumnType.CHANGE_WHERE),ColumnType.FORCE_WHERE});
		DefaultCellEditor defa =new DefaultCellEditor(jComboBox);
		edit.setCellEditor(defa);
		jComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				listTable.setValueAt("", listTable.getSelectedRow(), 1);
			}
		});
		JScrollPane selectScrol = new JScrollPane(listTable,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		LineNumberHeaderView lineNumberHeaderView = new LineNumberHeaderView();
		lineNumberHeaderView.setFont(listTable.getFont());
		selectScrol.setRowHeaderView(lineNumberHeaderView);
		lineNumberHeaderView.setLineHeight(listTable.getRowHeight());
		
		panel1.add(selectScrol,BorderLayout.CENTER);
		
		JPanel panel3 = initButtons();
		panel1.add(panel3,BorderLayout.EAST);

		Properties properties;
		properties = HelpProperties.getAll(BINDVARIABLES);
		Enumeration<Object> keys = properties.keys();
		while(keys.hasMoreElements()){
			String key = (String) keys.nextElement();
			String val = (String) properties.get(key);
			String[] split = val.split("#");
			if(key.equals(ColumnType.CHANGE_WHERE+"")){
				((DefaultTableModel)listTable.getModel()).addRow(new Object[]{key,split[0],split[1],""});
			}else{
				((DefaultTableModel)listTable.getModel()).addRow(new Object[]{key,split[0],split[1],split[2]});
			}
		}
	}

	private JPanel initButtons() {
		JPanel panel=new JPanel(); 
		BoxLayout layout=new BoxLayout(panel, BoxLayout.Y_AXIS); 
		panel.setLayout(layout);

		JButton add = new JButton("增加");
		panel.add(add);
		JButton update = new JButton("更新");
		JButton delete = new JButton("删除");
		add.addActionListener(new OnClick());
		update.addActionListener(new OnClick());
		delete.addActionListener(new OnClick());

		panel.add(delete);
		panel.add(update);
		return panel;
	}


	private void clear(){
		int rowCount = listTable.getRowCount();
		int j = 0;
		for (int i = 0; i < rowCount; i++,j++) {
			listValue.removeRow(i-j);
		}
	}
	
	@Override
	public Object getObject() {
		clear();
//		int rowCount = listTable.getRowCount();
//		listValue.addColumn("类型");
//		listValue.addColumn("绑定变量名");
//		listValue.addColumn("绑定变量值");
//		listValue.addColumn("增量值");
//		for (int i = 0; i <rowCount; i++) {
//			if(isNull(listTable.getValueAt(i, 0).toString())){
//				WaringMsg.showMessageDialog("请选择类型！");
//				return false;
//			}
//			if(isNull(listTable.getValueAt(i, 1).toString())){
//				WaringMsg.showMessageDialog("请填写绑定变量名！");
//				return false;
//			}
//			if(isNull(listTable.getValueAt(i, 2).toString())){
//				WaringMsg.showMessageDialog("请填写绑定变量值！");
//				return false;
//			}
//			if(isNull(listTable.getValueAt(i, 2).toString())&&(ColumnType.FINAL+"").equals(listTable.getValueAt(i, 0).toString())){
//				WaringMsg.showMessageDialog("请填写增量值！");
//				return false;
//			}
//
//			HelpProperties.WriteProperties(BINDVARIABLES, listTable.getValueAt(i, 0).toString(),listTable.getValueAt(i, 1).toString()+","+listTable.getValueAt(i, 2).toString()+","+listTable.getValueAt(i, 3).toString());
//		}
		
		return false;
	}

	@Override
	public JPanel getJPanel() {
		return panel1;
	}
	
	class OnClick implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String string = ((JButton) e.getSource()).getText();
			if("增加".equals(string)){
				int selectedRow = listTable.getSelectedRow();
				listValue.insertRow(selectedRow==-1?0:selectedRow+1, new String[]{""});
			}
			if("删除".equals(string)){
				int[] selectedRow = listTable.getSelectedRows();
				int r = 0;
				for (int i = 0; i < selectedRow.length; i++,r++) {
					HelpProperties.remove(BINDVARIABLES, listTable.getValueAt(i, 0).toString());
					int j = selectedRow[i-r];
					listValue.removeRow(j);
					
				}
			}
			if("更新".equals(string)){
				int rowCount = listTable.getRowCount();
			
				for (int i = 0; i <rowCount; i++) {
					if(isNull(listTable.getValueAt(i, 0))){
						WaringMsg.showMessageDialog("请选择类型！");
						return ;
					}
					if(isNull(listTable.getValueAt(i, 1))){
						WaringMsg.showMessageDialog("请填写	变量名！");
						return ;
					}
					if(isNull(listTable.getValueAt(i, 2))){
						WaringMsg.showMessageDialog("请填写绑定变量值！");
						return ;
					}
					if(isNull(listTable.getValueAt(i, 2))&&(ColumnType.FORCE_WHERE+"").equals(listTable.getValueAt(i, 0))){
						WaringMsg.showMessageDialog("请填写增量值！");
						return ;
					}
					if(listTable.getCellEditor()!=null){
						listTable.getCellEditor().stopCellEditing();
					}
					HelpProperties.WriteProperties(BINDVARIABLES, listTable.getValueAt(i, 0).toString(),listTable.getValueAt(i, 1).toString()+"#"+listTable.getValueAt(i, 2).toString()+"#"+listTable.getValueAt(i, 3));
				}
			}
		}
	}
	
}


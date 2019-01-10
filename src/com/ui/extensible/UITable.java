package com.ui.extensible;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.dao.entity.Column;

public class UITable extends JTable {
	private ArrayList<Column> HeaderColumn = new ArrayList<Column>();
	public final static String IS_EDIT_TYPE = "_is_edit_";
	private DefaultTableModel tableModel;
	private Column col = new Column();
	public static final String EDIT = "edit";
	public static final String DELETE = "delete";
	public static final String ADD = "add";
	private TableRowSorter<DefaultTableModel> tableRowSorter;
	private String tableName;

	/**
	 * 
	 */
	public UITable() {
		setColumnSelectionAllowed(false); 
		setRowSelectionAllowed(false); 
		setCellSelectionEnabled(true);
		tableModel = new DefaultTableModel();
		tableRowSorter = new TableRowSorter(tableModel);
		
		
//		DefaultTableCellRenderer cellrender = new DefaultTableCellRenderer();
//		cellrender.setHorizontalAlignment(JTextField.LEFT);
		
//		setDefaultRenderer(String.class, cellrender);
		  
		setDefaultEditor(Object.class, new MyEditor());     
		setRowSorter(tableRowSorter);
		MyDefaultTableColumnModel column = new MyDefaultTableColumnModel(
				this.getColumnModel());
		this.setModel(tableModel);
		this.setTableHeader(column);
		Column column2 = new Column();
		column2.setColumnCode("_is_edit_");
		tableModel.addColumn(column2, new Cell[0]);
		HeaderColumn.add(column2);
	}

	public UITable(DefaultTableModel tableModel) {
		super(tableModel);
		this.tableModel = tableModel;
	}

	/**
	 * 
	 * @param str
	 *            一条数据
	 * @return
	 */
	public void addRow(LinkedList row) {
		if (row == null) {
			return;
		}
//		row.addFirst(tableModel.getRowCount());
		tableModel.addRow(row.toArray());
	}

	public TableRowSorter getTableRowSorter() {
		return tableRowSorter;
	}

	public void addRowNull(int row) {
		Cell[] s = new Cell[tableModel.getColumnCount() + 1];
		for (int i = 0; i < s.length; i++) {
			 s[i]= new Cell("");
		}
		s[0].setNew_value(ADD);
//		tableModel.addRow(s);
		tableModel.insertRow(row, s);
	}

	/**
	 * 添加列名
	 * 
	 * @param colums
	 */
	// public void addColums(List<UIColum> colums) {
	//
	// for (int i = 0; i < colums.size(); i++) {
	// tableModel.addColumn(colums.get(i));
	// }
	// getColumnModel().getColumn(0).setPreferredWidth(40);
	// }

	/**
	 * 添加列名及相应的列数据
	 * 
	 * @param colums
	 *            列
	 * @param objects
	 *            列数据
	 */
	public void addColumAndData(Column colum, Cell[] objects) {
		tableModel.addColumn(colum, objects);

		HeaderColumn.add(colum);
		hideColumn(0);
	}
	
	

	public Column getHeaderColumn(int i) {
		return HeaderColumn.get(i) ;
	}

	public TableCellRenderer getCellRenderer(int row, int column) {
//		setCellEditor(new BuffCellEditor(new JTextField()));
		EditTableCellRenderer de = new EditTableCellRenderer();
//		de.setHorizontalAlignment(SwingConstants.LEFT);
//		de.setVerticalAlignment(SwingConstants.CENTER  );
		return de;
	}

	public void hideColumn(int index) {
		TableColumn tc = this.getColumnModel().getColumn(index);
		tc.setMaxWidth(0);
		tc.setPreferredWidth(0);
		tc.setWidth(0);
		tc.setMinWidth(0);
		this.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
		this.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		super.setValueAt(aValue, row, column);
	}


	public void editingStopped(ChangeEvent e) {
		TableCellEditor editor = getCellEditor();
		if (editor != null) {
			Object value = editor.getCellEditorValue();
			Cell cell = (Cell) getValueAt(editingRow, editingColumn);
//			cell.setOld_value(cell.getNew_value());
			cell.setNew_value(value);
			setValueAt(cell, editingRow, editingColumn);
			removeEditor();
		}
	}

	public Column[] getHeaderColumns() {
		return HeaderColumn.toArray(new Column[0]);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableName( ) {
		return 	this.tableName ;
	}
}

class MyEditor extends DefaultCellEditor {     
	  public MyEditor() {     
	    super(new JTextField());     
	  }     
	    
	  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,     
	      int row, int column) {     
	    //获得默认表格单元格控件     
	    JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected,     
	        row, column);   
//	    editor.setText(t);
//	    if (value != null)     
//	    TableCellEditor editor1 = table.getCellEditor();
	    Cell cell = (Cell) table.getValueAt(row, column);
		if (cell != null) {
//			cell.setNew_value(value);
			Object new_value = cell.getNew_value();
			if(new_value!=null){
				editor.setText(new_value.toString());   
			}
//		    editor.setText(new_value);   


		}
	    if (column == 0) {     
	      //设置对齐方式     
	      editor.setHorizontalAlignment(SwingConstants.CENTER);     
	      editor.setFont(new Font("Serif", Font.BOLD, 14));     
	    } else {     
	      editor.setHorizontalAlignment(SwingConstants.RIGHT);     
	      editor.setFont(new Font("Serif", Font.ITALIC, 12));     
	    }     
	    return editor;     
	  }     
	}  

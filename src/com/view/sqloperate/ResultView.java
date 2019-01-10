package com.view.sqloperate;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;

import com.dao.entity.Column;
import com.dao.entity.Table;
import com.dao.imp.Page;
import com.entity.LoginInfo;
import com.ui.extensible.Cell;
import com.ui.extensible.UITable;
import com.ui.menu.MenuAndTool;
import com.ui.menu.xml.XMLMenuAndToolFactory;
import com.view.tool.HelpProperties;
import com.view.tool.PowerDesigner;
import com.view.tool.entity.PDColumnEntity;

public class ResultView extends JPanel{
	private JLabel resultMessage;
	private UITable table;
	private JScrollPane scrollpane1;
//	private JMenuBar resultBar;
	private MenuAndTool menu_tool =  XMLMenuAndToolFactory.getXMLMenuAndToolFactory();
//	private JTextArea log = new JTextArea();
//	private UITable old_table;
	static int i =0;
	private LineNumberHeaderView lineNumberHeaderView = new LineNumberHeaderView();

	public ResultView(){
		JPanel toolBar = menu_tool.getButtons("resultBar");
		resultMessage = new JLabel();
		table = new UITable();
//		DefaultTableCellRenderer cellrender = new DefaultTableCellRenderer();
//		cellrender.setHorizontalAlignment(JTextField.LEFT);
//		
//		setDefaultRenderer(String.class, cellrender);
//		old_table = new UITable();
//		table.getRowSorter().setSortKeys(a);
//		table.setEnabled(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF    );
	
		setLayout(new BorderLayout());
		JPanel pane = new JPanel(new BorderLayout());
		pane.add(toolBar,BorderLayout.EAST);
		final JTextField find = new JTextField(15);
		JPanel l = new JPanel();
		l.add(find);
		pane.add(l,BorderLayout.WEST);
		find.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				
				table.getTableRowSorter().setRowFilter(RowFilter.regexFilter(".*"+find.getText()+".*"));
			}
		});

		add(pane,BorderLayout.NORTH);
		add(resultMessage,BorderLayout.SOUTH);
		scrollpane1 = new JScrollPane(table,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollpane1,BorderLayout.CENTER);
		table.getModel().addTableModelListener(new TableModelListener()
		{
			@Override
			public void tableChanged(TableModelEvent e)
			{ 	
				int selectedRow = table.getSelectedRow();
				int selectedcol = table.getSelectedColumn();
				int col = e.getColumn();
				int row = e.getFirstRow();
				if(col==-1||row==-1){
					return;
				}
				Cell cell = (Cell) table.getValueAt(selectedRow, 0);
				Cell edit = (Cell) table.getValueAt(selectedRow, selectedcol);
				if(UITable.ADD.equals(cell==null?"":cell.getNew_value())){
					return;
				}
				if(col!=0){
					Cell cell2 = new Cell();
					if(edit.isChange()){
						cell2.setNew_value(UITable.EDIT);
						table.setValueAt(cell2, selectedRow, 0);
					}else{
//						cell2.setNew_value("");
//						table.setValueAt(new Cell(""), selectedRow, 0);
				}		
				}
			}
		});
	}
	
//	public ResultView() {
//		JPanel toolBar = menu_tool.getButtons("resultBar");
//		resultMessage = new JLabel();
//		table = new UITable();
//		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
//		setLayout(new BorderLayout());
//		JPanel pane = new JPanel(new BorderLayout());
//		pane.add(toolBar,BorderLayout.EAST);
//		pane.add(new JTextField(""),BorderLayout.WEST);
//
//		add(pane,BorderLayout.NORTH);
//		add(resultMessage,BorderLayout.SOUTH);
//		scrollpane1 = new JScrollPane(table,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		add(scrollpane1,BorderLayout.CENTER);
//
//
//	}

	public void setResult(JComponent comp){
		add(comp,BorderLayout.CENTER); 
	}
	
	public void setMessage(){
		resultMessage.setText("结果出彩");
	}
	/**
	 * 设置执行结果信息
	 * @param loginInfo 
	 * @param resultSet 返回结果信息
	 * @throws SQLException
	 */
	public void setResultInfo(LoginInfo loginInfo, Table resultSet){
		add(scrollpane1);
		table.removeAll();
		
		table.setFont(new Font("宋体", Font.PLAIN, 15));
//		old_table.removeAll();
		table.setTableName(resultSet.getTableCode());
		Column[] columns = resultSet.getColumns();
		
		String pdm_show = HelpProperties.GetValueByKey("keyvalue.properties", "pdm_show");
		List<Cell> columnValus;
		for (int i = 0; i < columns.length; i++) {
			columnValus = resultSet.getColumnValus(columns[i]);
			if("Y".equals(pdm_show)){
				PDColumnEntity table = PowerDesigner.initPowerDesigner().getTable(loginInfo,resultSet.getTableCode(), columns[i].getColumnCode());
				columns[i].setColumnName(table.getColumnComment());
			}
			table.addColumAndData(columns[i], columnValus.toArray(new Cell[0]));
		}
		int columnCount = table.getColumnCount();
		FontMetrics fontMetrics = table.getTableHeader().getFontMetrics(table.getFont());
		for (int i = 1; i <columnCount; i++) {
			int stringWidth = fontMetrics.stringWidth(table.getColumnName(i));
			table.getColumnModel().getColumn(i).setPreferredWidth(stringWidth);
			table.getColumnModel().getColumn(i).setMinWidth(100);
		}
		lineNumberHeaderView.setLineHeight(table.getRowHeight());
		scrollpane1.setRowHeaderView(lineNumberHeaderView);
	}
	
//	/**
//	 * 设置执行结果信息
//	 * @param loginInfo 
//	 * @param resultSet 返回结果信息
//	 * @throws SQLException
//	 */
//	@SuppressWarnings("deprecation")
//	public void setResultLog(LoginInfo loginInfo, String log) throws SQLException{
//		this.log.append(new Date(System.currentTimeMillis()).toLocaleString()+" :"+log);
//	}
//	
	/**
	 * 得到结果集中所有即将更新的数据信息
	 * @return
	 * @throws CloneNotSupportedException 
	 */
	public Table getUpdateData() throws CloneNotSupportedException{
//		table.getColumnName(column)
		Table udateTable = new Table();
		udateTable.setTableCode(table.getTableName());
		Column[] columnCount = table.getHeaderColumns();
		for (int i = 0; i < columnCount.length; i++) {
			udateTable.addColumn(columnCount[i]);
		}
		int rowCount = table.getRowCount();
		for (int j= 0;j < rowCount;j++) {
			Cell edit = (Cell) table.getValueAt(j, 0);
			for (int i = 0; i < columnCount.length&&(edit!=null&&((UITable.EDIT.equals(edit.getNew_value())||UITable.ADD.equals(edit.getNew_value())||UITable.DELETE.equals(edit.getNew_value())))); i++) {
				udateTable.addColumnValue(i, (Cell) table.getValueAt(j, i));
			}
		}
		return udateTable;
	}
	/**
	 * 准备删除行
	 */
	public void prepDelete(){
		int[] selectedRows = table.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			Cell cell = new Cell();
			cell.setNew_value(UITable.DELETE);
			table.setValueAt(cell, selectedRows[i],0);
		}
	}
	
	private Object setPrepare(String object ,int type){
		
		Object o = "";

		if(object==null||"".equals(object)){
			o = null;
			return o;
		}
		switch(type){
		
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
		default:
			o = object;break;
		case Types.BIT:
			o =  new Boolean(object);break;
		case Types.NUMERIC:
			o = new BigDecimal(object);break;
		case Types.SMALLINT:
			o = new  Short(object);break;
		case Types.INTEGER:
			o = new  BigDecimal(object);break;
		case Types.BIGINT:
			o = new  Long( object);break;
		case Types.REAL:
		case Types.FLOAT:
			o = new  Float( object);break;
		case Types.DOUBLE:
			o = new  Double( object);break;
		case Types.VARBINARY:
		case Types.BINARY:
			o = new Byte(  object);break;
		case Types.DATE:
			o =  Date.valueOf(object);break;
		case Types.TIME:
			o = Time.valueOf( object);break;
		case Types.TIMESTAMP:
			o =   Timestamp.valueOf( object);break;
//		case Types.CLOB:
//			o = Clob( object);break;
//		case Types.BLOB:
//			o = new  Blob( object);break;
//		case Types.ARRAY:
//			o = new  Array( object);break;
//		case Types.REF:
//			o = new  Ref( object);break;
//		case Types.STRUCT:
//			o = new  Ref( object);break;
		}
		return o;
	}
	
	public void stopCellEditing(){
		TableCellEditor cellEditor = table.getCellEditor();
		if(cellEditor!=null){
			table.getCellEditor().stopCellEditing();
		}
	}
	
	/**
	 * 设置执行结果信息
	 * @param resultSet
	 * @throws SQLException
	 */
	public void setResultInfo(JTextPane label){
		add(label);
	}


	
	/**
	 * 添加一行数据
	 */
	public void prepAdd(){
		table.addRowNull(table.getSelectedRow()+1);
	}

	
	public void setStartEdit(){
		table.setEnabled(true);
//		table.getRowSorter().
	}
	
	public void setStopEdit(){
		table.setEnabled(false);
	}

	public void delete() {
		int selectedRows = table.getRowCount();
		for (int i = 0; i < selectedRows; i++) {
			Object valueAt = table.getValueAt(i, 0);
			if(valueAt!=null&&UITable.DELETE.equals(valueAt.toString())){
				table.remove(i);
			}
		}
	}
	
	public StringBuffer[] getInsertSql(){
		Column[] columns = table.getHeaderColumns();
		String tableName = table.getTableName();
		StringBuffer val =new StringBuffer("INSERT INTO "+tableName.toUpperCase()+"(");
		for (int i = 1; i <columns.length-1; i++) {
			val.append(columns[i]+",");
		}
		val.append(columns[columns.length-1]+") VALUES (");
		StringBuffer[] sqls = new StringBuffer[table.getRowCount()];
		for (int i = 0; i < sqls.length; i++) {
			sqls[i] = new StringBuffer(val);
			for (int j = 1; j < columns.length; j++) {
				Cell valueAt = (Cell) table.getValueAt(i, j);
					if(valueAt.getOld_value()==null){
						sqls[i].append("null");
					}else{
						sqls[i].append("'");
						sqls[i].append(valueAt.getOld_value());
						sqls[i].append("'");
					}
					sqls[i].append(",");
			}
			sqls[i].deleteCharAt(sqls[i].length()-1);
			sqls[i].append(")");
		}
		
		return sqls;
	}

	public ArrayList<String[]> getExcel() {
		Column[] columns = table.getHeaderColumns();
		ArrayList<String[]> arr = new ArrayList<String[]>();
		String[] values = new String[columns.length-1];
		for (int i = 1; i <columns.length; i++) {
			values[i-1] = columns[i].getColumnCode();
		}
		arr.add(values);
		int rowCount = table.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			values = new String[columns.length-1];
			for (int j = 1; j < columns.length; j++) {
				Cell valueAt = (Cell) table.getValueAt(i, j);
					if(valueAt.getOld_value()==null){
						values[j-1] = "";
					}else{
						values[j-1] = valueAt.getOld_value().toString();
					}
			}
			arr.add(values);
		}
		return arr;
	}

	
	public String getTableName() {
		return table.getTableName();
	}
}

class Sort extends RowSorter{

	@Override
	public Object getModel() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void toggleSortOrder(int column) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public int convertRowIndexToModel(int index) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int convertRowIndexToView(int index) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void setSortKeys(List keys) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public List getSortKeys() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public int getViewRowCount() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int getModelRowCount() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void modelStructureChanged() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void allRowsChanged() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void rowsInserted(int firstRow, int endRow) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void rowsDeleted(int firstRow, int endRow) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void rowsUpdated(int firstRow, int endRow) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void rowsUpdated(int firstRow, int endRow, int column) {
		// TODO 自动生成的方法存根
		
	}
	
}

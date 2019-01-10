package com.view.movedata.exp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.dao.ConnectionPool;
import com.dao.entity.Column;
import com.dao.entity.Table;
import com.entity.LoginInfo;
import com.ui.MyTableColor;
import com.ui.tree.UITree;
import com.view.movedata.execut.ExpMsg;
import com.view.movedata.execut.MoveDataCtr;
import com.view.movedata.exp.entity.ColumnType;
import com.view.movedata.exp.entity.DataFromBean;
import com.view.movedata.exp.entity.EditCell;
import com.view.movedata.exp.entity.MoveDataMsg;
import com.view.movedata.exp.entity.OddColumnMsg;
import com.view.movedata.exp.ui.dialog.ConvertColumnDialog;
import com.view.sqloperate.Controller;
import com.view.system.dialog.WaringMsg;
import com.view.tool.HelpProperties;
import com.view.tool.HelpRslutToSql;

import main.SQLTool;


/**
 * 导出数据库数据
 * @author wanghhc
 */
public class MoveDataView extends JPanel{
	protected static final String columnType = null;
	private File file;
	private DefaultTableModel selectMode;
	private DefaultTableModel insertMode;

	private JTable insertTable;
	private JComboBox selectConnList;
	private JComboBox insertConnList;
	private MoveDataMsg odds ;
	private JTable selectTable;
	private DefaultTableModel impMode;
	private CardLayout cardLayout;
	private DefaultTableModel selectCol;
	private DefaultTableModel insertCol;
	private JTable selectColTable;
	private JTable insertColTable;
	private JTable editColTable ;
	// 导入对比表
	private JTable impTable;
	private JRadioButton colButD;
	private JRadioButton colButE;
	private ConvertColumnDialog dialog;
	private Label fromTableName;
	private Label toTableName;
	private JComboBox showType;
	
	private JFileChooser fileChooser = new JFileChooser("D:\\");

	private boolean isEdit;
	public MoveDataView(){
		init();
	}
	public void init(){
		cardLayout = new CardLayout();
		this.setLayout(cardLayout);
		showType = getShowType();
		JPanel butPanel = initBut();
		initList();
		JPanel main = new JPanel(new GridLayout(2,1));
		JPanel center = initTable();
		
		JPanel editPanel = initEdit();
		
		
		editPanel.add(butPanel,BorderLayout.NORTH);
		JPanel  bar1 = new JPanel();
		bar1.setBorder(BorderFactory.createLoweredBevelBorder()  );
		bar1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bar1.add(new JLabel("使用内存/总内存 :"));
		JLabel nc = new JLabel(" 0/0 ");
		editPanel.add(bar1,BorderLayout.SOUTH);
		main.add(center);
		main.add(editPanel);
		
		this.add(main,BorderLayout.CENTER);
		
		
		
		JPanel editColumnPanel = initEditCol();
		this.add("editOdd",editColumnPanel);
	}
	private JPanel initEditCol() {
		final DefaultTableModel mode = new DefaultTableModel(){
			@Override
			public void addRow(Object[] rowData) {
				MyTableColor my = (MyTableColor)insertColTable.getColumnModel().getColumn(0).getCellRenderer();
				MyTableColor my1 = (MyTableColor)insertColTable.getColumnModel().getColumn(1).getCellRenderer();
				MyTableColor my2 = (MyTableColor)insertColTable.getColumnModel().getColumn(2).getCellRenderer();
				my.put(rowData[2].toString());
				my1.put(rowData[2].toString());
				my2.put(rowData[2].toString());
				insertColTable.repaint();
				super.addRow(rowData);
			}

			
			@Override
			public void removeRow(int row) {
				MyTableColor my = (MyTableColor)insertColTable.getColumnModel().getColumn(0).getCellRenderer();
				MyTableColor my1 = (MyTableColor)insertColTable.getColumnModel().getColumn(1).getCellRenderer();
				MyTableColor my2 = (MyTableColor)insertColTable.getColumnModel().getColumn(2).getCellRenderer();
				my.remove(editColTable.getValueAt(row,2).toString());
				my2.remove(editColTable.getValueAt(row,2).toString());
				my1.remove(editColTable.getValueAt(row,2).toString());
				insertColTable.repaint();
				super.removeRow(row);
			}
			
			
//			@Override
//			public boolean isCellEditable(int row, int column) {
//				// TODO 自动生成的方法存根
//				return column==0?true:false;
//			}
		};
		mode.addColumn("构造类型");
		mode.addColumn("转换前字段");
		mode.addColumn("转换后字段");
		editColTable = new JTable(mode);
		

		
		
		TableColumn column = editColTable.getColumnModel().getColumn(0);
		TableColumn edit = editColTable.getColumnModel().getColumn(1);
		JComboBox jComboBox = new JComboBox(new Object[]{new ColumnType(ColumnType.DEFULTCOLUMN),new ColumnType(ColumnType.FINAL),new ColumnType(ColumnType.NOW_TIME),new ColumnType(ColumnType.GROUPCOLUMN),new ColumnType(ColumnType.TEST)});
		edit.setCellRenderer(new EditCell());
		jComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editColTable.setValueAt("", editColTable.getSelectedRow(), 1);
			}
		});
		edit.setCellEditor(new MyButtonEditor(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ColumnType columnTyoe = (ColumnType) editColTable.getValueAt(editColTable.getSelectedRow(), 0);
					String type = columnTyoe.getType();
					if(ColumnType.DEFULTCOLUMN.equals(columnTyoe.getType())) return ;
					dialog =new ConvertColumnDialog(null, "编辑");
					
					Object showDialog = dialog.showDialog(columnTyoe.getType(),editColTable.getValueAt(editColTable.getSelectedRow(), 1));
					if(showDialog!=null){		
						if(impTable.getCellEditor()!=null){
							impTable.getCellEditor().stopCellEditing();
						}					
						editColTable.setValueAt(showDialog, editColTable.getSelectedRow(), 1);		
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}));
		DefaultCellEditor defa =new DefaultCellEditor(jComboBox);
		column.setCellEditor(defa);
		
		JScrollPane impScrol = new JScrollPane(editColTable,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel main = new JPanel(new GridLayout(2,1));
		JPanel center = initColumn();
		main.add(center);
		
		JPanel editCol = new JPanel(new BorderLayout());
		JPanel but = initColBut();
		editCol.add(but,BorderLayout.NORTH);
		editCol.add(impScrol,BorderLayout.CENTER);
		
	
		main.add(editCol);
		
		return main;
	}
	private JPanel initColBut() {
		JPanel butColLeftBut = new JPanel(new FlowLayout(FlowLayout.LEFT));
		 colButD =new JRadioButton("默认",true);
		 colButE =new JRadioButton("编辑");

		ButtonGroup  group = new ButtonGroup();
		group.add(colButD);
		group.add(colButE);

		butColLeftBut.add(colButD);
		butColLeftBut.add(colButE);

		JPanel bordPanel = new JPanel(new BorderLayout());
		bordPanel.add(butColLeftBut,BorderLayout.WEST);
		JPanel butColPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bordPanel.add(butColPanel,BorderLayout.EAST);

		JButton  add = new JButton("添加");
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int srow = selectColTable.getSelectedRow();
				int[] trow = insertColTable.getSelectedRows();
//				int trow = insertColTable.getSelectedRow();
				if(colButE.isSelected()){
					for (int i = 0; i < trow.length; i++) {
						int j = trow[i];
						((DefaultTableModel)editColTable.getModel()).addRow(new Object[]{new ColumnType(ColumnType.DEFULTCOLUMN),"",insertColTable.getValueAt(j, 0).toString()});
					}
					return;
				}
				if(srow==-1||trow.length==0){
					return ;
				}else{
					for (int i = 0; i < trow.length; i++) {
						int j = trow[i];
						((DefaultTableModel)editColTable.getModel()).addRow(new Object[]{new ColumnType(ColumnType.DEFULTCOLUMN),selectColTable.getValueAt(srow, 0).toString(),insertColTable.getValueAt(j, 0).toString()});
					}
				}
			}
		});
		JButton  delt = new JButton("删除");
		delt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(editColTable.getSelectedRow()==-1){
					return;
				}
				int[] selectedRows = editColTable.getSelectedRows();
				for (int i = 0; i < selectedRows.length; i++) {
					((DefaultTableModel)editColTable.getModel()).removeRow(selectedRows[i]-i);				
				}
			}
		});
		JButton  canl = new JButton("取消");
		canl.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.previous(MoveDataView.this);
			}
		});
		JButton  save = new JButton("保存");
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th = new Thread(){
					@Override
					public void run() {
						int rowCount = editColTable.getRowCount();
						final ExpMsg exp = new ExpMsg();
						if(((LoginInfo) selectConnList.getSelectedItem()).getDataType().equals("random")){
							exp.setMax(odds.getRowCount()); 
						}else{
							cardLayout.first(MoveDataView.this);
							int tablesRow = 0;
							int type = odds.getType();
							OddColumnMsg odd;
							ArrayList<OddColumnMsg> list = new ArrayList<OddColumnMsg>();
							for (int i = 0; i < rowCount; i++) {
								odd = new OddColumnMsg((ColumnType) editColTable.getValueAt(i, 0),editColTable.getValueAt(i, 1), editColTable.getValueAt(i, 2).toString());
								list.add(odd);
							}
							odds.setOdd(list);
							if(isEdit){
								impTable.setValueAt(odds, impTable.getSelectedRow(), 4);
								impTable.setValueAt(exp, impTable.getSelectedRow(), 3);
								impTable.setValueAt(odds.getInsertTableName(), impTable.getSelectedRow(), 2);
							}else{
								if(odds.getType()==MoveDataMsg.FROM_DATA_TABLE){
									((DefaultTableModel)impTable.getModel()).addRow(new Object[]{false,selectTable.getValueAt(selectTable.getSelectedRow(),0 ).toString(),insertTable.getValueAt(insertTable.getSelectedRow(),0).toString(),exp,odds,false,true});
								}else if(odds.getType()==MoveDataMsg.FROM_DATA_RANDOM){
									((DefaultTableModel)impTable.getModel()).addRow(new Object[]{false,"制造数据",insertTable.getValueAt(insertTable.getSelectedRow(),0).toString(),exp,odds});
								}else if(odds.getType()==MoveDataMsg.FROM_DATA_SQL){
									((DefaultTableModel)impTable.getModel()).addRow(new Object[]{false,odds.getSelectTableName(),insertTable.getValueAt(insertTable.getSelectedRow(),0).toString(),exp,odds,false,true});
								}
							}
//								if(MoveDataMsg.FROM_DATA_SQL==type){
//									tablesRow = Controller.newController().getTablesRowFromSql((LoginInfo) selectConnList.getSelectedItem(), ((DataFromBean)odds.getSelectTableName()).getSql());
//								}else{
							
						}
						
					}
				};
				th.start();
			}
		});

		butColPanel.add(add);
		butColPanel.add(delt);
		butColPanel.add(save);
		butColPanel.add(canl);

		return bordPanel;
	}
	
	private void loadRows(ExpMsg exp,MoveDataMsg odds,String sql){
		Thread th = new Thread(){
			@Override
			public void run() {
				try {
					int tablesRow;
					synchronized (exp) {
						int max = exp.getMax();
						if(max==-1){
							return;
						}
					}
					exp.setMax(-1);
					exp.setInitValue(0);
					impTable.repaint();;
					tablesRow = Controller.newController().getTablesRowFromSql((LoginInfo) selectConnList.getSelectedItem(),sql);
					exp.setMax(tablesRow); 
					exp.setStartDate("00:00:00");
					exp.setEndDate("00:00:00");
					impTable.repaint();;
				} catch (Exception e) {
					exp.setMax(-2);
					e.printStackTrace();
				}
			}
		};
		AsynchronousBathLoadDataBase.getAsynchronousBathLoadDataBase().submit(th);

	}
	private JPanel initColumn() {
		selectCol = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		insertCol = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		selectColTable = new JTable(selectCol);
		insertColTable = new JTable(insertCol);
		
		
		insertColTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int rowCount = selectColTable.getRowCount();
				if(insertColTable.getSelectedRow()==-1){
					return;
				}
				String string = insertColTable.getValueAt(insertColTable.getSelectedRow(), 0).toString();
				for (int i = 0; i < rowCount; i++) {
					if(!"".equals(string)&&string.equals(selectColTable.getValueAt(i, 0).toString())){
						selectColTable.setRowSelectionInterval(i, i);
						selectColTable.changeSelection(i,1,false,false);
						selectColTable.grabFocus();
					}
				}
			}
		});
		

		selectCol.addColumn("字段名");
		selectCol.addColumn("字段类型");
		selectCol.addColumn("字段长度");
		insertCol.addColumn("字段名");
		insertCol.addColumn("字段类型");
		insertCol.addColumn("字段长度");

		JScrollPane selectScrol = new JScrollPane(selectColTable,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		MouseWheelListener[] mouseWheelListeners = selectScrol.getMouseWheelListeners();
		JScrollPane insertScrol = new JScrollPane(insertColTable,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		for (int i = 0; i < insertScrol11.length; i++) {
//			insertScrol.removeMouseWheelListener(insertScrol11[i]);
//		}
		for (int i = 0; i < mouseWheelListeners.length; i++) {
			insertScrol.addMouseWheelListener(mouseWheelListeners[i]);
		}
		JPanel center = new JPanel(new GridLayout(1,2));


		JPanel selectCenter = new JPanel(new BorderLayout());
		JPanel insertCenter = new JPanel(new BorderLayout());
		
		fromTableName = new Label("A");
		toTableName = new Label("B");
		selectCenter.add(fromTableName,BorderLayout.NORTH);
		insertCenter.add(toTableName,BorderLayout.NORTH);
		selectCenter.add(selectScrol,BorderLayout.CENTER);
		insertCenter.add(insertScrol,BorderLayout.CENTER);
		center.add(selectCenter);
		center.add(insertCenter);
		insertColTable.getColumnModel().getColumn(0).setCellRenderer(new MyTableColor()); 
		insertColTable.getColumnModel().getColumn(1).setCellRenderer(new MyTableColor()); 
		insertColTable.getColumnModel().getColumn(2).setCellRenderer(new MyTableColor()); 

		return center;
	}
	
	public JComboBox getShowType() {
		JComboBox selectConnList = new JComboBox();	
		selectConnList.removeAllItems();
		selectConnList.addItem("TABLE");
		selectConnList.addItem("SEQUENCE");
		return selectConnList;
	}
	private JPanel initBut() {
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton zd = new JButton("自动比对");
		JButton allChange = new JButton("全局变量");
		Action action = new Action();
		zd.addActionListener(action);
		allChange.addActionListener(action);
		JButton script = new JButton("编辑脚本");
		JButton edit = new JButton("修改比对");
		JButton delete = new JButton("撤销比对");
		JButton imp = new JButton("导入比对信息");
		JButton save = new JButton("保存比对信息");
		JButton begin = new JButton("开始迁移");
		JButton stop = new JButton("停止迁移");
		JButton updateRows = new JButton("更新行数");
		script.addActionListener(action);
		begin.addActionListener(action);
		updateRows.addActionListener(action);
		stop.addActionListener(action);
		delete.addActionListener(action);
		edit.addActionListener(action);
		save.addActionListener(action);
		imp.addActionListener(action);
		
//		butPanel.add(showType);
		butPanel.add(script);
		butPanel.add(allChange);
		butPanel.add(zd);
		butPanel.add(edit);
		butPanel.add(delete);
		butPanel.add(imp);
		butPanel.add(save);
		butPanel.add(begin);
		butPanel.add(stop);
		butPanel.add(updateRows);
		return butPanel;
	}
	private JPanel initEdit() {
		JPanel editPanel = new JPanel(new BorderLayout());
		impMode = new DefaultTableModel(){
			
			
			@Override
			public void addRow(Object[] rowData) {
				MyTableColor my = (MyTableColor)insertTable.getColumnModel().getColumn(0).getCellRenderer();
				MyTableColor my1 = (MyTableColor)insertTable.getColumnModel().getColumn(1).getCellRenderer();
				my.put(rowData[2].toString());
				my1.put(rowData[2].toString());
//				my2.put(rowData[2].toString());
				super.addRow(rowData);
				insertTable.repaint();
			}

			
			@Override
			public void removeRow(int row) {
				MyTableColor my = (MyTableColor)insertTable.getColumnModel().getColumn(0).getCellRenderer();
				MyTableColor my1 = (MyTableColor)insertTable.getColumnModel().getColumn(1).getCellRenderer();
				my.remove(impTable.getValueAt(row,2).toString());
				my1.remove(impTable.getValueAt(row,2).toString());
//				my1.remove(editColTable.getValueAt(row,2).toString());
				super.removeRow(row);
				insertTable.repaint();
			}
			
			@Override
			public boolean isCellEditable(int row, int column) {
////				MoveDataMsg md = (MoveDataMsg) impTable.getValueAt( row, 4);
////				if(md.getType()!=MoveDataMsg.FROM_DATA_SQL){
////					return false;
////				}
//				// TODO 自动生成的方法存根
				return column==0||column==5||column==6||column==7?true:false;
//				return false;
			}
		};
		impMode.addColumn("执行");
		impMode.addColumn("导出源");		
		impMode.addColumn("导入源");
		impMode.addColumn("进度");
		impMode.addColumn("Odd");
		impMode.addColumn("强制增量条件");
		impMode.addColumn("强制条件");
		impMode.addColumn("线程数");
		impTable = new JTable(impMode);
		impTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int rowCount = insertTable.getRowCount();
				if(impTable.getSelectedRow()<0){
					return;
				}
				String string = impTable.getValueAt(impTable.getSelectedRow(), 2).toString();
				for (int i = 0; i < rowCount; i++) {
					if(string.equals(insertTable.getValueAt(i, 0).toString())){
						insertTable.setRowSelectionInterval(i, i);
						insertTable.changeSelection(i,1,false,false);
						insertTable.grabFocus();
					}
				}				
			}
		});
		
		impTable.addMouseMotionListener(new MouseAdapter(){
			 public void mouseMoved(MouseEvent e) {
			        int row=impTable.rowAtPoint(e.getPoint());  
			        int col=impTable.columnAtPoint(e.getPoint());  
			        if(row>-1 && col>-1){
			        	MoveDataMsg moveDataMsg=(MoveDataMsg) impTable.getValueAt(row, 4);

			        	if(moveDataMsg.getType()==MoveDataMsg.FROM_DATA_SQL){
			        		Boolean isForceWhere = (Boolean) impTable.getValueAt(row, 6);
			        		String select = HelpRslutToSql.getSelect(moveDataMsg).toUpperCase();
			        		if(isForceWhere!=null&&isForceWhere){
			        			select=HelpRslutToSql.addForceWhere(select);
			        		}
							StringBuffer buf = formatSql(select);
			            	impTable.setToolTipText(buf.toString());//悬浮显示单元格
//			            	System.out.println(buf.toString());
//			        		impTable.setToolTipText(((MoveDataMsg)moveDataMsg.getSelectTableName()).get);
			        	}else if(moveDataMsg.getType()==MoveDataMsg.FROM_DATA_RANDOM){
			        		
			        	}else if(moveDataMsg.getType()==MoveDataMsg.FROM_DATA_TABLE){
			        		Boolean isForceWhere = (Boolean) impTable.getValueAt(row, 6);
			        		String select = HelpRslutToSql.getSelect(moveDataMsg);
			        		if(isForceWhere!=null&&isForceWhere){
			        			select=HelpRslutToSql.addForceWhere(select);
			        		}
			        		StringBuffer buf = formatSql(select);
			            	impTable.setToolTipText(buf.toString());//悬浮显示单元格内容  
//			            	System.out.println(buf.toString());

			        	}else{
			    			String select = HelpRslutToSql.getScriptSql(moveDataMsg);

							StringBuffer buf = formatSql(select);
			            	impTable.setToolTipText(buf.toString());//悬浮显示单元格内容  

			        	}
//			            	impTable.setToolTipText(value.toString());//悬浮显示单元格内容  
//			            	impTable.setToolTipText("");
//			            	impTable.setToolTipText("");
//			            else{
//			            	impTable.setToolTipText(null);//关闭提示  
//			            }  
			        }
			    }

				private StringBuffer formatSql(String select) {
					select = select.replaceAll("<","&lt;").replaceAll(">","&gt;");
					select = "<html>"+select;
					String replaceAll = select.replaceAll("SELECT", "SELECT <br>&emsp;&emsp;").replaceAll("WHERE", "<br>WHERE <br>&emsp;&emsp;").replaceAll("FROM","<br>FROM <br>&emsp;&emsp;");
					replaceAll+="</html>";
					int leng1 = 100;
					StringBuffer buf = new StringBuffer(replaceAll);
					for (int i = 1; i <=buf.length(); i++) {
						int g = buf.indexOf(",",buf.indexOf("<br>", leng1*i) +leng1*i);
						if(g==-1){
							continue;
						}
						buf.replace(g, g+1, " ,<br>&emsp;&emsp;");
					}
					return buf;
				}  
		});
		JScrollPane impScrol = new JScrollPane(impTable,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		TableColumn column = impTable.getColumnModel().getColumn(0);
		column.setMaxWidth(40);
		column.setPreferredWidth(40);
		column.setWidth(40);
		column.setMinWidth(40);
		column.setCellEditor(impTable.getDefaultEditor(Boolean.class));   
		column.setCellRenderer(impTable.getDefaultRenderer(Boolean.class));
		
		
		TableColumn zeng = impTable.getColumnModel().getColumn(5);
		zeng.setMaxWidth(80);
		zeng.setPreferredWidth(80);
		zeng.setWidth(80);
		zeng.setMinWidth(80);
		zeng.setCellEditor(impTable.getDefaultEditor(Boolean.class));   
		zeng.setCellRenderer(impTable.getDefaultRenderer(Boolean.class));
		
		
		TableColumn tong = impTable.getColumnModel().getColumn(6);
		tong.setMaxWidth(80);
		tong.setPreferredWidth(80);
		tong.setWidth(80);
		tong.setMinWidth(80);
		tong.setCellEditor(impTable.getDefaultEditor(Boolean.class));   
		tong.setCellRenderer(impTable.getDefaultRenderer(Boolean.class));
		
		TableColumn threads = impTable.getColumnModel().getColumn(7);
		threads.setMaxWidth(60);
		threads.setPreferredWidth(60);
		
		TableColumn jpro = impTable.getColumnModel().getColumn(3); 
		jpro.setCellRenderer((new MyProgressBarRenderer()));

		editPanel.add(impScrol,BorderLayout.CENTER);
		return editPanel;
	}
	private JPanel initTable() {
		selectMode = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		insertMode = new DefaultTableModel(){
			
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		selectTable = new JTable(selectMode);
		insertTable = new JTable(insertMode);
		insertTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int rowCount = selectTable.getRowCount();
				if(insertTable.getSelectedRow()==-1){
					return;
				}
				String string = insertTable.getValueAt(insertTable.getSelectedRow(), 0).toString();
				for (int i = 0; i < rowCount; i++) {
					if((string.split("\\.").length>1?string.split("\\.")[1]:string).equals(selectTable.getValueAt(i, 0).toString())||((selectTable.getValueAt(i, 0).toString().split("\\.").length>1&&string.equals(selectTable.getValueAt(i, 0).toString().split("\\.")[1])))||string.equals(selectTable.getValueAt(i, 0).toString())||((selectTable.getValueAt(i, 0).toString().split("\\.").length>1&&string.equals(selectTable.getValueAt(i, 0).toString().split("\\.")[1])))){
						selectTable.setRowSelectionInterval(i, i);
						selectTable.changeSelection(i,1,false,false);
						selectTable.grabFocus();
					}
				}				
			}
		});
	
		selectMode.addColumn("对象名");
		selectMode.addColumn("存在数据量");
		insertMode.addColumn("对象名");
		insertMode.addColumn("存在数据量");
		insertTable.getColumnModel().getColumn(0).setCellRenderer(new MyTableColor()); 
		insertTable.getColumnModel().getColumn(1).setCellRenderer(new MyTableColor()); 
		JScrollPane selectScrol = new JScrollPane(selectTable,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JScrollPane insertScrol = new JScrollPane(insertTable,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		MouseWheelListener[] mouseWheelListeners = selectScrol.getMouseWheelListeners();
//		for (int i = 0; i < insertScrol11.length; i++) {
//			insertScrol.removeMouseWheelListener(insertScrol11[i]);
//		}
		for (int i = 0; i < mouseWheelListeners.length; i++) {
			insertScrol.addMouseWheelListener(mouseWheelListeners[i]);
		}
		
		
		JPanel center = new JPanel(new GridLayout(1,2));
		JPanel selectCenter = new JPanel(new BorderLayout());
		JPanel insertCenter = new JPanel(new BorderLayout());
		
		selectCenter.add(selectConnList,BorderLayout.NORTH);
		insertCenter.add(insertConnList,BorderLayout.NORTH);
		selectCenter.add(selectScrol,BorderLayout.CENTER);
		insertCenter.add(insertScrol,BorderLayout.CENTER);
		center.add(selectCenter);
		center.add(insertCenter);
		return center;
	}
	private void initList() {
		selectConnList = getDrowDown();
		insertConnList = getDrowDown();
		insertConnList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					changeInsertlistConnection();
				}
			}

			
		});
		selectConnList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					final LoginInfo  log = (LoginInfo) selectConnList.getSelectedItem();
					clear(selectMode);
					if(log.getIsLive()==ConnectionPool.IS_DIE){
						return ;
					}
					Thread th = new Thread(){
						public void run() {
							try {
								selectConnList.setEnabled(false);
								selectMode.addRow(new Object[]{"数据加载中...",""});
								ArrayList<Table> tablesa = Controller.newController().getResTables(log);
								Collections.sort(tablesa,new Comparator<Table> (){
									@Override
									public int compare(Table o1, Table o2) {
										return o1.getTableCode().compareTo(o2.getTableCode());
//										return 0;
									}
								});
								MoveDataView.this.clear(selectMode); 

								ArrayList<String> tableNames = new ArrayList<String>();
								boolean count = "Y".equals(HelpProperties.GetValueByKey("keyvalue.properties", "countEachTable"));
								int i = 0;
								for ( i = 0; i < tablesa.size(); i++) {
									tableNames.add(tablesa.get(i).getTableCode());
									selectMode.addRow(new Object[]{tablesa.get(i).getTableCode(),"统计数据中"});
									if(count){
										int tablesRow = Controller.newController().getTablesRow(log, tablesa.get(i).getTableCode(),count);
										selectMode.setValueAt(tablesRow,i, 1);
									}
								}
								filtCols(selectMode,insertMode);

//								for (int i = 0; i < tablesa.size(); i++) {
//									selectMode.addRow(new Object[]{tablesa.get(i).getTableName(),tablesRow.get(tableNames.get(i))});
//								}

							} catch (InterruptedException e1) {
								e1.printStackTrace();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
							finally{
								selectConnList.setEnabled(true);

							}
						};
					};
					th.start();

					
				}
			}
		});
	}
	private void changeInsertlistConnection() {
		final LoginInfo  log = (LoginInfo) insertConnList.getSelectedItem();
		MoveDataView.this.clear(insertMode); 
		if(log.getIsLive()==ConnectionPool.IS_DIE){
			return ;
		}
		Thread th = new Thread(){
			@Override
			public void run() {
//				final String  showT = (String) showType.getSelectedItem();
//				if("TABLE".equals(showT)){
					try {
						insertMode.addRow(new Object[]{"数据加载中...",""});
						insertConnList.setEnabled(false);
						ArrayList<Table> tablesa = Controller.newController().getResTables(log);
						Collections.sort(tablesa,new Comparator<Table> (){
							@Override
							public int compare(Table o1, Table o2) {
								return o1.getTableCode().compareTo(o2.getTableCode());
//								return 0;
							}
						});
						MoveDataView.this.clear(insertMode); 
						ArrayList<String> tableNames = new ArrayList<String>();
						boolean count = "Y".equals(HelpProperties.GetValueByKey("keyvalue.properties", "countEachTable"));
						for (int i = 0; i < tablesa.size(); i++) {
							tableNames.add(tablesa.get(i).getTableCode());
							insertMode.addRow(new Object[]{tablesa.get(i).getTableCode(),"统计数据中"});
							if(count){
								int tablesRow = Controller.newController().getTablesRow(log, tablesa.get(i).getTableCode(),count);
								insertMode.setValueAt(tablesRow,i, 1);
							}
						}
						filtCols(selectMode,insertMode);
						
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					finally{
						insertConnList.setEnabled(true);
					}
//				}else{
//					try {
//						insertMode.addRow(new Object[]{"数据加载中...",""});
//						insertConnList.setEnabled(false);
//						ArrayList<Table> tablesa = Controller.newController().getResSequence (log);
//						Collections.sort(tablesa,new Comparator<Table> (){
//							@Override
//							public int compare(Table o1, Table o2) {
//								return o1.getTableCode().compareTo(o2.getTableCode());
////								return 0;
//							}
//						});
//						MoveDataView.this.clear(insertMode); 
//						ArrayList<String> tableNames = new ArrayList<String>();
//						for (int i = 0; i < tablesa.size(); i++) {
//							tableNames.add(tablesa.get(i).getTableCode());
//							insertMode.addRow(new Object[]{tablesa.get(i).getTableCode(),"完成"});
//						}
//						filtCols(selectMode,insertMode);
//						
//					} catch (InterruptedException e1) {
//						e1.printStackTrace();
//					} catch (SQLException e1) {
//						e1.printStackTrace();
//					}
//					finally{
//						insertConnList.setEnabled(true);
//					}
//				}
			}
		};
		th.start();
	}

	private JPanel getChooseExpType() {
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,10));
		panel1.setBorder( BorderFactory.createTitledBorder("导出格式"));
		JCheckBoxMenuItem tableStructure = new JCheckBoxMenuItem("导出表结构");
		JCheckBoxMenuItem tableData = new JCheckBoxMenuItem("导出表数据");
		JCheckBoxMenuItem views = new JCheckBoxMenuItem("导出视图");
		JCheckBoxMenuItem procedures = new JCheckBoxMenuItem("导出存储过程");
		JCheckBoxMenuItem functions = new JCheckBoxMenuItem("导出方法");
		JCheckBoxMenuItem indexs = new JCheckBoxMenuItem("导出索引");
		
		JCheckBox box = new JCheckBox();
		box.add(tableStructure);
		box.add(tableData);
		box.add(views);
		box.add(procedures);
		box.add(functions);
		box.add(indexs);
		panel1.add(tableStructure);
		panel1.add(tableData);
		panel1.add(views);
		panel1.add(procedures);
		panel1.add(functions);
		panel1.add(indexs);
		return panel1;
	}
	
	public void initData(){
		
	}
	
	public void addRows(Object[] rowData){
		selectMode.addRow(rowData);
	}
	public void clear(DefaultTableModel mode){
		int rowCount = mode.getRowCount();
		for (int i = 0; i < rowCount&&rowCount!=-1; i++) {
			mode.removeRow(0);
		}
	}
	
	
	public JComboBox getDrowDown() {
		JComboBox selectConnList = new JComboBox();	
		selectConnList.removeAllItems();
		UITree dataBaseTree = SQLTool.getSQLTool().getToolFrame().getSqlView()
				.getDataBaseTree();
		DefaultTreeModel model = (DefaultTreeModel) dataBaseTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		int childCount = root.getChildCount();
		LoginInfo[] please = new LoginInfo[childCount];
		LoginInfo login = new LoginInfo();
		login.setName("---请选择/随机数据---");
		login.setDataType("random");
		selectConnList.addItem(login);

		LoginInfo conn;
		for (int i = 0; i < childCount; i++) {
			DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) root
					.getChildAt(i);
			please[i] = (LoginInfo) childAt.getUserObject();
		}
		Arrays.sort(please,  new Comparator<LoginInfo>(){

			@Override
			public int compare(LoginInfo o1, LoginInfo o2) {
				int i = o1.getIsLive()>o2.getIsLive()==true?1:-1;
				return i;
			}
			
		});
		for (int i = 0; i < childCount; i++) {
			selectConnList.addItem(please[i]);
		}
		
		return selectConnList;
	}
	
	
	class Action implements ActionListener{

		private static final int TABLENAME = 0;

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			String name = source.getText();
			
			if("全局变量".equals(name)){
				ConvertColumnDialog dialog =new ConvertColumnDialog(null, "全局变量设定",false);
				try {
					Boolean showDialog = (Boolean) dialog.showDialog(ColumnType.ALL_CHANGE,"");
					while(showDialog!=null&&!showDialog){
						if(!showDialog){
							dialog =new ConvertColumnDialog(null, "全局变量设定",false);
							showDialog = (Boolean) dialog.showDialog(ColumnType.ALL_CHANGE,"");
						}else{
							break;
						}
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if("更新行数".equals(name)){
					int[] i = impTable.getSelectedRows();
					if(i.length==0){
						return;
					}
					for (int j = 0; j < i.length; j++) {
						Boolean b = (Boolean) impTable.getValueAt(i[j], 6);
						MoveDataMsg odds = (MoveDataMsg) impTable.getValueAt(i[j], 4);
						if(3==odds.getType()){
							((ExpMsg)impTable.getValueAt(i[j], 3)).setStartDate("00:00:00");
							((ExpMsg)impTable.getValueAt(i[j], 3)).setEndDate("00:00:00");
							((ExpMsg)impTable.getValueAt(i[j], 3)).setInitValue(0);;
							impTable.repaint();
							continue;
						}
						if(b)
							loadRows((ExpMsg)impTable.getValueAt(i[j], 3),(MoveDataMsg)impTable.getValueAt(i[j], 4),HelpRslutToSql.addForceWhere( HelpRslutToSql.getSelect(odds)));
						else{
							loadRows((ExpMsg)impTable.getValueAt(i[j], 3),(MoveDataMsg)impTable.getValueAt(i[j], 4),HelpRslutToSql.getSelect(odds));
						}
					}
			}
			if("停止迁移".equals(name)){
				for (int i = 0; i <impTable.getRowCount() ; i++) {
					MoveDataMsg mdm = (MoveDataMsg)impTable.getValueAt(i, 4);
					mdm.setRun(false);
				}
			}
			if("开始迁移".equals(name)){
				for (int i = 0; i <impTable.getRowCount() ; i++) {
					Boolean b = (Boolean) impTable.getValueAt(i, 0);
					if(b){
						MoveDataMsg mdm = (MoveDataMsg)impTable.getValueAt(i, 4);
						if(mdm.isRun()){
							WaringMsg.showMessageDialog("正在迁移中，请勿重复迁移。");
//							return;	
						}
						mdm.setRun(true); 

						if(impTable.getValueAt(i, 7)!=null&&impTable.getValueAt(i, 7).toString().matches("\\d*")){
							int count = Integer.valueOf(impTable.getValueAt(i, 7).toString());
							if(count<=0){
								WaringMsg.showMessageDialog("请输入执行线程数量");
								return;
							}
							mdm.setThreads(count);
						}else{
							WaringMsg.showMessageDialog("请输入执行线程数量");
							return ;
						}
						
						if(((MoveDataMsg)impTable.getValueAt(i, 4)).getType()==MoveDataMsg.FROM_DATA_TABLE){
							MoveDataCtr ctr = new MoveDataCtr((LoginInfo)selectConnList.getSelectedItem(),(LoginInfo)insertConnList.getSelectedItem(),mdm,impTable,i);
						}
						if(((MoveDataMsg)impTable.getValueAt(i, 4)).getType()==MoveDataMsg.FROM_DATA_RANDOM){
							MoveDataCtr ctr = new MoveDataCtr((LoginInfo)insertConnList.getSelectedItem(),mdm,impTable,i);
						}
						if(((MoveDataMsg)impTable.getValueAt(i, 4)).getType()==MoveDataMsg.FROM_DATA_SQL){
							MoveDataCtr ctr = new MoveDataCtr((LoginInfo)selectConnList.getSelectedItem(),(LoginInfo)insertConnList.getSelectedItem(),mdm,impTable,i);
						}
						if(((MoveDataMsg)impTable.getValueAt(i, 4)).getType()==3){
							MoveDataCtr ctr = new MoveDataCtr((LoginInfo)insertConnList.getSelectedItem(),mdm,impTable,i,"");
							ctr.setStartDate();
						}
					}
				}
			}
			if("撤销比对".equals(name)){
				if(impTable.getSelectedRow()==-1){
					return;
				}
				int[] selectedRows = impTable.getSelectedRows();
				for (int i = 0; i < selectedRows.length; i++) {
					((DefaultTableModel)impTable.getModel()).removeRow(selectedRows[i]-i);				
				}
			}
			if("修改比对".equals(name)){
				if(impTable.getSelectedRow()==-1){
					WaringMsg.showMessageDialog("请选择需要修改数据！");
					return;
				}
				int selectedRow2 = insertTable.getSelectedRow();
				if(selectedRow2==-1){
					WaringMsg.showMessageDialog("请选择目标表！");
					return;
				}
				Object valueAt2 = insertTable.getValueAt(selectedRow2, 0);
//				odds = new MoveDataMsg(MoveDataMsg.FROM_DATA_RANDOM,new Integer(showInputDialog));
				isEdit = true;
				MoveDataMsg valueAt = (MoveDataMsg) impTable.getValueAt(impTable.getSelectedRow(), 4);
				valueAt.setRun(false);
				if("".equals(valueAt2)){
					WaringMsg.showMessageDialog("请选择目标表！");
					return;
				}
				valueAt.setInsertTableName(HelpRslutToSql.addDefultWhere(valueAt2.toString()));
//				toTableName.setText(valueAt2.toString());

				odds = valueAt;
				if(valueAt.getType()==MoveDataMsg.FROM_DATA_RANDOM){
					String showInputDialog = WaringMsg.showInputDialog("请输入需要随机数量",MoveDataView.this);
					valueAt.setRowCount(new Integer(showInputDialog));
				}else if(valueAt.getType()==MoveDataMsg.FROM_DATA_SQL){
					if(ConnectionPool.IS_DIE == checkSelectConnection()){
						WaringMsg.showMessageDialog("请选择数据来源连接！");
						return ;
					}
					if(ConnectionPool.IS_DIE == checkInsertConnection()){
						WaringMsg.showMessageDialog("请选择数据转换连接！");
						return ;
					}
					int selectedRow = impTable.getSelectedRow();
					odds.setType(MoveDataMsg.FROM_DATA_SQL);
					dialog =new ConvertColumnDialog(null, "定制数据集");
					DataFromBean showDialog;
					try {
						showDialog = (DataFromBean) dialog.showDialog(ColumnType.CUSTOM_MADE,odds.getSelectTableName());
						if(showDialog!=null){	
							impTable.setValueAt(showDialog.toString(), impTable.getSelectedRow(), 1);
							fromTableName.setText(HelpRslutToSql.addDefultWhere(showDialog.toString()));	
							toTableName.setText(HelpRslutToSql.addDefultWhere(odds.getInsertTableName().toString()));
							if(!selectModel(showDialog)){
								return;
							}
						}
//						cardLayout.next(MoveDataView.this);
						insertModel();
					} catch (InterruptedException | SQLException e1) {
						e1.printStackTrace();
					}
					clear((DefaultTableModel) editColTable.getModel());

					int rowCount = insertColTable.getRowCount();
					int rowCount2 = selectColTable.getRowCount();
					for (int j = 0; j < rowCount2; j++) {
						for (int i = 0; i < rowCount; i++) {
							if(selectColTable.getValueAt(j, 0).toString().toUpperCase().equals((insertColTable.getValueAt(i, 0).toString().toUpperCase()))){
								((DefaultTableModel)editColTable.getModel()).addRow(new Object[]{new ColumnType(ColumnType.DEFULTCOLUMN),selectColTable.getValueAt(j, 0),insertColTable.getValueAt(i, 0).toString()});
							}
						}
					}
					try {
						filtCols((DefaultTableModel)insertColTable.getModel(),(DefaultTableModel)selectColTable.getModel());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else if(valueAt.getType()==MoveDataMsg.FROM_DATA_TABLE){
						
					fromTableName.setText(HelpRslutToSql.addDefultWhere(valueAt.getSelectTableName().toString()));
					toTableName.setText(HelpRslutToSql.addDefultWhere(valueAt.getInsertTableName().toString()));
					DefaultTableModel model = (DefaultTableModel) editColTable.getModel();
//					cardLayout.next(MoveDataView.this);

					clear(model);
					try {
						selectModel();
						insertModel();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					OddColumnMsg oddColumnMsg;
					for (int i = 0; i < valueAt.getPp().size(); i++) {
						oddColumnMsg = valueAt.getPp().get(i);
						model.addRow(new Object[]{new ColumnType(oddColumnMsg.getDefindMate().getType()),oddColumnMsg.getFcolumnName(),oddColumnMsg.getTcolumnName()});
						insertColTable.repaint();

					}
				}else{
					return;
				}
//				Boolean valueAt2 = (Boolean) impTable.getValueAt(impTable.getSelectedRow(), 5);
//				if(valueAt2){
//					WaringMsg.showMessageDialog("！");
//				}
				cardLayout.next(MoveDataView.this);
			}
			if("编辑脚本".equals(name)){
				if(ConnectionPool.IS_DIE == checkSelectConnection()){
					WaringMsg.showMessageDialog("请选择数据来源连接！");
					return ;
				}
				if(ConnectionPool.IS_DIE == checkInsertConnection()){
					WaringMsg.showMessageDialog("请选择数据转换连接！");
					return ;
				}
				DataFromBean showDialog;
				if(impTable.getSelectedRow()==-1){
					odds = new MoveDataMsg();
					odds.setRun(false);
//					odds.setType(MoveDataMsg.FROM_SCRIPT_SQL);
					odds.setType(3);
					dialog =new ConvertColumnDialog(null, "订制执行脚本");
					try {
//						showDialog = (DataFromBean) dialog.showDialog(ColumnType.CUSTOM_SCRIPT,null);
						showDialog = (DataFromBean) dialog.showDialog("订制执行脚本",null);
						if(showDialog!=null){	
							final ExpMsg exp = new ExpMsg();
							String[] split = showDialog.getSql().split(";");
							exp.setMax(split.length);
							odds.setInsertTableName(showDialog);
							((DefaultTableModel)impTable.getModel()).addRow(new Object[]{false,"订制执行脚本",odds.getInsertTableName(),exp,odds,false,true,split.length});
						}
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}else if(impTable.getSelectedRow()>=0){
					MoveDataMsg valueAt = (MoveDataMsg) impTable.getValueAt(impTable.getSelectedRow(), 4);
					if(3==valueAt.getType()){
						try {
							odds = new MoveDataMsg();
							odds.setRun(false);
							dialog =new ConvertColumnDialog(null, "订制执行脚本");
							odds = new MoveDataMsg();
							odds.setRun(false);

//							odds.setType(MoveDataMsg.FROM_SCRIPT_SQL);
							odds.setType(3);
							//showDialog = (DataFromBean) dialog.showDialog(ColumnType.CUSTOM_SCRIPT,valueAt.getInsertTableName());
							showDialog = (DataFromBean) dialog.showDialog("订制执行脚本",valueAt.getInsertTableName());
							if(showDialog!=null){	
								final ExpMsg exp = new ExpMsg();
								String[] split = showDialog.getSql().split(";");
								exp.setMax(split.length);
								odds.setInsertTableName(showDialog);
								impTable.setValueAt(odds, impTable.getSelectedRow(), 4);
								impTable.setValueAt(exp, impTable.getSelectedRow(), 3);
								impTable.setValueAt(odds.getInsertTableName(), impTable.getSelectedRow(), 2);
								impTable.setValueAt(split.length,impTable.getSelectedRow(), 7);
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						
					}else{
						WaringMsg.showMessageDialog("脚本类型非法！");
						return ;
					}

				}
				isEdit = false;
				

			}
			if("自动比对".equals(name)){
				if(ConnectionPool.IS_DIE == checkSelectConnection()){
					WaringMsg.showMessageDialog("请选择数据来源连接！");
					return ;
				}
				if(ConnectionPool.IS_DIE == checkInsertConnection()){
					WaringMsg.showMessageDialog("请选择数据转换连接！");
					return ;
				}
				isEdit = false;
				clear((DefaultTableModel) editColTable.getModel());
				int selectedRow = selectTable.getSelectedRow();
				int insertRow = insertTable.getSelectedRow();
				boolean compareColumns = compareColumns(selectedRow, insertRow);
				if(!compareColumns){
					return;
				}
				
				int rowCount = insertColTable.getRowCount();
				int rowCount2 = selectColTable.getRowCount();
//				HashMap<String, String> map = new HashMap<String, String>();
				for (int j = 0; j < rowCount2; j++) {
					for (int i = 0; i < rowCount; i++) {
						if(selectColTable.getValueAt(j, 0).toString().toUpperCase().equals((insertColTable.getValueAt(i, 0).toString().toUpperCase()))){
							((DefaultTableModel)editColTable.getModel()).addRow(new Object[]{new ColumnType(ColumnType.DEFULTCOLUMN),selectColTable.getValueAt(j, 0),insertColTable.getValueAt(i, 0).toString()});
						}
					}
				}
				try {
					filtCols((DefaultTableModel)insertColTable.getModel(),(DefaultTableModel)selectColTable.getModel());
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				cardLayout.next(MoveDataView.this);
			}
			
			if("保存比对信息".equals(name)){
//				((DefaultTableModel)impTable.getModel()).addRow(new Object[]{false,"制造数据",insertTable.getValueAt(insertTable.getSelectedRow(),0).toString(),exp,odds});
				if(impTable.getCellEditor()!=null){
					impTable.getCellEditor().stopCellEditing();
				}

				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY  );
				FileNameExtensionFilter filter = new FileNameExtensionFilter("rad","rad");//建立过滤器    
				fileChooser.setFileFilter(filter);//开始过滤    
				int showSaveDialog = fileChooser.showSaveDialog(MoveDataView.this);
				if (JFileChooser.APPROVE_OPTION == showSaveDialog) {
					file = null;
					
					File selectedFile = fileChooser.getSelectedFile();
					boolean isFile = selectedFile.isFile();
					if(!isFile){
						selectedFile = new File(selectedFile.getPath()+".rad");
					}
					ArrayList<Object[]>  row = new ArrayList<Object[]>();
					ObjectOutputStream  write = null;
						try {
							write = new ObjectOutputStream(new FileOutputStream(selectedFile)) ;
							int rowCount = impTable.getRowCount();
							int columnCount = impTable.getColumnCount();
							Object[] arr =null;
							for (int i = 0; i < rowCount; i++) {
								arr = new Object[columnCount];
								for (int j = 0; j < columnCount; j++) {
									arr[j]  = impTable.getValueAt(i, j);
								}
								row.add(arr);
							}
							write.writeObject(row);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						finally{
							if(write!=null){
								try {
									write.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
					}
			}
			if("导入比对信息".equals(name)){
//				((DefaultTableModel)impTable.getModel()).addRow(new Object[]{false,"制造数据",insertTable.getValueAt(insertTable.getSelectedRow(),0).toString(),exp,odds});
				
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );

				int showSaveDialog = fileChooser.showOpenDialog(null);
				if (JFileChooser.APPROVE_OPTION == showSaveDialog) {
					File selectedFile = fileChooser.getSelectedFile();
					ObjectInputStream  read = null;
//					if(file.getName().endsWith(".md")){
						try {
							read = new ObjectInputStream(new FileInputStream(selectedFile)) ;
							ArrayList<Object[]>   readObject = (ArrayList<Object[]>) read.readObject();
							for (int i = 0; i <readObject.size(); i++) {
								impMode.addRow(readObject.get(i));								
							}
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						finally{
							if(read!=null){
								try {
									read.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
					}
				}
//			}
		}

		
		private int checkSelectConnection(){
			LoginInfo loginfo = (LoginInfo)selectConnList.getSelectedItem();
			return loginfo.getIsLive();
		}
		private int checkInsertConnection(){
			LoginInfo loginfo = (LoginInfo)insertConnList.getSelectedItem();
			return loginfo.getIsLive();
		}
		/**
		 * 列名比对
		 * @param selectedRow
		 * @param insertRow
		 */
		private boolean  compareColumns(int selectedRow, int insertRow) {
			if(insertRow==-1){
				WaringMsg.showMessageDialog("请数据转换表！");
				return false;
			}else{
				odds = new MoveDataMsg();
				odds.setRun(false);

				if(selectedRow==-1){
					odds.setType(MoveDataMsg.FROM_DATA_SQL);
					dialog =new ConvertColumnDialog(null, "定制数据集");
					DataFromBean showDialog;
					try {
						showDialog = (DataFromBean) dialog.showDialog(ColumnType.CUSTOM_MADE,null);
						if(showDialog!=null){	
							fromTableName.setText(showDialog.toString());	
							toTableName.setText(insertTable.getValueAt(insertTable.getSelectedRow(), 0).toString());
							if(!selectModel(showDialog)){
								return false;
							}
							insertModel();
							return true;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return false;
				}
				try {
					fromTableName.setText(selectTable.getValueAt(selectedRow, TABLENAME).toString());
					toTableName.setText(insertTable.getValueAt(insertTable.getSelectedRow(), 0).toString());
					selectModel();
					insertModel();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			return true;
		}

		private void insertModel() throws InterruptedException,
				SQLException {
			odds.setInsertTableName(toTableName.getText());

//			String tTable = insertTable.getValueAt(insertRow, TABLENAME).toString();
			String length;
			clear((DefaultTableModel) insertColTable.getModel());
			ArrayList<Column> tcolumns = Controller.newController().getColumns((LoginInfo)insertConnList.getSelectedItem(), toTableName.getText());
			Collections.sort(tcolumns,new Comparator<Column> (){
				@Override
				public int compare(Column o1, Column o2) {
					return o1.getColumnCode().compareTo(o2.getColumnCode());
//					return 0;
				}
			});
			for (int i = 0; i < tcolumns.size(); i++) {
				length = tcolumns.get(i).getColumnSize()+"";
				if(tcolumns.get(i).getDataType()==Types.NUMERIC||tcolumns.get(i).getDataType()==Types.FLOAT||tcolumns.get(i).getDataType()==Types.DECIMAL||tcolumns.get(i).getDataType()==Types.DOUBLE){
					length=length+","+tcolumns.get(i).getColumnScaleSize();
				}
				((DefaultTableModel)insertColTable.getModel()).addRow(new Object[]{tcolumns.get(i).getColumnCode(),tcolumns.get(i).getColumnTypeName(),length});
			}
		}

		/**
		 * 初始化查询表字段名
		 * @param selectedRow
		 * @throws InterruptedException
		 * @throws SQLException
		 */
		private boolean  selectModel(DataFromBean fromBean) throws InterruptedException, SQLException {
			odds.setSelectTableName(fromBean);
//			String fTable = selectTable.getValueAt(selectedRow, TABLENAME).toString();
			ArrayList<Column> fcolumns = Controller.newController().getColumnsFromSql((LoginInfo)selectConnList.getSelectedItem(), HelpRslutToSql.addDefultWhere(fromBean.getSql()));
			if(fcolumns==null){
				WaringMsg.showMessageDialog("没有查到数据来源列名信息！");
				return false;
			}
			Collections.sort(fcolumns,new Comparator<Column> (){
				@Override
				public int compare(Column o1, Column o2) {
					return o1.getColumnCode().toUpperCase().compareTo(o2.getColumnCode().toUpperCase());
//					return 0;
				}
			});
			clear((DefaultTableModel) selectColTable.getModel());
			String length ="";
			for (int i = 0; i < fcolumns.size(); i++) {
				length = fcolumns.get(i).getColumnSize()+"";
				if(fcolumns.get(i).getDataType()==Types.NUMERIC||fcolumns.get(i).getDataType()==Types.FLOAT||fcolumns.get(i).getDataType()==Types.DECIMAL||fcolumns.get(i).getDataType()==Types.DOUBLE){
					length=length+","+fcolumns.get(i).getColumnScaleSize();
				}
				((DefaultTableModel)selectColTable.getModel()).addRow(new Object[]{fcolumns.get(i).getColumnCode(),fcolumns.get(i).getColumnTypeName(),length});
			}
			return true;
		}
		private void selectModel() throws InterruptedException, SQLException {
			odds.setSelectTableName(fromTableName.getText());
//			String fTable = selectTable.getValueAt(selectedRow, TABLENAME).toString();
			ArrayList<Column> fcolumns = Controller.newController().getColumns((LoginInfo)selectConnList.getSelectedItem(), fromTableName.getText());
			Collections.sort(fcolumns,new Comparator<Column> (){
				@Override
				public int compare(Column o1, Column o2) {
					return o1.getColumnCode().toUpperCase().compareTo(o2.getColumnCode().toUpperCase());
//					return 0;
				}
			});
			clear((DefaultTableModel) selectColTable.getModel());
			String length ="";
			for (int i = 0; i < fcolumns.size(); i++) {
				length = fcolumns.get(i).getColumnSize()+"";
				if(fcolumns.get(i).getDataType()==Types.NUMERIC||fcolumns.get(i).getDataType()==Types.FLOAT||fcolumns.get(i).getDataType()==Types.DECIMAL||fcolumns.get(i).getDataType()==Types.DOUBLE){
					length=length+","+fcolumns.get(i).getColumnScaleSize();
				}
				((DefaultTableModel)selectColTable.getModel()).addRow(new Object[]{fcolumns.get(i).getColumnCode(),fcolumns.get(i).getColumnTypeName(),length});
			}
		}
	}
	public void  filtCols(DefaultTableModel selectCol,DefaultTableModel insertCol) throws InterruptedException{
		int rowSelectCount = selectCol.getRowCount();
		int rowInsertCount = insertCol.getRowCount();
		ArrayList<String[]> slist = new ArrayList<>();
		ArrayList<String[]> ilist = new ArrayList<>();
		TreeSet<String> sset =new TreeSet<>();
		TreeSet<String> iset =new TreeSet<>();
		TreeSet<Val> set = new TreeSet<Val>(new Comparator<Val>() {

			@Override
			public int compare(Val o1, Val o2) {
				Val v1 =(Val) o1;
				Val v2 =(Val) o2;
				String[] split = v1.getKey().split("\\.");
				String[] split2 = v2.getKey().split("\\.");
				if(split.length>1){
					if(split2.length>1){
						if(split[1].equals((split2[1]))){
	
							return 0;
						}
					}else{
						if( split[1].equals(split2[0])){
							return 0;
						}
					}
				}else{
					if(split2.length>1){
						if(split[0].equals(split2[1])){
							return 0;	
						}
					}else{
						if(split[0].equals(split2[0])){
							return 0;	
						}
					}
				}

//				if(iset.contains(v1.getKey())){
//					System.out.println(v1.getKey());
//				}
			return -1;
			}
		});
		int columnCount = selectCol.getColumnCount();
		String[] vla;
		for (int s = 0; s <rowSelectCount; s++) {
			vla = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				String v = (selectCol.getValueAt(s, i).toString());
				vla[i] = v;
			}
			if(vla!=null&&!"".equals(vla[0])){
				slist.add(vla);
				sset.add(vla[0]);
				set.add(new Val(vla[0],vla));

			}
		}
		for (int s = 0; s <rowInsertCount; s++) {
			vla = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				String v = (insertCol.getValueAt(s, i).toString());
				vla[i] = v;
			}
			if(vla!=null&&!"".equals(vla[0])){
				ilist.add(vla);
				iset.add(vla[0]);
				set.add(new Val(vla[0],vla));
			}
		}
		
		Val[] array = set.toArray(new Val[0]);
		ArrayList<Val>  temp = new ArrayList<>();
		Collections.addAll(temp, array);
		Collections.sort(temp,new Comparator<Val> (){
			@Override
			public int compare(Val o1, Val o2) {
				String[] split = o1.getKey().split("\\.");
				String[] split2 = o2.getKey().split("\\.");
				if(split.length>1){
					if(split2.length>1){
						return split[1].compareTo(split2[1]);
					}else{
						return split[1].compareTo(split2[0]);
					}
				}else{
					if(split2.length>1){
						return split[0].compareTo(split2[1]);
					}else{
						return split[0].compareTo(split2[0]);
					}
					
				}
			}
		});
		
		MoveDataView.this.clear(selectCol); 
		MoveDataView.this.clear(insertCol); 
			
		

		
		int size2 = temp.size();
		for (int i = 0; i <size2 ; i++) {
			boolean isHave = false;
			int j = 0;
			String[] split = temp.get(i).getKey().split("\\.");
			String from = null;
			String to = null;
			for (j = 0; j < slist.size(); j++) {
				String[] split2 = slist.get(j)[0].split("\\.");
				if(split.length>1){
					
					if(split2.length>1){
						if(split[1].equals(split2[1])){
							from = split[1];
							to = split2[1];
							isHave= true;
							break;
						}
					}else{
						if(split[1].equals(split2[0])){
							from = split[1];
							to = split2[0];
							isHave= true;
							break;
						}
					}
				}else{
					if(split2.length>1){
						if(split[0].equals(split2[1])){
							from = split[0];
							to = split2[1];
							isHave= true;
							break;
						}
					}else{
						if(split[0].equals(split2[0])){
							from = split[0];
							to = split2[0];
							isHave= true;
							break;
						}
					}
				}
			}
			sset.size();
			if(isHave&&slist.size()>0){
				if(sset.contains(slist.get(j)[0])){
					sset.remove(slist.get(j)[0]);
					selectCol.addRow(slist.get(j));
				}
			}else{
				String[] obj = new String[columnCount];
				for (int k = 0; k < columnCount; k++) {
					obj[k] = "";
				}
				selectCol.addRow(obj);
			}
		}
		
		int size3 = temp.size();
		for (int i = 0; i <size3 ; i++) {
			boolean isHave = false;
			int j = 0;
			String[] split = temp.get(i).getKey().split("\\.");
			String from =null;
			String to =null;
			for (j = 0; j < ilist.size(); j++) {
				String[] split2 = ilist.get(j)[0].split("\\.");
				if(split.length>1){
					if(split2.length>1){
						from = split[1];
						to = split2[1];
						if(split[1].equals(split2[1])){
							isHave= true;
							break;
						}
					}else{
						if(split[1].equals(split2[0])){
							from = split[1];
							to = split2[0];
							isHave= true;
							break;
						}
					}
				}else{
					if(split2.length>1){
						if(split[0].equals(split2[1])){
							from = split[0];
							to = split2[1];
							isHave= true;
							break;
						}
					}else{
						if(split[0].equals(split2[0])){
							from = split[0];
							to = split2[0];
							isHave= true;
							
							break;
						}
					}
					
				}
			}
			if(isHave&&ilist.size()>0){
				if(iset.contains(ilist.get(j)[0])){
					iset.remove(ilist.get(j)[0]);
					insertCol.addRow(ilist.get(j));
				}
			}else{
				String[] obj = new String[columnCount];
				for (int k = 0; k < columnCount; k++) {
					obj[k] = "";
				}
				insertCol.addRow(new Object[]{"",""});
			}
		}
		setCorlor();
	}
	
	public void setCorlor(){
		
	}
	 
	@Override
	protected void finalize() throws Throwable {
		// TODO 自动生成的方法存根
		super.finalize();
		System.out.println("销毁");
	}
}

class Val  {
	private String key;
	private String[] arr;
	public Val(String key,String[] arr){
		this.setKey(key);
		this.setArr(arr);
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String[] getArr() {
		return arr;
	}
	public void setArr(String[] arr) {
		this.arr = arr;
	}
	
	

	@Override
	public boolean equals(Object obj) {

		Val v =(Val) obj;
		String[] split = v.getKey().split("\\.");
		String[] split2 = this.getKey().split("\\.");
//		if(!v.getKey().equals(key)){
//			return split[1].equals(split2[1]);
//		}
		if(split.length>1){
			if(split2.length>1){
				return split[1].equals(split2[1]);
			}else{
				return split[1].equals(split2[0]);
			}
		}else{
			if(split2.length>1){
				return split[0].equals(split2[1]);
			}else{
				return split[0].equals(split2[0]);
			}
		}
	}
	
}
/**
 * 进度条
 * @author wanghh
 *
 */
class MyProgressBarRenderer extends JProgressBar implements TableCellRenderer{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
    	if(value instanceof ExpMsg){
    		ExpMsg msg = (ExpMsg) value;
    		setMaximum(msg.getMax());
    		setStringPainted(true);
    		setValue((int) (msg.getValue().get()));
    		setMinimum(0);
//    		+"("+msg.getStartDate()==null?"_:_:_":msg.getStartDate()+"-"+msg.getEndDate()==null?"_:_:":msg.getEndDate()
    		String start = msg.getStartDate()==null?"00:00:00":msg.getStartDate();
    		String end = msg.getEndDate()==null?"00:00:00":msg.getEndDate();
    		if(msg.getMax()==-1){
        		setString("  统计中"+this.getValue()*100/(this.getMaximum()==0?1:this.getMaximum())+" % | "+this.getValue()+" / "+this.getMaximum()+"    执行时间:"+start+"-"+end);
    		}else{
    			setString("  "+this.getValue()*100/(this.getMaximum()==0?1:this.getMaximum())+" % | "+this.getValue()+" / "+this.getMaximum()+"    执行时间:"+start+"-"+end);
    		}
    		return this;
    	}
    	JLabel jLabel = new JLabel(value.toString());
    	jLabel.setOpaque(true);
        return jLabel;
    }
    
    
   
}


/** 
 * 自定义一个往列里边添加按钮的单元格编辑器。最好继承DefaultCellEditor，不然要实现的方法就太多了。 
 *  
 */  
class MyButtonEditor extends DefaultCellEditor  
{  
  
	private JPanel panel;
	private JLabel label;
	private JButton button;
    /** 
     * serialVersionUID 
     */  
    private static final long serialVersionUID = -6546334664166791132L;  

    public MyButtonEditor(ActionListener action)  
    {  
        // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。   
        super(new JTextField());  
        panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        button = new JButton("编辑"); 
        label =new JLabel();
        panel.add(button,BorderLayout.EAST);
        delegate = new EditorDelegate() {
//            public void setValue(Object value) {
//            	label.setText((value != null) ? value.toString() : "");
//            }
//
//            public Object getCellEditorValue() {
//                return delegate.getCellEditorValue();
//            }
        };
        panel.add(label,BorderLayout.CENTER);
        
        button.addActionListener(action);
        // 设置点击几次激活编辑。   
        this.setClickCountToStart(1); 
    }  
  
    
    /** 
     * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格） 
     */  
    @Override  
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  
    {  
    	
    	delegate.setValue(value);
    	label.setText(value==null?"":value.toString());
        return panel;  
    }  
    /** 
     * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。 
     */  
    @Override  
    public Object getCellEditorValue()  
    {  
        return delegate.getCellEditorValue();  
    }  
  
} 







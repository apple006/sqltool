package com.view.movedata.exp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import main.SQLTool;

import com.dao.ConnectionPool;
import com.dao.entity.Table;
import com.entity.LoginInfo;
import com.excel.CheckException;
import com.excel.ExcelEntity;
import com.excel.ExcelReader;
import com.excel.ImpDefaultExcelCheckAndPack;
import com.listener.ExcuteListener;
import com.ui.tree.UITree;
import com.view.movedata.execut.ExpDataToFileCtr;
import com.view.movedata.execut.ExpMsg;
import com.view.movedata.execut.ImpDataBaseCtr;
import com.view.sqloperate.Controller;
import com.view.system.dialog.WaringMsg;
import com.view.tool.HelpProperties;


/**
 * 导出数据库数据
 * @author wanghhc
 */
public class ExpDataView extends JPanel{
	private File[] file;
	private DefaultTableModel tableModel;
	private JTable table;
	private JComboBox connectionList;
	private JRadioButton impBut;
	private JRadioButton expBut;
	private JFileChooser fileChooser = new JFileChooser("D:\\");


	public ExpDataView(){
		init();
	}
	public void init(){
		this.setLayout(new BorderLayout());
		connectionList = getDrowDown();
	
		JPanel chooseExpType = getChooseExpType();
		
		this.add(chooseExpType,BorderLayout.NORTH);
		
		tableModel = new DefaultTableModel(){
			public boolean isCellEditable(int row, int column) {
				return column==0?true:false;
			} ;
			
		};
		table = new JTable(tableModel);
		table.setOpaque(true);
		
		tableModel.addColumn("");
		tableModel.addColumn("对象名");
		tableModel.addColumn("进度");
		TableColumn column = table.getColumnModel().getColumn(0); 
		column.setCellEditor(table.getDefaultEditor(Boolean.class));   
		column.setCellRenderer(table.getDefaultRenderer(Boolean.class));
		TableColumn jpro = table.getColumnModel().getColumn(2); 
		jpro.setCellRenderer((new MyProgressBarRenderer()));
		column.setMaxWidth(30);
		column.setPreferredWidth(30);
		column.setWidth(30);
		column.setMinWidth(30);
		
		
		
		JScrollPane scrollpane1 = new JScrollPane(table,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel panelCenter = new JPanel(new BorderLayout());
		
		JPanel panelList = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
		final JTextField find = new JTextField(15);
		final TableRowSorter tableRowSorter = new TableRowSorter(tableModel);
		table.setRowSorter(tableRowSorter);
		panelList.add(find);
		panelList.add(connectionList);
		
		find.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				tableRowSorter.setRowFilter(RowFilter.regexFilter(".*"+find.getText()+".*"));
			}
		});


		panelCenter.add(panelList,BorderLayout.NORTH);
		panelCenter.add(scrollpane1,BorderLayout.CENTER);
		this.add(panelCenter,BorderLayout.CENTER);
		
		
		JPanel selectPanel = getSelectPanel();
		
		JPanel downButtnPanel = new JPanel(new BorderLayout());
		
		downButtnPanel.add(selectPanel,BorderLayout.NORTH);
	
		
//		JPanel model = getModel();
		JPanel selectPath = getSelectPath();
		
//		selectPath.add(model);
		downButtnPanel.add(selectPath,BorderLayout.SOUTH);
		this.add(downButtnPanel,BorderLayout.SOUTH);
		initData();
		
	}
	private JPanel getSelectPath() {
		JPanel selectPath = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		
		JPanel model = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		expBut = new JRadioButton("导出模式",true);
		impBut= new JRadioButton("导入模式");
		ButtonGroup group = new ButtonGroup();
		group.add(expBut);
		group.add(impBut);
		model.setBorder( BorderFactory.createLineBorder(Color.gray));
		model.add(expBut);
		model.add(impBut);
		selectPath.add(model);
		
		
		selectPath.add(new JLabel("   路径:"));
		final JTextField field = new JTextField(40);
		field.setEditable(false);
		selectPath.add(field);
		
		final JButton chooser = new JButton("选择路径");
		final JButton exp = new JButton("导出");
		expBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				int showConfirmDialog = WaringMsg.showConfirmDialog("是否加载导出信息",ExpDataView.this);
//				if(showConfirmDialog==JOptionPane.OK_OPTION){
					clearTableMsg();
					getChangeTableMsg();
					exp.setText("导出");
					fileChooser.setMultiSelectionEnabled(false);
					field.setText("");
//				}else{
//					impBut.setSelected(true);
//				}
			}
		});
		impBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				int showConfirmDialog = WaringMsg.showConfirmDialog("是否加载导入信息",ExpDataView.this);
//				if(showConfirmDialog==JOptionPane.OK_OPTION){
					clearTableMsg();
					exp.setText("导入");
					fileChooser.setMultiSelectionEnabled(true);
					field.setText("");
//				}else{
//					expBut.setSelected(true);
//				}
			}
		});
		
		selectPath.add(chooser);
		exp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int rowCount = table.getRowCount();
				if(impBut.isSelected()){
					for (int i = 0; i < rowCount; i++) {
						Boolean valueAt = (Boolean) table.getValueAt(i, 0);
						if(valueAt){
							new ImpDataBaseCtr((LoginInfo)connectionList.getSelectedItem(),file[i],table,i);
						}
					}
				}
				if(expBut.isSelected()){
					for (int i = 0; i < rowCount; i++) {
						Boolean valueAt = (Boolean) table.getValueAt(i, 0);
						if(valueAt){
							new ExpDataToFileCtr((LoginInfo)connectionList.getSelectedItem(), table.getValueAt(i, 1).toString(),file[0],table,i);
						}
						
					}
				}
			}
			
		});
		selectPath.add(exp);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
		
		chooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(expBut.isSelected()){
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
				}else{
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
					
				}
				int showSaveDialog = fileChooser.showSaveDialog(null);
				if (JFileChooser.APPROVE_OPTION == showSaveDialog) {
					
					
					file = null;
					
					StringBuffer pathStr = new StringBuffer();
					file = fileChooser.getSelectedFiles();
					File selectedFile = fileChooser.getSelectedFile();
					if(selectedFile!=null&&expBut.isSelected()){
						file = new File[]{selectedFile.isFile()?selectedFile.getParentFile():selectedFile};
					}
					for (int i = 0; i < file.length; i++) {
						String path = file[i].getPath();
						pathStr.append(file[i].getPath());
						pathStr.append(";");
					}
					field.setText(pathStr.toString());
					
					if(impBut.isSelected()){
						clear();
						for (int j = 0; j < file.length; j++) {
							if(!file[j].isFile()){
								WaringMsg.showConfirmDialog("请选择文件", ExpDataView.this);
								break;
							}
							if(file[j].getName().endsWith(".xls")){
								try {
									ExcelReader<String[]> exacelReader = new ExcelReader<String[]>(file[j].getPath());
									exacelReader.setCheckAndPackModel(new ImpDefaultExcelCheckAndPack(exacelReader));
									ExcelEntity<String[]> readExcelContent = exacelReader.readExcelContent();
									readExcelContent.getList();
									int rowNum = readExcelContent.getRowNum();
									
									ExpMsg exp = new ExpMsg(rowNum); 	 
									tableModel.addRow(new Object[]{false,file[j].getName(),exp});
									
								} catch (IOException e) {
									ExpMsg exp = new ExpMsg(0); 	 
									tableModel.addRow(new Object[]{false,"文件 "+file[j].getName()+" 格式有误读取失败",exp});
								} catch (CheckException e) {
									e.printStackTrace();
								}
							}else {
								tableModel.addRow(new Object[]{false,"文件 "+file[j].getName()+" 暂不支持 "+file[j].getName().split("\\.")[1]+"格式导入",exp});

							}
						}
					}
				}
			}
		});
		return selectPath;
	}
	private JPanel getModel() {
		JPanel model = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		impBut = new JRadioButton("导入模式");
		expBut = new JRadioButton("导出模式",true);
		ButtonGroup group = new ButtonGroup();
		group.add(expBut);
		group.add(impBut);
		model.setBorder( BorderFactory.createLineBorder(Color.gray));
		
		model.add(expBut);
		model.add(impBut);
		return model;
	}	
	private JPanel getSelectPanel() {
		JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
		JButton all = new JButton("全选");
		JButton cancel = new JButton("取消");
		selectPanel.add(all);
		selectPanel.add(cancel);
		return selectPanel;
	}	
		
		
	private JPanel getChooseExpType() {
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,10));
		panel1.setBorder( BorderFactory.createTitledBorder("导出格式"));
		JCheckBoxMenuItem tableData = new JCheckBoxMenuItem("导出表数据.sql");
		JCheckBoxMenuItem tableDataDb = new JCheckBoxMenuItem("导出表数据.db");
		JCheckBoxMenuItem tableStructure = new JCheckBoxMenuItem("导出表结构");
		JCheckBoxMenuItem views = new JCheckBoxMenuItem("导出视图");
		JCheckBoxMenuItem procedures = new JCheckBoxMenuItem("导出存储过程");
		JCheckBoxMenuItem functions = new JCheckBoxMenuItem("导出方法");
		JCheckBoxMenuItem indexs = new JCheckBoxMenuItem("导出索引");
		JCheckBox box = new JCheckBox();
//		tableStructure.setEnabled(false);
		tableData.setSelected(true);
		box.add(tableData);
		box.add(tableDataDb);
		box.add(tableStructure);
		box.add(views);
		box.add(indexs);
		box.add(procedures);
		box.add(functions);
		panel1.add(tableData);
		panel1.add(tableDataDb);
		panel1.add(tableStructure);
		panel1.add(views);
		panel1.add(procedures);
		panel1.add(functions);
		panel1.add(indexs);
		return panel1;
	}	
		
	public void initData(){
		
	}	
		
	public void addRows(Object[] rowData){
		tableModel.addRow(rowData);
	}
	public void clear(){
		int rowCount = tableModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			tableModel.removeRow(0);
		}
	}
		
		
	public JComboBox getDrowDown() {
		connectionList = new JComboBox();	
		connectionList.removeAllItems();
		UITree dataBaseTree = SQLTool.getSQLTool().getToolFrame().getSqlView()
				.getDataBaseTree();
		DefaultTreeModel model = (DefaultTreeModel) dataBaseTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		int childCount = root.getChildCount();
		LoginInfo[] please = new LoginInfo[childCount];
		LoginInfo login = new LoginInfo();
		login.setName("---请选择---");
		connectionList.addItem(login);

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
			connectionList.addItem(please[i]);
		}
		connectionList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					if(expBut.isSelected()){
						clearTableMsg();
						getChangeTableMsg();
					}
				}
			}
		});
		return connectionList;
	}
	/**
	 * 导出表信息
	 */
	private void getChangeTableMsg() {
		Thread th = new Thread(){
			public void run() {
				try {
					LoginInfo  log = (LoginInfo) connectionList.getSelectedItem();
					if(log.getIsLive()==ConnectionPool.IS_DIE){
						return ;
					}
					tableModel.addRow(new Object[]{false,"加载数据中...",""});
					ArrayList<Table> tablesa = Controller.newController().getResTables(log);
					clear();
					boolean count = "Y".equals(HelpProperties.GetValueByKey("keyvalue.properties", "countEachTable"));
					for (int i = 0; i < tablesa.size(); i++) {
						tableModel.addRow(new Object[]{false,tablesa.get(i).getTableCode(),"统计数据中"});
						int tablesRow = Controller.newController().getTablesRow(log, tablesa.get(i).getTableCode(),count); 
						tableModel.setValueAt(new ExpMsg(tablesRow),i, 2);
					}

				} catch (InterruptedException e1) {
					table.removeAll();
					e1.printStackTrace();
				} catch (SQLException e1) {
					table.removeAll();
					e1.printStackTrace();
				}
			}
		};
		th.start();
		
	}
	/**
	 * 清楚导入导出数据信息
	 */
	private void clearTableMsg() {
		clear();
	}
}

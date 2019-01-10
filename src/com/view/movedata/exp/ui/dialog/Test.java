package com.view.movedata.exp.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import main.SQLTool;

import com.dao.entity.Table;
import com.entity.LoginInfo;
import com.excel.CheckException;
import com.excel.ExcelEntity;
import com.excel.ExcelReader;
import com.excel.ImpDefaultExcelCheckAndPack;
import com.ui.extensible.Cell;
import com.ui.tree.UITree;
import com.view.movedata.exp.entity.RandomTest;
import com.view.movedata.exp.ui.IPop;
import com.view.sqloperate.Controller;
import com.view.sqloperate.LineNumberHeaderView;
import com.view.system.dialog.WaringMsg;

public class Test implements IPop{
	private JPanel panel1;
	private DefaultTableModel listValue;
	private JTable listTable;
	private JTextField codeText;
	private JFileChooser fileChooser = new JFileChooser("D:\\");

	
	private JRadioButton common;
	private JRadioButton excel;
	private JRadioButton sql;
	private JButton open;
	private JLabel jLabel;
	private JComboBox connectionList;
	public Test(Object object){
		panel1 = new JPanel(new BorderLayout());
	
		
		listValue = new DefaultTableModel();
		listTable = new JTable(listValue);
		
		listValue.addColumn("随机值");
		if(object instanceof RandomTest){
			
			Object[] o = ((RandomTest)object).getArr();
			for (int i = 0; i < o.length; i++) {
				listValue.addRow( new String[]{o[i].toString()});
			}
		}
		JScrollPane selectScrol = new JScrollPane(listTable,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		LineNumberHeaderView lineNumberHeaderView = new LineNumberHeaderView();
		lineNumberHeaderView.setFont(listTable.getFont());
		selectScrol.setRowHeaderView(lineNumberHeaderView);
		lineNumberHeaderView.setLineHeight(listTable.getRowHeight());
		
		panel1.add(selectScrol,BorderLayout.CENTER);
		
		JPanel panel3 = initButtons();
		panel1.add(panel3,BorderLayout.EAST);
		JPanel panel2 = initRadioGroup();
		panel1.add(panel2,BorderLayout.NORTH);
		JPanel panel4 = initInputPanel();
		panel1.add(panel4,BorderLayout.SOUTH);

		
	}

	private  JPanel  initInputPanel() {
		 JPanel panel=new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		 codeText = new JTextField(30);
		 codeText.setEditable(false);
		 jLabel = new JLabel("目标");
		 panel.add(jLabel);
		 panel.add(codeText);

		 open = new JButton("打开");
		 open.setEnabled(false);
		 panel.add(open);
		 open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(excel.isSelected()){
						
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.setFileFilter(new FileFilter() {
						
						@Override
						public String getDescription() {
							return ".xls";
						}
						
						@Override
						public boolean accept(File f) {
							 if (f.isDirectory())return true;
							    return f.getName().endsWith(".xls");
						}
					});
					int showSaveDialog = fileChooser.showSaveDialog(null);
					if (JFileChooser.APPROVE_OPTION == showSaveDialog) {
	//					file = null;
						
	//					file = fileChooser.getSelectedFiles();
						File selectedFile = fileChooser.getSelectedFile();
						
						if(excel.isSelected()){
								clear();
								if(selectedFile.isFile()){
									if(WaringMsg.showConfirmDialog("加载EXCEL信息？", panel1)==JOptionPane.CANCEL_OPTION){
										return;
									}
								}
								codeText.setText(selectedFile.getPath().toString());
								if(selectedFile.getName().endsWith(".xls")){
									ExcelReader<String[]> exacelReader;
									try {
										exacelReader = new ExcelReader<String[]>(selectedFile.getPath());
										exacelReader.setCheckAndPackModel(new ImpDefaultExcelCheckAndPack(exacelReader));
										ExcelEntity<String[]> readExcelContent = exacelReader.readExcelContent();
										List<String[]> list =  readExcelContent.getList();
										for (int i = 0; i < list.size(); i++) {
											listValue.addRow(new String[]{list.get(i)[0]});
											
										}
									} catch (IOException e1) {
										// TODO 自动生成的 catch 块
										e1.printStackTrace();
									} catch (CheckException e1) {
										// TODO 自动生成的 catch 块
										e1.printStackTrace();
									}
								}
							}
						}
					}else if(sql.isSelected()){
						try {
							Table executSql = Controller.newController().executSql((LoginInfo)connectionList.getSelectedItem(), codeText.getText());
							int rowNum = executSql.getRowNum();
							for (int i = 0; i < rowNum; i++) {
								Cell value = executSql.getValue(i, 1);
								if(value.getOld_value()==null){
									listValue.addRow(new String[]{"null"});
								}
								else{
									listValue.addRow(new String[]{value.getOld_value().toString()});
								}
							}
						} catch (InterruptedException e1) {
							WaringMsg.showConfirmDialog("查询失败", panel1);
							e1.printStackTrace();
						} catch (SQLException e1) {
							WaringMsg.showConfirmDialog(e1.getMessage(), panel1);
							e1.printStackTrace();
						}
					}
				
				}
		 }
		);
		 return panel;

	}

	private JPanel initButtons() {
		 JPanel panel=new JPanel(); 
		 BoxLayout layout=new BoxLayout(panel, BoxLayout.Y_AXIS); 
		 panel.setLayout(layout);

		JButton add = new JButton("增加");
		panel.add(add);
		JButton edit = new JButton("修改");
		JButton delete = new JButton("删除");
		add.addActionListener(new OnClick());
		edit.addActionListener(new OnClick());
		delete.addActionListener(new OnClick());

		panel.add(edit);
		panel.add(delete);
		return panel;
	}


	private JPanel initRadioGroup() {
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		common = new JRadioButton("一般",true);
		excel = new JRadioButton("EXCEL");
		sql = new JRadioButton("SQL");
		ButtonGroup group = new ButtonGroup();
		common.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
				codeText.setEditable(false);
				open.setEnabled(false);				
			}
		});
		excel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					clear();
					jLabel.setText("路径");
					codeText.setEditable(false);
					open.setText("打开");
					open.setEnabled(true);
			}
		});
		sql.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sql.isSelected()){
					clear();
					jLabel.setText("SQL:");
					codeText.setEditable(true);
					open.setVisible(true);
					open.setText("执行");
					open.setEnabled(true);
				}
			}
		});
		
		group.add(common);
		group.add(excel);
		group.add(sql);
		connectionList = new JComboBox();
		getConnection();

		panel2.add(common);
		panel2.add(excel);
		panel2.add(sql);
		JPanel panel12 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel12.add(new JLabel("数据源:"));
		panel12.add(connectionList);
		panel2.add(panel12);
		return panel2;
	}
	private void getConnection(){
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
		int rowCount = listTable.getRowCount();
		LinkedList<String> link =new LinkedList<String>();
		for (int i = 0; i <rowCount; i++) {
			link.add(listTable.getValueAt(i, 0).toString());
		}
		return new RandomTest<String>(link.toArray(new String[0]));
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
					int j = selectedRow[i-r];
					listValue.removeRow(j);
				}
			}
			if("执行".equals(string)){
				try {
					Table executSql = Controller.newController().executSql((LoginInfo)connectionList.getSelectedItem(), codeText.getText());
					int rowNum = executSql.getRowNum();
					for (int i = 0; i < rowNum; i++) {
						Cell value = executSql.getValue(i, 0);
						listValue.addRow(new String[]{value.getNew_value().toString()});

					}
				} catch (InterruptedException e1) {
					WaringMsg.showConfirmDialog("查询失败", panel1);
					e1.printStackTrace();
				} catch (SQLException e1) {
					WaringMsg.showConfirmDialog(e1.getMessage(), panel1);
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Random rand = new Random();
		for (int i = 0; i < 1000; i++) {
			System.out.println(rand.nextInt(100));
			
		}
	}
}


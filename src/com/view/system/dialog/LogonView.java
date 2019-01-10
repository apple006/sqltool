package com.view.system.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.tree.TreeModel;

import com.dao.ConnectionPool;
import com.entity.DataBaseType;
import com.entity.DriverPathInfo;
import com.entity.LoginInfo;
import com.ui.XMLConnectionsConfig;
import com.ui.tree.IconMutableTreeNode;
import com.ui.tree.UITree;
import com.util.ICheckUtilParameter;
import com.view.sqloperate.Controller;
import com.view.system.perferenc.xml.XMLJdbcConfig;

import main.SQLTool;

/**
 * 新建连接数据库对话框
 * 
 * @author Administrator
 * 
 */
public class LogonView extends JDialog implements ICheckUtilParameter{

	private Component parent;
	private LoginInfo loginInfo;
	private LoginInfo connInfo_old;
	private IconMutableTreeNode treeNode;

	private JComboBox dataTypeList;
	private JComboBox driverList;
	private JTextField name;
//	private JTextField dataBaseUrl;
	private JTextField user;
	
	private JTextField ip;
	private JTextField port;
	private JTextField example;


	private JPasswordField password;
	private JFileChooser fileChooser = new JFileChooser("D:\\");

	private final int WIDTH =300;
	private final int HEIGHT = 280;
//	private JDialog dialog;
	private final XMLConnectionsConfig info = XMLConnectionsConfig.getConnDataBaseInfo();
	private Hashtable<String, DriverPathInfo> driverPathsArr = XMLJdbcConfig.getXMLJdbcConfig().getDriverPathsArr();
	private JTextField pdm;

	public LogonView(JFrame jFrame) {
		super( jFrame, "连接数据库", true);
		this.parent = jFrame;
		init();
	}

	public LogonView() {
		this(SQLTool.getSQLTool().getToolFrame().getFrame());
	}

	public LogonView(IconMutableTreeNode treeNode) {
		this(SQLTool.getSQLTool().getToolFrame().getFrame());
	}
	private static final long serialVersionUID = 1L;

	private void init() {

//		dialog = new JDialog((Frame) parent, "连接数据库", false);
		setSize(WIDTH, HEIGHT);
//		setLayout(new GridLayout(9, 1, 4, 8));
		JPanel p = new JPanel(new BorderLayout());
		GridBagLayout gridBagLayout = new GridBagLayout();
		JPanel logP = new JPanel(gridBagLayout);
		p.add(logP,BorderLayout.CENTER);
		setLayout(new BorderLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets.bottom=4;
		constraints.insets.left=4;
		constraints.insets.right=2;
		constraints.insets.right=2;
		constraints.fill = GridBagConstraints.BOTH;
		JLabel gL = new JLabel("名称：", JLabel.RIGHT);
		constraints.gridwidth=2;
		constraints.weightx = 0;
		constraints.weighty=0;
		gridBagLayout.setConstraints(gL, constraints);
		logP.add(gL);
		
		name = new JTextField();
		constraints.gridwidth=0;
		constraints.weightx = 1;
		constraints.weighty=0;
		gridBagLayout.setConstraints(name, constraints);
		logP.add(name);
		
		JLabel aL = new JLabel("数据库类型：", JLabel.RIGHT);
		constraints.gridwidth=2;
		constraints.weightx = 0;
		constraints.weighty=0;
		gridBagLayout.setConstraints(aL, constraints);
		logP.add(aL);
		
		constraints.gridwidth=0;
		constraints.weightx = 1;
		constraints.weighty=0;
		DataBaseType[] dataBaseTypes = XMLJdbcConfig.getXMLJdbcConfig().getDataBaseTypeList().toArray(new DataBaseType[0]);
		dataTypeList = new JComboBox(dataBaseTypes);
		gridBagLayout.setConstraints(dataTypeList, constraints);
		logP.add(dataTypeList);
		
		JLabel bL = new JLabel("驱动：", JLabel.RIGHT);
		constraints.gridwidth=2;
		constraints.weightx = 0;
		constraints.weighty=0;
		gridBagLayout.setConstraints(bL, constraints);
		logP.add(bL);
		
		DriverPathInfo[] jdbcPathInfos = XMLJdbcConfig.getXMLJdbcConfig().getDriverPathList().toArray(new DriverPathInfo[0]);
		driverList = new JComboBox(jdbcPathInfos);
		constraints.gridwidth=0;
		constraints.weightx = 1;
		constraints.weighty=0;
		gridBagLayout.setConstraints(driverList, constraints);
		logP.add(driverList);
		
		JLabel cL = new JLabel("IP：", JLabel.RIGHT);
		constraints.gridwidth=2;
		constraints.weightx = 0;
		constraints.weighty=0;
		gridBagLayout.setConstraints(cL, constraints);
		logP.add(cL);

		ip = new JTextField(12);
		constraints.gridwidth=0;
		constraints.weightx = 1;
		constraints.weighty=0;
		gridBagLayout.setConstraints(ip, constraints);
		logP.add(ip);
		
		JLabel portL = new JLabel("端口号：", JLabel.RIGHT);
		constraints.gridwidth=2;
		constraints.weightx = 0;
		constraints.weighty=0;
		gridBagLayout.setConstraints(portL, constraints);
		logP.add(portL);
		
		
		port = new JTextField();
		constraints.gridwidth=0;
		constraints.weightx = 1;
		constraints.weighty=0;
		gridBagLayout.setConstraints(port, constraints);
		logP.add(port);

		
		
		JLabel dL = new JLabel("实例：", JLabel.RIGHT);
		constraints.gridwidth=2;
		constraints.weightx = 0;
		constraints.weighty=0;
		gridBagLayout.setConstraints(dL, constraints);
		logP.add(dL);
		
		example = new JTextField(12);
		constraints.gridwidth=0;
		constraints.weightx = 1;
		constraints.weighty=0;
		gridBagLayout.setConstraints(example, constraints);
		logP.add(example);
		
		JLabel eL = new JLabel("用户名：", JLabel.RIGHT);
		constraints.gridwidth=2;
		constraints.weightx = 0;
		constraints.weighty=0;
		gridBagLayout.setConstraints(eL, constraints);
		logP.add(eL);

		user = new JTextField(12);
		constraints.gridwidth=0;
		constraints.weightx = 1;
		constraints.weighty=0;
		gridBagLayout.setConstraints(user, constraints);
		logP.add(user);
		
		JLabel fL = new  JLabel("密码：", JLabel.RIGHT);
		constraints.gridwidth=2;
		constraints.weightx = 0;
		constraints.weighty=0;
		gridBagLayout.setConstraints(fL, constraints);
		logP.add(fL);
		
		password = new JPasswordField(12);
		constraints.gridwidth=0;
		constraints.weightx = 1;
		constraints.weighty=0;
		gridBagLayout.setConstraints(password, constraints);
		logP.add(password);
		

		name.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent paramKeyEvent) {
			}
			
			@Override
			public void keyReleased(KeyEvent paramKeyEvent) {
				try {
					updata();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				SQLTool.getSQLTool().getToolFrame().getSqlView().getDataBaseTree().updateUI();
			}
			
			@Override
			public void keyPressed(KeyEvent paramKeyEvent) {
				
			}
		});

		JPanel butonP = new JPanel(new GridLayout(1, 3, 8, 8));
		JPanel jPanel = new JPanel();
		jPanel.setBorder( BorderFactory.createTitledBorder("PDM路径"));
		pdm = new JTextField(18);
//		pdm.setEditable(false);
		jPanel.add(pdm);
		JButton pdmbut = new JButton("选择PDM");
		
		pdmbut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int showSaveDialog = fileChooser.showOpenDialog(null);
				if (JFileChooser.APPROVE_OPTION == showSaveDialog) {
					File selectedFile = fileChooser.getSelectedFile();
					pdm.setText(selectedFile.toString());
				}
			}
		});
		
		
		
		
		
		jPanel.add(pdmbut);
		constraints.gridwidth=0;
		constraints.weightx = 1;
		constraints.weighty=0;
		gridBagLayout.setConstraints(butonP, constraints);
		logP.add(butonP);
		JButton logon = new JButton("登录");
		butonP.add(logon);
		logon.addMouseListener(new LogOnMouse(treeNode));
		JButton cancel = new JButton("取消");
		butonP.add(cancel);
		cancel.addMouseListener(new LogOnMouse(treeNode));
		final JButton pmd = new JButton("PDM >>");
		butonP.add(pmd);
		pmd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JButton cancel = new JButton("取消");
				if("PDM >>".equals(pmd.getText())){
					pmd.setText("PDM <<");
					setSize(300, 340);
				}else{
					pmd.setText("PDM >>");
					setSize(300, 280);
				}
			}
		});
		add(p,BorderLayout.NORTH);
		add(jPanel,BorderLayout.SOUTH);
		setLocationRelativeTo(null);
	}
	

	/**
	 * 
	 * @param treeNode 被点击树节点（登陆时选择的登录节点）
	 */
	public void setTreeNode(IconMutableTreeNode treeNode) {
		this.treeNode = treeNode;
		this.loginInfo = (LoginInfo) treeNode.getUserObject();
		name.setText(this.loginInfo.getName());
		dataTypeList.setSelectedItem(XMLJdbcConfig.getXMLJdbcConfig().getDataBaseTypes().get(this.loginInfo.getDataType()));
		driverList.setSelectedItem(XMLJdbcConfig.getXMLJdbcConfig().getDriverPaths().get(this.loginInfo.getDriverClass()));
//		dataBaseUrl.setText(this.loginInfo.getUrl());
		
//		DriverPathInfo driverPathInfo = driverPathsArr.get(((DriverPathInfo) driverList.getSelectedItem()).getName());
		ip.setText(this.loginInfo.getIp());
		pdm.setText(this.loginInfo.getPdm());
		port.setText(this.loginInfo.getPort());
		example.setText(this.loginInfo.getExample());
		user.setText(this.loginInfo.getUserName());
		password.setText(this.loginInfo.getPassword());
		
		
		
		try {
			connInfo_old = (LoginInfo) this.loginInfo.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		setVisible(true);
	}
	private void updata() throws CloneNotSupportedException{
		
		loginInfo.setName(name.getText());
		if( dataTypeList.getSelectedItem()!=null)
			loginInfo.setDataType(((DataBaseType) dataTypeList.getSelectedItem()).getName());
		if( driverList.getSelectedItem()!=null)
		loginInfo.setDriverClass(((DriverPathInfo) driverList.getSelectedItem()).getName());
//		loginInfo.setUrl(this.dataBaseUrl.getText());
		loginInfo.setIp(ip.getText());
		loginInfo.setPort(port.getText());
		loginInfo.setExample(example.getText());
		loginInfo.setUserName(user.getText());
		loginInfo.setPassword(password.getText());
		loginInfo.setPdm(pdm.getText());
	}
	class LogOnMouse extends MouseAdapter{
		private IconMutableTreeNode node;
		public LogOnMouse(IconMutableTreeNode treeNode){
			this.node =treeNode;
		}
		@Override
		public void mouseClicked(MouseEvent event) {
			if(((JButton)event.getSource()).getText().equals("登录")){
				int ecp = 0;
				try {
					try {
						updata();
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					
					boolean addConnectionInfo = info.updateConnectionInfo(loginInfo,connInfo_old);
					if(!addConnectionInfo){
						JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "连接源名称："+name.getText()+" 已存在,请重新填写！");
						ecp = -1;
						name.selectAll();
						name.grabFocus();
					}
					else{
						try {
							loginInfo.setIsLive(ConnectionPool.DENG);
							if(loginInfo.getDataType()==null){
								JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "数据库类型不能为空！");
								return;
							}
							if(loginInfo.getDriverClass()==null){
								JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "驱动不能为空！");
								return;
							}
							if(loginInfo.getUserName()==null){
								JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "用户名不能为空！");
								return;
							}
							if(loginInfo.getUrl()==null){
								JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "数据库url不能为空！");
								return;
							}
							if(loginInfo.getUrl()==null){
								JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "数据库url不能为空！");
								return;
							}
							if(loginInfo.getPassword()==null){
								JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "密码不能为空！");
								return;
							}
							Controller.newController().loginIn(loginInfo);
							treeNode.setUserObject(loginInfo);
							 UITree dataBaseTree = SQLTool.getSQLTool().getToolFrame().getSqlView().getDataBaseTree();
								TreeModel model = dataBaseTree.getModel();
								IconMutableTreeNode root = (IconMutableTreeNode) model.getRoot();
								Enumeration children = root.children();
								IconMutableTreeNode needAddNode = null;
								while (children.hasMoreElements()) {
									IconMutableTreeNode node = (IconMutableTreeNode) children.nextElement();
									if (node.getUserObject() instanceof LoginInfo) {
										LoginInfo user = (LoginInfo) node.getUserObject();
										if (user.equals(loginInfo)) {
											needAddNode  = node;
											break;
										}
									}
								}
								needAddNode.runIoc();
						} catch (ClassNotFoundException e) {
							ecp = -1;
							JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "没有找到jar包！");
							e.printStackTrace();
						} catch (SQLException e) {
							ecp = -1;
							JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "用户名或密码错误！");
							e.printStackTrace();
						}
					}
					if(ecp ==0){
						setVisible(false);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(((JButton)event.getSource()).getText().equals("取消")){
//				updata();
//				int showConfirmDialog =-999;
//				if(!loginInfo.equals(connInfo_old)){
//					showConfirmDialog = JOptionPane.showConfirmDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "信息已更改是否进行取消编辑？","提示",JOptionPane.YES_OPTION);
//				}
//				if(showConfirmDialog==0){
					loginInfo.setDataType(connInfo_old.getDataType());
					loginInfo.setDriverClass(connInfo_old.getDriverClass());
					loginInfo.setName(connInfo_old.getName());
					loginInfo.setPassword(connInfo_old.getPassword());
					loginInfo.setUrl(connInfo_old.getUrl());
					loginInfo.setUserName(connInfo_old.getUserName());
					treeNode.setUserObject(loginInfo);
					SQLTool.getSQLTool().getToolFrame().getSqlView().getDataBaseTree().updateUI();
//					
//					setVisible(false);
//				}
//				else{
					setVisible(false);
//				}
			}
		}
	}
	
	private void loadTables(){
		
	}
	@Override
	public void setVisible(boolean paramBoolean) {
		super.setVisible(paramBoolean);
		
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name.getText();
	}
}



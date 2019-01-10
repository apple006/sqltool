package com.view.system.perferenc;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.SQLTool;

import com.entity.DriverPathInfo;
import com.view.system.perferenc.xml.XMLJdbcConfig;

public class JdbcPathPanel extends JPanel{
	/**
	 * 
	 */
	
	private JTextField name;
	private JTextField urlFormat;
	private JTextField driverClass;
	private JFileChooser fileChooser;
	private JTextField fileUrl;
	private static final long serialVersionUID = 1L;
	private XMLJdbcConfig xmlJdbcConfig;
	private DriverPathInfo oldJdbcPathInfo;
	private DriverPathInfo jdbcConfig;
	private JdbcDialog dialog;
	private Hashtable<String, DriverPathInfo> jdbcPathInfoArray;
	private JList jdbdList;
	private DefaultListModel model=new DefaultListModel();
	private JScrollPane jScrollPane;
	public JdbcPathPanel(){
		init();
	}
	private void init(){
		BorderLayout border = new BorderLayout();
		setLayout(border);
		JLabel describe = new JLabel("配置jdbc驱动");
		describe.setFont(new Font("SansSerif", Font.PLAIN, 18));
		add(describe,BorderLayout.NORTH);
		xmlJdbcConfig =  XMLJdbcConfig.getXMLJdbcConfig();
		DriverPathInfo[] listData = getDriverPaths();
		jdbdList = new JList();
		jdbdList.setListData(listData);
		jScrollPane = new JScrollPane(jdbdList);
		add(jScrollPane,BorderLayout.CENTER);
		
		dialog = new JdbcDialog();
		
		JPanel buttonsPanel = new JPanel(new GridLayout(8,1));
		JButton add = new JButton("  添加  ");
		JButton modit = new JButton("  修改  ");
		JButton delete = new JButton("  删除  ");
		MouseAdapter listener =new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				clearText();
				oldJdbcPathInfo=null;
				JButton source = (JButton) e.getSource();
				if("  添加  ".equals(source.getText())){
					dialog.setTitle("添加jdbc驱动");
					dialog.setVisible(true);
				}
				if("  修改  ".equals(source.getText())){
					int selectedIndex = jdbdList.getSelectedIndex();
					if(selectedIndex!=-1){
						DriverPathInfo selectedValue = (DriverPathInfo) jdbdList.getSelectedValue();
						oldJdbcPathInfo = selectedValue;
						name.setText(oldJdbcPathInfo.getName());
						urlFormat.setText(oldJdbcPathInfo.getUrlFormat());
						driverClass.setText(oldJdbcPathInfo.getDriverClass());
						fileUrl.setText(oldJdbcPathInfo.getFileUrl());
						dialog.setVisible(true);
						
					}
					else{
						JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "请选择需要修改驱动信息！");
					}
				}
				if("  删除  ".equals(source.getText())){
					dialog.setTitle("修改jdbc驱动");
					if(jdbdList.getSelectedIndex()!=-1){
						int selectedIndex = jdbdList.getSelectedIndex();
						DriverPathInfo selectedValue = (DriverPathInfo) jdbdList.getSelectedValue();
						oldJdbcPathInfo = selectedValue;
						try {
							xmlJdbcConfig.deleteElement(selectedValue);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						jdbdList.setListData(getDriverPaths());
						jdbdList.updateUI();
					}
					else{	
						JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "请选择需要删除的驱动信息！");
					}
				}
			}
		};
		add.addMouseListener(listener);
		modit.addMouseListener(listener);
		delete.addMouseListener(listener);
		buttonsPanel.add(add);
		buttonsPanel.add(modit);
		buttonsPanel.add(delete);
		add(buttonsPanel,BorderLayout.EAST);
		
	}
	/**
	 * 数据驱动类型
	 * @return
	 */
	private DriverPathInfo[] getDriverPaths() {
		Hashtable<String, DriverPathInfo> driverPathsArr = xmlJdbcConfig.getDriverPathsArr();
		Iterator<String> iterator = driverPathsArr.keySet().iterator();
		
		DriverPathInfo [] listData = new DriverPathInfo[driverPathsArr.size()];
		int i=0;
		while(iterator.hasNext()){
			listData[i++]=driverPathsArr.get(iterator.next());
		}
		return listData;
	}
	
	
	class JdbcDialog extends JDialog{
		
		public JdbcDialog(){
			super(SQLTool.getSQLTool().getToolFrame().getFrame(),"添加jdbc驱动",true);
			this.setSize(300, 200);
			this.setLayout(new GridLayout(5, 1, 4, 10));
			
			JPanel c = new JPanel(new GridLayout(1, 2, 8, 4));
			c.add(new JLabel("URL Format:", JLabel.RIGHT));
			urlFormat = new JTextField(12);
			c.add(urlFormat);

			JPanel d = new JPanel(new GridLayout(1, 2, 8, 4));
			d.add(new JLabel("Driver Class:", JLabel.RIGHT));
			driverClass = new JTextField(12);
			d.add(driverClass);

			JPanel e = new JPanel(new GridLayout(1, 2, 8, 4));
			e.add(new JLabel("fileUrl:", JLabel.RIGHT));
			JPanel open = new JPanel(new BorderLayout(4,4));
			fileUrl = new JTextField();
			fileUrl.setEditable(false);
			
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".jar", "jar");

			fileChooser = new JFileChooser();
			fileChooser.setFileFilter(filter);
			
			JButton openButton = new JButton("打开");
			openButton.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent arg0) {
					int returnValue = fileChooser.showOpenDialog(null);  
				    if (returnValue == JFileChooser.APPROVE_OPTION)  
				    {  
				    	fileUrl.setText(fileChooser.getSelectedFile().toString());
				    	fileUrl.setToolTipText(fileUrl.getText());
				    }  
				}
			});
			open.add(openButton,BorderLayout.EAST);
			open.add(fileUrl,BorderLayout.CENTER);
			e.add(open);
			
			JPanel namePael = new JPanel(new GridLayout(1, 2, 8, 4));
			namePael.add(new JLabel("Name:", JLabel.RIGHT));
			name = new JTextField();
			namePael.add(name);
			name.setText("");
			this.add(namePael);
			this.add(c);
			this.add(d);
			this.add(e);
			JPanel f = new JPanel(new GridLayout(1, 6, 8, 4));
			JButton save = new JButton("保存");
			save.addMouseListener(new SaveJdbcConfigMouse());
			f.add(save);
			JButton cancel = new JButton("取消");
			cancel.addMouseListener(new SaveJdbcConfigMouse());
//			cancel.addMouseListener(new LogOnMouse());
			f.add(cancel);
			
			this.add(f);
			this.setLocationRelativeTo(null);
		}
	}
	private void clearText(){
		urlFormat.setText("");
		name.setText("");
		driverClass.setText("");
		fileUrl.setText("");
	}
	
	class SaveJdbcConfigMouse extends  MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			DriverPathInfo info = new DriverPathInfo(name.getText(), urlFormat.getText(), driverClass.getText(), fileUrl.getText());
			JButton source = (JButton) e.getSource();
			
			if("保存".equals(source.getText())){
				try {
					boolean addJbdcConfig = xmlJdbcConfig.addDriverPaths(info,oldJdbcPathInfo);
					if(addJbdcConfig){
						DriverPathInfo[] driverPaths = getDriverPaths();
						jdbdList.setListData(driverPaths);
						jScrollPane.updateUI();
						oldJdbcPathInfo = null;
						dialog.setVisible(false);
					}
					else{
						if(oldJdbcPathInfo==null){
							JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "驱动名称已存在，请修改名称后重新保存！");
							name.selectAll();
							name.grabFocus();
						}
						else{
							JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "修改驱动信息已不存在，请确认后在进行修改！");
//							oldJdbcPathInfo = xmlJdbcConfig.getJdbcList();
							dialog.setVisible(false);
						}
					}
				
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else{
				dialog.setVisible(false);
			}
		}
	}
}

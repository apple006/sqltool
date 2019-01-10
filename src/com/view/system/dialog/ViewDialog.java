package com.view.system.dialog;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.tree.TreeModel;

import main.SQLTool;

import com.entity.DataBaseType;
import com.entity.DriverPathInfo;
import com.entity.LoginInfo;
import com.ui.XMLConnectionsConfig;
import com.ui.tree.IconMutableTreeNode;
import com.ui.tree.UITree;
import com.view.sqloperate.Controller;
import com.view.system.perferenc.xml.XMLJdbcConfig;

/**
 * 新建连接数据库对话框
 * 
 * @author Administrator
 * 
 */
public class ViewDialog extends JDialog{

	
	
	private final int WIDTH =300;
	private final int HEIGHT = 230;
	private final JFrame parent;

	public ViewDialog(JFrame jFrame) {
		super( jFrame, "连接数据库", true);
		this.parent = jFrame;
		init();
	}

	public ViewDialog() {
		this(SQLTool.getSQLTool().getToolFrame().getFrame());
	}

	public ViewDialog(IconMutableTreeNode treeNode) {
		this(SQLTool.getSQLTool().getToolFrame().getFrame());
	}
	private static final long serialVersionUID = 1L;

	private void init() {
		setSize(WIDTH, HEIGHT);
	}
	

	@Override
	public void setVisible(boolean paramBoolean) {
		super.setVisible(paramBoolean);
		
	}
	
	@Override
	public String toString() {
		// TODO"";
		return "";
	}
}



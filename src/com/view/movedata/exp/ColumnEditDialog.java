package com.view.movedata.exp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 列默认值编辑弹出框
 * @author whh
 *
 */
public class ColumnEditDialog extends JDialog{
	
	public ColumnEditDialog(){
		this.setLayout(new BorderLayout());
		 JPanel one = new JPanel(new FlowLayout(FlowLayout.LEFT));
		 JComboBox check = new JComboBox();
//		 check.setEditable(true);;
		 check.addItem("自增字符串主键");
		 check.addItem("自增数字主键");
		 check.addItem("日期");
		 check.addItem("常量数值");
		 check.addItem("随机常量数值");
		 check.addItem("随机字符串");
		 one.add(new JLabel("生成方式:"));
		 one.add(check);
		 add(one,BorderLayout.NORTH);
		 
		 JPanel two = new JPanel(new GridLayout(2,1 ));
		 JPanel two1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		 JPanel two2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		 two1.add(new JLabel("起始值/常量值"));
		 two1.add(new JTextField(10));
		 two2.add(new JLabel("日期格式"));
		 two.add(two1);
		 two.add(two2);
		 
		 add(one,BorderLayout.NORTH);
		 add(two,BorderLayout.CENTER);
		 this.setSize(200, 150);
	}
	
	
	public static void main(String[] args) {
		new ColumnEditDialog().show();
	}
}


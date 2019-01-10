package com.view.movedata.exp.ui.dialog;


import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.view.movedata.exp.ui.IPop;
import com.view.tool.UUID;

public class Groupcolumn  implements IPop{
	private JTextField start;
	private JTextField digitTF;
	private JPanel panel;
	public Groupcolumn(Object object){
		panel =new JPanel(null);
		JLabel jLabel = new JLabel("常量值：");
		start = new JTextField(20);
		JLabel note = new JLabel("*只支持 0-9");
		note.setForeground(Color.red);
		note.setFont(new Font(note.getFont().getName(), Font.BOLD, 10));
		JLabel digitLa = new JLabel("编号长度：");
		 digitTF = new JTextField(20);
		jLabel.setBounds(5, 5, 200, 25);
		start.setBounds(80, 5, 200, 25);
		note.setBounds(290, 5, 100, 25);
		digitLa.setBounds(5, 35, 200, 25);
		digitTF.setBounds(80, 35, 200, 25);
		panel.add(jLabel);
		panel.add(start);
		panel.add(note);
		panel.add(digitLa);
		panel.add(digitTF);
		
		if(object instanceof UUID){
			start.setText(((UUID)object).getStart());
			digitTF.setText(((UUID)object).getLength()+"");
		}
	}
	
	@Override
	public Object getObject() {
		UUID uuid = new UUID(start.getText().trim(), new Integer(digitTF.getText()));
		return uuid;
	}

	@Override
	public JPanel getJPanel() {
		return panel;
	}
}
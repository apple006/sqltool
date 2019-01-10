package com.view.movedata.exp.ui.dialog;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.view.movedata.exp.ui.IPop;

public class Final implements IPop{
	private JTextField field;
	private JPanel panel;
	public Final(Object object){
		panel = new JPanel(null);
		JLabel jLabel = new JLabel("常量值：");
		field = new JTextField(20);
		JLabel note = new JLabel("*常量");
		note.setForeground(Color.red);
		note.setFont(new Font(note.getFont().getName(), Font.BOLD, 10));
		jLabel.setBounds(5, 5, 200, 25);
		field.setBounds(80, 5, 200, 25);
		note.setBounds(290, 5, 100, 25);
		panel.add(jLabel);
		panel.add(field);
		panel.add(note);
		field.setText(object==null?"":object.toString());
	}
	
	@Override
	public Object getObject() {
		return field.getText();
	}

	@Override
	public JPanel getJPanel() {
		return panel;
	}
}
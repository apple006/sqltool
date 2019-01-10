package com.view.movedata.exp.ui.dialog;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.util.UtilDate;
import com.view.movedata.exp.entity.NowDate;
import com.view.movedata.exp.ui.IPop;

public class NewTime implements IPop {

	private JPanel panel;

	private ButtonGroup group;
	private JRadioButton b1,b2,b3;
	public NewTime(Object object) {
		panel = new JPanel(null);
		b1 = new JRadioButton("yyyy-MM-dd hh:mm:ss");
		JRadioButton b2 = new JRadioButton("yyyy-MM-dd");
		JRadioButton b3 = new JRadioButton("hh:mm:ss");
		group = new ButtonGroup();
		group.add(b1);
		group.add(b2);
		group.add(b3);
		
		panel.add(b1);
		panel.add(b2);
		panel.add(b3);
		b1.setBounds(5, 5, 200, 25);
		b2.setBounds(5, 50, 200, 25);
		b3.setBounds(5, 100, 200, 25);
	}

	@Override
	public Object getObject() {
		if(b1.isSelected()){
			return	new NowDate("yyyy-MM-dd hh:mm:ss");
		}else if(b2.isSelected()){
			return new NowDate("yyyy-MM-dd");
		}else{
		 	return new NowDate("hh:mm:ss");
		}
	}

	@Override
	public JPanel getJPanel() {
		return panel;
	}

}

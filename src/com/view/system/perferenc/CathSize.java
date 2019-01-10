package com.view.system.perferenc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.view.tool.HelpProperties;

@SuppressWarnings("serial")
public class CathSize extends JPanel {
//	private 
	public CathSize(){
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel jLabel = new JLabel("读取数据量：");
		final JTextField field = new JTextField(20);
		String getValueByKey = HelpProperties.GetValueByKey("keyvalue.properties", "fetchSize");
		field.setText(getValueByKey);
		JLabel note = new JLabel("*数据迁移时从表取出数据量大小");
		note.setForeground(Color.red);
		note.setFont(new Font(note.getFont().getName(), Font.BOLD, 11));
		JLabel digitLa = new JLabel("缓存包数量：");
		final JTextField digitTF = new JTextField(20);
		digitTF.setText(HelpProperties.GetValueByKey("keyvalue.properties", "cathSize"));
		JLabel note1 = new JLabel("*数据迁移时中间数据缓存量");
		note1.setForeground(Color.red);
		note1.setFont(new Font(note.getFont().getName(), Font.BOLD, 11));

		JLabel count = new JLabel("每个包缓存量：");
		final JTextField countTF = new JTextField(20);
		countTF.setText(HelpProperties.GetValueByKey("keyvalue.properties", "count"));
		
		JLabel rowCount = new JLabel("执行查询行数：");
		final JTextField rowCountTF = new JTextField(20);
		rowCountTF.setText(HelpProperties.GetValueByKey("keyvalue.properties", "count"));
		
		JLabel countEachTable = new JLabel("是否统计每个表量：");
//		final JTextField countEachTableTF = new JTextField(20);
		final JRadioButton y = new JRadioButton("是"); 
		final JRadioButton n = new JRadioButton("否"); 
		ButtonGroup group = new ButtonGroup();
		group.add(y);
		group.add(n);
		JLabel commentL = new JLabel("pdm关联字段：");
//		final JTextField countEachTableTF = new JTextField(20);
		final JRadioButton comment = new JRadioButton("comment"); 
		final JRadioButton name = new JRadioButton("name"); 
		ButtonGroup commentG = new ButtonGroup();
		commentG.add(comment);
		commentG.add(name);
		String select = (HelpProperties.GetValueByKey("keyvalue.properties", "countEachTable"));
		if("Y".equals(select)){
			y.setSelected(true);
		}else{
			n.setSelected(true);
		}
		
		
		JLabel pdm_show = new JLabel("是否开启PDM联想：");
//		final JTextField countEachTableTF = new JTextField(20);
		final JRadioButton pdm_showy = new JRadioButton("是"); 
		final JRadioButton pdm_shown = new JRadioButton("否"); 
		ButtonGroup pdm_showgroup = new ButtonGroup();
		pdm_showgroup.add(pdm_showy);
		pdm_showgroup.add(pdm_shown);
		String select1 = (HelpProperties.GetValueByKey("keyvalue.properties", "pdm_show"));
		if("Y".equals(select1)){
			pdm_showy.setSelected(true);
		}else{
			pdm_shown.setSelected(true);
		}
//		JLabel note1 = new JLabel("*从表取出数据量大小");
//		note1.setForeground(Color.red);
//		note1.setFont(new Font(note.getFont().getName(), Font.BOLD, 10));
		jLabel.setBounds(5, 5, 210, 25);
		field.setBounds(90, 5, 210, 25);
		note.setBounds(310, 5, 100, 25);
		digitLa.setBounds(5, 35, 210, 25);
		digitTF.setBounds(90, 35, 210, 25);
		count.setBounds(5, 65, 210, 25);
		countTF.setBounds(90, 65, 210, 25);
		rowCount.setBounds(5, 95, 210, 25);
		rowCountTF.setBounds(90, 95, 210, 25);
		countEachTable.setBounds(5, 125, 210, 25);
		y.setBounds(115, 125, 50, 25);
		n.setBounds(165, 125, 50, 25);
		
		pdm_show.setBounds(5, 155, 210, 25);
		pdm_showy.setBounds(115, 155, 50, 25);
		pdm_shown.setBounds(165, 155, 50, 25);
		
		panel.add(jLabel);
		panel.add(field);
		panel.add(note);
		panel.add(digitLa);
		panel.add(digitTF);
		panel.add(count);
		panel.add(countTF);
		panel.add(rowCount);
		panel.add(rowCountTF);
		
		panel.add(countEachTable);
		panel.add(y);
		panel.add(n);
		
		panel.add(pdm_show);
		panel.add(pdm_showy);
		panel.add(pdm_shown);
		
		
		
		this.add(panel,BorderLayout.CENTER);
		JButton save = new JButton("应用");
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel1.add(save);
		this.add(panel1,BorderLayout.SOUTH);
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HelpProperties.WriteProperties("keyvalue.properties","fetchSize",field.getText());
				HelpProperties.WriteProperties("keyvalue.properties","cathSize",digitTF.getText());
				HelpProperties.WriteProperties("keyvalue.properties","count",countTF.getText());
				HelpProperties.WriteProperties("keyvalue.properties","count",rowCountTF.getText());
				HelpProperties.WriteProperties("keyvalue.properties","countEachTable",y.isSelected()?"Y":"N");
				HelpProperties.WriteProperties("keyvalue.properties","pdm_show",y.isSelected()?"Y":"N");

			}
		});
		
	}
}  

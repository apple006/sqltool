package com.view.movedata.exp.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.prompt.DBKeyTextPane;
import com.view.movedata.exp.entity.DataFromBean;
import com.view.movedata.exp.ui.IPop;
import com.view.sqloperate.LineNumberHeaderView;
import com.view.tool.HelpProperties;

public class EdisFromData implements IPop{
	private JPanel panel1;
	private DefaultTableModel listValue;
	private DBKeyTextPane editSql;
	private JTextField codeText;
	private JTextField describe;
	private DataFromBean data;
	
	private JRadioButton sql;
	public EdisFromData(Object object){
		panel1 = new JPanel(new BorderLayout());
		editSql = new DBKeyTextPane();
		if(object!=null){
			data= (DataFromBean) object;
			editSql.setText(data !=null?data.getSql():"");
		}
		JScrollPane selectScrol = new JScrollPane(editSql,   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		String current_fontName = HelpProperties.GetValueByKey("keyvalue.properties","current_fontName" );
    	String current_fontSize = HelpProperties.GetValueByKey("keyvalue.properties","current_fontSize" );
		Font font= new Font(current_fontName, 10, new Integer(current_fontSize));
		LineNumberHeaderView lineNumberHeaderView = new LineNumberHeaderView();
		editSql.setFont(font);
		lineNumberHeaderView.setFont(editSql.getFont());
		selectScrol.setRowHeaderView(lineNumberHeaderView);
		lineNumberHeaderView.setLineHeight(editSql.getFontMetrics(font).getHeight());
		
		panel1.add(selectScrol,BorderLayout.CENTER);
		
		JPanel panel3 = initButtons();
		panel1.add(panel3,BorderLayout.EAST);
		JPanel panel2 = initDescribe();
		panel1.add(panel2,BorderLayout.NORTH);

		
	}

	private  JPanel  initInputPanel() {
		 JPanel panel=new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		 codeText = new JTextField(30);
		 codeText.setText(data !=null?data.getSql():"");
		 codeText.setEditable(false);
		 return panel;

	}

	private JPanel initButtons() {
		 JPanel panel=new JPanel(); 
		 BoxLayout layout=new BoxLayout(panel, BoxLayout.Y_AXIS); 
		 panel.setLayout(layout);

		return panel;
	}


	private JPanel initDescribe() {
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel group = new JLabel("∂®÷∆√Ë ˆ");
		describe = new JTextField(40);
		describe.setText(data !=null?data.getName():"");
		panel2.add(group);
		panel2.add(describe);
		return panel2;
	}
	@Override
	public Object getObject() {
		String sql = editSql.getText();
		String title = describe.getText();
		DataFromBean bean = new DataFromBean(title,sql);
		return bean;
	}

	@Override
	public JPanel getJPanel() {
		return panel1;
	}
	
	
	public static void main(String[] args) {
		Random rand = new Random();
		for (int i = 0; i < 1000; i++) {
			System.out.println(rand.nextInt(100));
			
		}
	}
}


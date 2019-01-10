package com.view.movedata.exp.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.view.movedata.exp.entity.ColumnType;
import com.view.movedata.exp.ui.IPop;

public class ConvertColumnDialog extends JDialog{

	private static final int WIDE = 600;
	private static final int HEIGHT = 300;
	private JButton ok = new JButton("确定");
	private JButton close = new JButton("取消");
	private String type;
	private IPop pop;
	private boolean isOk ;
	private boolean showClose =true ;

	public ConvertColumnDialog(Frame  comp ,String text){
		super(comp,text,true);
		this.setSize(WIDE, HEIGHT);
		setLayout(new BorderLayout());
		JPanel iniBut =getButs();
		add(iniBut,BorderLayout.SOUTH);
	}
	public ConvertColumnDialog(Frame  comp ,String text,boolean showClose){
		super(comp,text,true);
		this.setSize(WIDE, HEIGHT);
		setLayout(new BorderLayout());
		JPanel iniBut =getButs();
		add(iniBut,BorderLayout.SOUTH);
		this.showClose = showClose;
	}
	private JPanel getButs() {
		JPanel  panel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isOk = true;
				ConvertColumnDialog.this.setVisible(false);
			}
		});
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isOk = false;
				ConvertColumnDialog.this.setVisible(false);
			}
		});
		if(showClose){
			panel.add(close);
			panel.add(ok);
		}
		return panel;
	}
	
	private JPanel initFinal(Object object){
		pop = new Final(object);
		return pop.getJPanel();
	}
	public Object showDialog(String columnType, Object object) throws InterruptedException {
		type = columnType;
		if(ColumnType.FINAL.equals(columnType)){
			add(initFinal(object),BorderLayout.CENTER);
		}else if(ColumnType.GROUPCOLUMN.equals(columnType)){
			add(getGroupcolumn(object),BorderLayout.CENTER);
		}else if(ColumnType.TEST.equals(columnType)){
			add(initTest(object),BorderLayout.CENTER);
		}else if(ColumnType.NOW_TIME.equals(columnType)){
			add(initNowTime(object),BorderLayout.CENTER);
		}else if(ColumnType.CUSTOM_MADE.equals(columnType)){
			add(initCustomMMade(object),BorderLayout.CENTER);
		}else if("订制执行脚本".equals(columnType)){
			add(initCustomMMade(object),BorderLayout.CENTER);
		}else if(ColumnType.ALL_CHANGE.equals(columnType)){
			add(initAllChange(object),BorderLayout.CENTER);
		}
		setLocationRelativeTo(null);

		super.show();
		if(isOk){
			isOk = false;
			return getOK();
		}
		return null;
	}	

	private Component initAllChange(Object object) {
		pop = new BindVariables(object);
		return pop.getJPanel();
	}

	private Component initCustomMMade(Object object) {
		pop = new EdisFromData(object);
		return pop.getJPanel();
	}

	private JPanel initTest(Object object) {
		pop = new Test(object);
		return pop.getJPanel();
	}
	private JPanel initNowTime(Object object) {
		pop = new NewTime(object);
		return pop.getJPanel();
	}
	
	public Object getOK(){
		this.setVisible(false);
		return pop.getObject();
	}

	private JPanel getGroupcolumn(Object object) {
		pop = new Groupcolumn(object);
		return pop.getJPanel();
	}
	
	public static void main(String[] args) throws InterruptedException {
//		ConvertColumnDialog a= new ConvertColumnDialog();
//		Object show = a.show(ColumnType.TEST);
//		System.out.println("aaaa");
	}
}





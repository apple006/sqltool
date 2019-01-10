package com.ui.panel.border;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.ui.panel.Border;
import com.ui.panel.JPanelSplit;

public abstract class AbsBorderPanel extends JPanel implements Border,MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel theLeftBorder;
	protected JPanel theMainBorder;
	protected JPanel theRightBorder;
	protected JPanel theDownBorder;
	protected JPanel theUpBorder;
	
	protected int dividerLocation = 200;
	private  int borderType = Border.ROUTINE;
	public AbsBorderPanel(){
		initInner();
		init();
	}
	public AbsBorderPanel(int borderType){
		this.borderType = borderType;
		initInner();
		
	}
	private void initInner(){
		setLayout(new BorderLayout());
		theLeftBorder = new LeftBorder();
		theMainBorder = new MainBorder();
		theRightBorder = new RightBorder();
		theDownBorder = new DownBorder();
		theUpBorder = new UpBorder();
		initBasic();
	}
	private void initBasic(){
		if(getBorderType()==Border.ROUTINE){
			initRoutinePanel();
		}
		if(getBorderType()==Border.MOVEMENT){
			initMoveMentPanel();
		}
	}
	
	
	
	/**
	 * 初始化固定窗体
	 */
	private void initRoutinePanel(){
		
		add(theLeftBorder,BorderLayout.WEST);
		add(theUpBorder,BorderLayout.NORTH);
		add(theDownBorder,BorderLayout.SOUTH);
		add(theRightBorder,BorderLayout.EAST);
		add(theMainBorder,BorderLayout.CENTER);
	}
	/**
	 * 如需自定义可移动分隔窗体重写此方法
	 */
	protected void initMoveMentPanel(){
		JPanelSplit lr1 = new JPanelSplit(JPanelSplit.HORIZONTAL_SPLIT);
		lr1.setDividerLocation(300);
		lr1.addLeft(theLeftBorder);
		lr1.addRight(theRightBorder);
		add(lr1,BorderLayout.CENTER);
	}
	
	@Override
	public void setBorderType(int borderType) {
		this.borderType = borderType;
	}
	
	@Override
	public int getBorderType(){
		return borderType;
	}
	@Override
	public void addLeftPanel(Component comp) {
		if(Border.MOVEMENT==borderType){
			
		}
		theLeftBorder.add(comp,BorderLayout.CENTER);
		this.repaint();
	}
	@Override
	public void addRightPanel(Component comp) {
		theRightBorder.add(comp,BorderLayout.CENTER);
		this.repaint();
	}

	@Override
	public void addUpPanel(Component comp) {
		theUpBorder.add(comp);
		this.repaint();

	}

	@Override
	public void addDownPanel(Component comp) {
		theDownBorder.add(comp);
	}

	@Override
	public void addMainPanel(Component comp) {
		theMainBorder.add(comp);
	}
	@Override
	public JPanel getLeftPanel() {
		return theLeftBorder;
	}

	@Override
	public JPanel getRightPanel() {
		return theRightBorder;
	}

	@Override
	public JPanel getUpPanel() {
		return theUpBorder;

	}

	@Override
	public JPanel getDownPanel() {
		return theDownBorder ;
	}

	@Override
	public JPanel getMainPanel() {
		return theMainBorder ;
	}

	
}

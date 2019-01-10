//Source file: E:\\workspace\\com\\ui\\border\\BorderPanel.java

package com.ui.panel.border;

import java.awt.event.MouseEvent;

import com.ui.panel.JPanelSplit;

/**
 * 带有边界的JPanel扩展
 * @author wang.hh
 *
 */
public class BorderPanel extends AbsBorderPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * 将主界面分为左、中、右、下四个区域
	 * @borderType 边界面板种类 可以活动边界面板
	 */
	public BorderPanel(int borderType){
		super(borderType);
	}

	/**
	 * 初始话面板
	 * 
	 */
	public void init() {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("进入");
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("退出");
		
	}

	


	
	
	
	

}

package com.ui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 * 所有菜单均实现次接口
 * @author Administrator
 */
public  class MenuListenner implements MouseListener,KeyListener,ActionListener{
	
	private boolean isRevoke = false;
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		MouseOnClick(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		isRevoke = false;
			
	}

	@Override
	public void mousePressed(MouseEvent e) {
		isRevoke = true;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isRevoke){
			MouseOnClick(e);
		}
		isRevoke = false;
	}

	/**
	 * 当对菜单和弹出菜单时实现此方法
	 * @param e
	 */
	public  void MouseOnClick(MouseEvent e){
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

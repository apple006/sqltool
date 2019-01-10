package com.jtattoo.plaf.aero;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.ComponentUI;

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.BaseTreeUI;

public class AeroTreeUI extends BaseTreeUI {
	
	 public static ComponentUI createUI(JComponent c) { 
	       return new AeroTreeUI(); 
	   }
	@Override
	public void paint(Graphics g, JComponent c) {
		// TODO Auto-generated method stub
		tree.setBackground(AbstractLookAndFeel.getTheme().getBackgroundColor());
		super.paint(g, c);
	}
	
	
	
	
}

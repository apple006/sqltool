//Source file: E:\\workspace\\com\\ui\\border\\HorizontalSplitPane.java

package com.ui.panel.border;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import com.ui.panel.IJSplitPane;

/**
 * 水平可移动分割面板
 */
public abstract class HorizontalSplitPane extends JPanel implements IJSplitPane {
	private static int dividerLocation = 200;
	private JSplitPane split;

	public HorizontalSplitPane(){
		this(dividerLocation);
	}
	public HorizontalSplitPane(int location){
		dividerLocation = location;
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setDividerLocation(dividerLocation);
		setLayout(new BorderLayout());
		add(split, BorderLayout.CENTER);
		init();
	}
	public HorizontalSplitPane(Component left, Component right,int location) {
		initInner(left,right);
		init();
	}
	private void initInner(Component left, Component right){
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setDividerLocation(dividerLocation);
		split.add(left);
		split.add(right);
		setLayout(new BorderLayout());
		add(split, BorderLayout.CENTER);
	}

	public void setLeftPanel(Component comp) {
		split.setLeftComponent(comp);
	}
	public void setRightPanel(Component comp) {
		split.setRightComponent(comp);
	}


	public void setDownPanel(Component comp) {
		setRightPanel(comp);
	}

	public void setUpPanel(Component comp) {
		setRightPanel(comp);
	}
}

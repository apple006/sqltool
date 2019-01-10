//Source file: E:\\workspace\\com\\ui\\border\\VerticalSplitPane.java

package com.ui.panel.border;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.ui.panel.IJSplitPane;

/**
 * 垂直移动分割面板
 */
public abstract class VerticalSplitPane extends JPanel implements IJSplitPane {
	private int dividerLocation = 200;
	private JSplitPane split;
	public VerticalSplitPane(){
		this(JSplitPane.VERTICAL_SPLIT);
	}
	public VerticalSplitPane(int location) {
		dividerLocation = location;
		split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.setDividerLocation(dividerLocation);
		setLayout(new BorderLayout());
		add(split, BorderLayout.CENTER);
		init();
	}

	/**
	 * 
	 * @param up
	 *            设置上面面板
	 * @param dow
	 *            设置下面面板
	 */
	public VerticalSplitPane(Component up, Component dow) {
		split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.setAlignmentX(100);
		split.add(up);
		split.add(dow);
		init();
	}

	/**
	 * 设置下方容器
	 * 
	 * @param comp
	 * @roseuid 5282E1600292
	 */
	public void setDownPanel(Component comp) {
		split.setRightComponent(comp);
	}

	/**
	 * 设置上方容器
	 * 
	 * @param comp
	 * @roseuid 5282E16002A2
	 */
	public void setUpPanel(Component comp) {
		split.setLeftComponent(comp);
	}

	@Override
	public void setLeftPanel(Component comp) {
		setUpPanel(comp);
	}

	@Override
	public void setRightPanel(Component comp) {
		setDownPanel(comp);
	}
}

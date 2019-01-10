package com.control;

import javax.swing.tree.MutableTreeNode;

import com.ui.tree.IconMutableTreeNode;

/**
 * 执行树节点点击能操作功能接口，子类针对不同节点实现不同操作功能，
 * @author Administrator
 */
public interface ITreeAction {
	/**
	 * 执行单击操作
	 */
	public void doClick();
	/**
	 * 执行双击操作
	 */
	void doDoubleClick(IconMutableTreeNode treeNode);
}

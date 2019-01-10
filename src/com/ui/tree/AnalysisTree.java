package com.ui.tree;

import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 所有生成树方式的接口
 * @author Administrator
 *
 */
public interface AnalysisTree {
	/**
	 * 生成树根
	 * @return
	 */
	IconMutableTreeNode addChilds();

	IconMutableTreeNode addChild() throws IOException;
}

package com.ui.tree;

import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.prompt.PromptLabel;

/**
 * 自定义树描述类,将树的每个节点设置成不同的图标
 * 
 * @author RuiLin.Xie - xKF24276
 * 
 */

public class DataBaseTreeCellRenderer extends DefaultTreeCellRenderer  {

	/**
	 * ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 重写父类DefaultTreeCellRenderer的方法
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		// 得到每个节点的TreeNode
		IconMutableTreeNode node = (IconMutableTreeNode) value;
		// 执行父类原型操作
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		if(node.getUserObject() instanceof PromptLabel){
			PromptLabel label =(PromptLabel)node.getUserObject();
			if(sel){
				label.setSelect(true);
			}else{
				label.setSelect(false);
			}
			setText(label.toString());
		}else{
			setText(value.toString());
		}
		if (sel) {
			setForeground(getTextSelectionColor());
		} else {
			setForeground(getTextNonSelectionColor());
		}

	
		setOpaque(false);
		// 得到每个节点的text
		String str = node.toString();

		// 判断是哪个文本的节点设置对应的值（这里如果节点传入的是一个实体,则可以根据实体里面的一个类型属性来显示对应的图标）
		this.setIcon(node.getIcon());

		return this;
	}
	
	
	

}

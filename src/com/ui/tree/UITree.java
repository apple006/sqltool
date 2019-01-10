package com.ui.tree;

import java.awt.Component;
import java.io.IOException;
import java.util.EventObject;
import java.util.Hashtable;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.RowFilter;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

public class UITree extends JTree {


	private static final long serialVersionUID = 1L;
	private static AnalysisTree factory ;

	public UITree(AnalysisTree init) {
		super(init.addChilds());
		factory = init;
		DataBaseTreeCellRenderer dataBaseTreeCellRenderer = new DataBaseTreeCellRenderer();
		this.setCellRenderer(dataBaseTreeCellRenderer);
	}

	public UITree(Hashtable sh) {
		super(sh);
	}
	
	private void init(){
	}
	public void removeChild(IconMutableTreeNode child ,DefaultMutableTreeNode parent){
		DefaultTreeModel  defualt = (DefaultTreeModel) this.getModel();
//		defualt.re(child,parent, 0);.
	}
	public void removeNode(IconMutableTreeNode node){
		((DefaultTreeModel) this.getModel()).removeNodeFromParent(node);
//		this.updateUI();
//		defualt.re(child,parent, 0);.
	}

	public void addChild(IconMutableTreeNode child ,DefaultMutableTreeNode parent){
		DefaultTreeModel  defualt = (DefaultTreeModel) this.getModel();
		defualt.insertNodeInto(child,parent, 0);
	}
	public void reoveChild(IconMutableTreeNode child ,DefaultMutableTreeNode parent){
		DefaultTreeModel  defualt = (DefaultTreeModel) this.getModel();
	}

	public DefaultMutableTreeNode addChild() throws IOException {
		DefaultTreeModel  defualt = (DefaultTreeModel) this.getModel();
		IconMutableTreeNode addChild = factory.addChild();
		defualt.insertNodeInto(addChild, (MutableTreeNode) this.getModel().getRoot(), 0);
		return addChild;
	}
	
	
}

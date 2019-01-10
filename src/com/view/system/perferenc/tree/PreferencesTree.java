package com.view.system.perferenc.tree;


import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.ui.tree.AnalysisTree;
import com.ui.tree.DataBaseTreeCellRenderer;
import com.view.system.perferenc.tree.xml.XMLPreferencesTree;



public class PreferencesTree extends JTree {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static AnalysisTree factory = XMLPreferencesTree.getPreferencesTree();
	
	
	public PreferencesTree() {
		super(factory.addChilds());
		DataBaseTreeCellRenderer dataBaseTreeCellRenderer = new DataBaseTreeCellRenderer();
		this.setCellRenderer(dataBaseTreeCellRenderer);
		this.repaint();
	}

	public PreferencesTree(Hashtable sh) {
		super(sh);
	}
	
	private void init(){
		
	}

	public DefaultMutableTreeNode addChild() throws IOException {
		DefaultTreeModel  defualt = (DefaultTreeModel) this.getModel();
		DefaultMutableTreeNode addChild = factory.addChild();
		defualt.insertNodeInto(addChild, (MutableTreeNode) this.getModel().getRoot(), 0);
		return addChild;
	}
	
	
	
}

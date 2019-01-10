package com.view.sqloperate;

import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.dnd.FileToTabDnd;
import com.entity.LoginInfo;
import com.ui.extensible.UITabbedPane;
import com.ui.menu.MenuAndTool;
import com.ui.menu.xml.XMLMenuAndToolFactory;
import com.ui.panel.border.HorizontalSplitPane;
import com.ui.tree.IconMutableTreeNode;
import com.ui.tree.UITree;
import com.ui.tree.xml.XMLAnalysisTree;

/**
 * 查询语句
 * 
 * @author Administrator
 */
public class SqlView extends HorizontalSplitPane {

	private static final long serialVersionUID = 1L;
	/**
	 * 查询tab页
	 */
	private UITabbedPane tabQuerySql;
	private UITabbedPane tabConn;
	/**
	 * 数据库信息树
	 */
	private UITree dataBaseTree;
	private MenuAndTool menu_tool;
	private JPopupMenu connection_pop;
	private JPopupMenu database_pop;
	private JPopupMenu databaseOther_pop;
	private  List<LoginInfo>	conns;
	

	@Override
	public void init() {
		dataBaseTree = initTree();
	}
	
	

	public List<LoginInfo> getConns() {
		return conns;
	}

	public void setConns(List<LoginInfo> conns) {
		this.conns = conns;
	}



	private UITree initTree() {
		
		menu_tool = XMLMenuAndToolFactory.getXMLMenuAndToolFactory();
		database_pop = menu_tool.getPopupMenu("dataBase");
		connection_pop = menu_tool.getPopupMenu("connection");
		databaseOther_pop = menu_tool.getPopupMenu("databaseOther");
		UITree sqlCom = new UITree(new XMLAnalysisTree());
		sqlCom.setEditable(false);
		sqlCom.setDragEnabled(false);
		JScrollPane sp = new JScrollPane();
		sp.getViewport().add(sqlCom);
		tabConn = new UITabbedPane(JTabbedPane.TOP);
		tabConn.addTab("数据连接", sp);
		setLeftPanel(tabConn);
		tabQuerySql = new UITabbedPane(JTabbedPane.TOP);
		new DropTarget(tabQuerySql, DnDConstants.ACTION_COPY_OR_MOVE,new FileToTabDnd());
       
		setRightPanel(tabQuerySql);
		sqlCom.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent paramMouseEvent) {
			}

			@Override
			public void mousePressed(MouseEvent paramMouseEvent) {
			}

			@Override
			public void mouseExited(MouseEvent paramMouseEvent) {
			}

			@Override
			public void mouseEntered(MouseEvent paramMouseEvent) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				Point point = e.getPoint();
//				TreePath pathForLocation = dataBaseTree.getPathForLocation(
//						e.getX(), e.getY());

//				dataBaseTree.setSelectionPath(pathForLocation);
				if (MouseEvent.BUTTON3 == e.getButton()
						&& e.getClickCount() == 1) {
					TreePath selectionPath =dataBaseTree.getPathForLocation(e.getX(), e.getY());
					if (selectionPath != null) {
						IconMutableTreeNode node = (IconMutableTreeNode) selectionPath
								.getLastPathComponent();
						node.getDoAction().doClick();
						IconMutableTreeNode parent = (IconMutableTreeNode) node
								.getParent();
						if(parent==null){
							return;
						}
						if (parent.isRoot()) {
							database_pop.show(dataBaseTree, e.getX(), e.getY());
						} else {
							databaseOther_pop.show(dataBaseTree, e.getX(),
									e.getY());
						}
					} else {
						connection_pop.show(dataBaseTree, e.getX(), e.getY());
					}

				}
				if (MouseEvent.BUTTON1 == e.getButton()&& e.getClickCount() == 2) {
					TreePath selectionPath = dataBaseTree.getSelectionPath();
					dataBaseTree.setSelectionPath(selectionPath);
//					IconMutableTreeNode node = (IconMutableTreeNode) selectionPath.getLastPathComponent();
//					node.getDoAction().doDoubleClick(node);

				}
			}
		});
//		sqlCom.addTreeWillExpandListener(new TreeWillExpandListener() {
//			int n ;
//
//			@Override
//			public void treeWillExpand(TreeExpansionEvent event)
//					throws ExpandVetoException {
//			}
//
//			@Override
//			public void treeWillCollapse(TreeExpansionEvent event)
//					throws ExpandVetoException {
//				System.out.println("关闭");
//			}
//		});

		return sqlCom;
	}
	
	
	 private void expandAll(JTree tree, TreePath parent, boolean expand) {
			DefaultTreeModel  defualt = (DefaultTreeModel) tree.getModel();
			DefaultMutableTreeNode selectedNode  = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();  
			ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
	        if (selectedNode.getChildCount()>0) {
	            for (Enumeration e = selectedNode.children(); e.hasMoreElements();) {
	            	nodes.add((DefaultMutableTreeNode) e.nextElement());
	            }
	        }
	        for (DefaultMutableTreeNode defaultMutableTreeNode : nodes) {
	        	defualt.removeNodeFromParent(defaultMutableTreeNode);
			}
	    } 

//	public String[] getTables(){
//		
//	}
	public DefaultMutableTreeNode addChild() throws IOException {
		return dataBaseTree.addChild();
	}

	public UITabbedPane getTabQuerySql() {
		return tabQuerySql;
	}

	public UITabbedPane getTabConn() {
		return tabConn;
	}

	public JPopupMenu getConnection_pop() {
		return connection_pop;
	}

	public JPopupMenu getDatabase_pop() {
		return database_pop;
	}

	public JPopupMenu getDatabaseOther_pop() {
		return databaseOther_pop;
	}

	public UITree getDataBaseTree() {
		return dataBaseTree;
	}

}

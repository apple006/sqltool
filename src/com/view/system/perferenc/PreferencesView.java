package com.view.system.perferenc;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import com.entity.PreferencInfo;
import com.ui.panel.border.HorizontalSplitPane;
import com.ui.tree.IconMutableTreeNode;
import com.view.system.perferenc.tree.PreferencesTree;

public class PreferencesView extends HorizontalSplitPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PreferencesTree preferencesTree;
	private JPanel RIghtpanel;
	private static PreferencesView preferencesView;
	private static Object o = new Object();
	private PreferencesView(){
		
	}
	
	public static PreferencesView getPreferencesView() {
		if(preferencesView==null){
			synchronized (o) {
				preferencesView = new PreferencesView();
			}
		}
		return preferencesView;
	}
	@Override
	public void init() {
		preferencesTree = new PreferencesTree();
		BorderLayout borderLayout = new BorderLayout();
		RIghtpanel = new JPanel(borderLayout);
		this.setLeftPanel(preferencesTree);
		this.setRightPanel(RIghtpanel);
		preferencesTree.addMouseListener(new MouseAction());
	}
	class MouseAction extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			 TreePath selectionPath = preferencesTree.getSelectionPath();
			 if(selectionPath!=null){
					IconMutableTreeNode node = (IconMutableTreeNode) selectionPath.getLastPathComponent();
					try {
						String path = ((PreferencInfo)node.getUserObject()).getView();
						if("".equals(path.trim())){
							return;
						}
						setRightPanel((JPanel)Class.forName(path).newInstance());
					} catch (InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 }
		}
	}
	public JPanel getRIghtpanel() {
		return RIghtpanel;
	}


	public void showPreferences(String userObject) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		this.setRightPanel((JPanel)Class.forName(userObject).newInstance());
	}
	
	

}

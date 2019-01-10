package com.view.system.perferenc.tree.action;


import com.control.ITreeAction;
import com.entity.PreferencInfo;
import com.ui.tree.IconMutableTreeNode;
import com.view.system.perferenc.PreferencesView;

public class PreferencesTreeAction implements ITreeAction{

	@Override
	public void doClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDoubleClick(IconMutableTreeNode treeNode) {
		try {
			PreferencesView.getPreferencesView().showPreferences(((PreferencInfo)treeNode.getUserObject()).getView());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

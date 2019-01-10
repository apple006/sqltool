package com.control.doaction.tree;
import com.control.ITreeAction;
import com.entity.LoginInfo;
import com.ui.tree.IconMutableTreeNode;
import com.view.system.dialog.LogonView;

public class DoTreeNodeAction implements ITreeAction{

	@Override
	public void doClick() {
		
	}

	@Override
	public void doDoubleClick(IconMutableTreeNode treeNode) {
		LogonView logon = new LogonView();
		LoginInfo loginInfo = (LoginInfo)treeNode.getUserObject();
//		if(!loginInfo.getIsLive()){
			logon.setTreeNode(treeNode);
//		}
	}
}

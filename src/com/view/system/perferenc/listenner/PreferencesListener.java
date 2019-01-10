package com.view.system.perferenc.listenner;


import java.awt.event.MouseEvent;

import com.ui.MenuListenner;
import com.util.DynamicDialog;
import com.view.system.perferenc.PreferencesView;

/**
 * Ê×Ñ¡Ïî¼àÌý
 * @author Administrator
 *
 */
public class PreferencesListener extends MenuListenner{
	@Override
	public void MouseOnClick(MouseEvent e) {
		PreferencesView a =  PreferencesView.getPreferencesView();
		
		DynamicDialog.getDynamicDialog().setRootPanel(a,600, 400);
	}
}

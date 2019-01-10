package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

public class KeyStrokeTest extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new KeyStrokeTest();
	}

	public KeyStrokeTest() {
		setVisible(true);
		setSize(300, 300);
		
//		setSize(100, 100);
		JTextPane jToolBar = new JTextPane();
		JButton jb1 = new JButton("aa");
		JButton jb2 = new JButton("bb");
		jToolBar.add(jb1);
		jToolBar.add(jb2);
		add(jToolBar,BorderLayout.CENTER);
		

		ActionMap actionmap = jToolBar.getActionMap();
		InputMap iMap = jToolBar.getInputMap(JComponent.WHEN_FOCUSED);

		actionmap.put("r", new MyAction('r')); // right
//		JButton jButton = new JButton(new MyAction('r'));
//		this.getRootPane().setDefaultButton(jButton);
//		add(jButton,BorderLayout.EAST);
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "r");
	}
}

class MyAction implements Action {
	char r;

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(r);
	}

	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	public MyAction(char r) {
		this.r = r;
	}

}
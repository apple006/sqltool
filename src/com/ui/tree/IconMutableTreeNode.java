package com.ui.tree;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import main.SQLTool;

import com.control.ITreeAction;
import com.control.doaction.DefaultDoTreeAction;
import com.dao.ConnectionPool;
import com.entity.LoginInfo;
import com.ico.ImagesManager;
import com.ico.LazyImageIcon;

public class IconMutableTreeNode extends DefaultMutableTreeNode{
	private String action;
	private ITreeAction doaction;
	private Icon icon ;
	public IconMutableTreeNode(Object name,String iconName){
		super(name);
		setUserObject(name);
		icon  = ImagesManager.getImagesManager().getIcon(iconName);
		
	}
	/**
	 * 
	 * @param name 节点名
	 * @param icon 节点图片路径以/包名/picture.png,gif为例
	 * @param action 节点点击执行动作类路径/默认为 DefaultDoTreeAction
	 */
	public IconMutableTreeNode(Object name,String icon,String action){
		this(name,icon);
		this.action = action;
	}
	/**
	 * 惰性加载Action
	 * @return
	 */
	public ITreeAction getDoAction(){
		if(doaction == null){
			try {
				
				if(action==null||"".equals(action.trim())){
					doaction = new DefaultDoTreeAction();
					return doaction;
				}
				doaction = (ITreeAction) Class.forName(action).newInstance();
				return doaction;
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
		return doaction;
	}
	public void setDoAction(ITreeAction doaction){
		this.doaction = doaction;
	}
	
	public void runIoc(){
		Thread th = new Thread(new Runnable() {
			
			LazyImageIcon[] lazy1 = {new LazyImageIcon("/imgs/refresh/1.png"),new LazyImageIcon("/imgs/refresh/2.png"),
					new LazyImageIcon("/imgs/refresh/3.png"),new LazyImageIcon("/imgs/refresh/4.png"),
					new LazyImageIcon("/imgs/refresh/5.png"),new LazyImageIcon("/imgs/refresh/6.png"),
					new LazyImageIcon("/imgs/refresh/7.png"),new LazyImageIcon("/imgs/refresh/8.png")};
			LoginInfo user = (LoginInfo) getUserObject();
			int i =0;
			
			@Override
			public void run() {
				while(user.getIsLive()==ConnectionPool.DENG){
					try {
						Thread.sleep(100);
						icon = lazy1[i];
						if(i<7){
							i++;
						}
						else{
							i=0;
						}
						SQLTool.getSQLTool().getToolFrame().getSqlView().getDataBaseTree().repaint();
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if(user.getIsLive()==ConnectionPool.IS_LIVE){
					icon = new LazyImageIcon("/imgs/dataBaseImgs/onlogdatabase.gif");
				}else if(user.getIsLive()==ConnectionPool.IS_DIE) {
					icon = new LazyImageIcon("/imgs/dataBaseImgs/nologiindatabse.gif");
					SQLTool.getSQLTool().getToolFrame().getSqlView().getDataBaseTree().repaint();
				}
				else{
					JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "登录失败 ："+user.getMessage());
					icon = new LazyImageIcon("/imgs/dataBaseImgs/nologiindatabse.gif");
				}
				SQLTool.getSQLTool().getToolFrame().getSqlView().getDataBaseTree().repaint();
			}
		});
		th.start();
		
		
		
	}

	public Icon getIcon(){
	
		return icon;
	}
}

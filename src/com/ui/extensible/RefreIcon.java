package com.ui.extensible;

import main.SQLTool;

import com.ico.LazyImageIcon;


public class RefreIcon implements Runnable{
	
	private LazyImageIcon[] lazy1 = {new LazyImageIcon("/imgs/refresh/1.png"),new LazyImageIcon("/imgs/refresh/2.png"),
			new LazyImageIcon("/imgs/refresh/3.png"),new LazyImageIcon("/imgs/refresh/4.png"),
			new LazyImageIcon("/imgs/refresh/5.png"),new LazyImageIcon("/imgs/refresh/6.png"),
			new LazyImageIcon("/imgs/refresh/7.png"),new LazyImageIcon("/imgs/refresh/8.png")};
	
	private RunIcon runIcon;
	private boolean isExec = true;
	private Object o;
	
	public RefreIcon(Object o, RunIcon runIcon){
		this.runIcon = runIcon;
		this.o = o;
	}
	
	public RefreIcon( RunIcon runIcon){
		this.runIcon = runIcon;
	}
	
	public Object getObject() {
		return this.o;
	}
	@Override
	public void run() {
		int i =0;
		while(isExec){
			try {
				Thread.sleep(100);
				runIcon.refreIcon(lazy1[i]);
				if(i<7){
					i++;
				}
				else{
					i=0;
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		runIcon.endIcon();
//			SQLTool.getSQLTool().getToolFrame().getSqlView().getDataBaseTree().repaint();
	}

	public void stop() {
		isExec = false;
	}
	public boolean isExec(){
		return isExec;
	}
}

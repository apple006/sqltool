package com.ui.tree;

import java.util.HashMap;

import com.ui.MenuListenner;

public class MouserListenFactory {
	
	private HashMap<String, MenuListenner> menuListenner =new HashMap<String, MenuListenner>();
	private static MouserListenFactory factory = new MouserListenFactory();
	private MouserListenFactory(){
		
	}
	public static MouserListenFactory getMouserListenFactory(){
		return factory;
	}
	public MenuListenner getMenuListenner(final String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		 MenuListenner listenner = menuListenner.get(className);
		 if(listenner==null){
			 synchronized (menuListenner) {
				if(menuListenner.get(className)==null){
					listenner = (MenuListenner) getListenerObject(className);
					menuListenner.put(className, listenner);
				}
			}
		 }
		 return listenner;
	}
	/**
	 * 
	 * @param claspath  监听类路径
	 * @return  Object        监听类实例
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Object getListenerObject(String claspath) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (!"".equals(claspath) && claspath != null) {
				Class forName = (Class<Singleton>) Class.forName(claspath);
				return  forName.newInstance();
		}
		return new MenuListenner();
	}
}

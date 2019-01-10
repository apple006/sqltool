package com.ico;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

public class ImagesManager {
	private Map<String,LazyImageIcon> icons =  new HashMap<String, LazyImageIcon>();
	private static ImagesManager mange = new ImagesManager();
	private ImagesManager(){}
	
	public static ImagesManager getImagesManager(){
		return mange;
	}
	
	public Icon  getIcon(String icoName){
		LazyImageIcon lazy = icons.get(icoName);
		Icon icon;
		if(lazy==null){
			LazyImageIcon lazyImageIcon = new LazyImageIcon(icoName);
			icons.put(icoName,lazyImageIcon );
			icon = lazyImageIcon.getIcon();
		}
		else{
			icon = lazy.getIcon();
		}
		return icon;
	}
}

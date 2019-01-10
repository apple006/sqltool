package com.view.tool;

public interface IUtilCheck {

	default boolean isNull(Object o){
		if(o==null){
			return true;
		}
		if("".equals(o.toString().trim())){
			return true;
		}
		return false;
	}
}

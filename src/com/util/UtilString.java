package com.util;

public class UtilString {

	public static boolean isNull(Object str){
		if(str==null){
			return true;
		}else{
			if("".endsWith(str.toString().trim())){
				return true;
			}
		}
		return false;
	}
}

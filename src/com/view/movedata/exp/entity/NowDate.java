package com.view.movedata.exp.entity;

import java.io.Serializable;

import com.util.UtilDate;

public class NowDate implements Serializable{

	private String fomat;
	public NowDate(String fomat){
		this.fomat = fomat;
	}
	
	public String getNowDate(){
		return UtilDate.getNowFomatDate(fomat);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return fomat;
	}
}

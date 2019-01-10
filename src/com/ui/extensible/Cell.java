package com.ui.extensible;

public class Cell {

	public static final String NULLSTR = "";
	private Object new_value;
	private Object old_value;
	
	public Cell(){}
	public Cell(Object new_Object){
		this.old_value = new_Object;
	}
	public Cell(Object  new_value,Object old_value){
		this.new_value = new_value;
		this.old_value =old_value;
	}
	public Object getNew_value() {
		return new_value;
	}


	public void setNew_value(Object new_value) {
		this.new_value = new_value;
	}


	public Object getOld_value() {
		return old_value;
	}


	public void setOld_value(Object old_value) {
		this.old_value = old_value;
	}


	public boolean isChange(){
		if(this.old_value==null){
			if(new_value!=null){
				return true;
			}else{
				return false;
			}
		}else{
			if(this.new_value!=null){
//				if(this.old_value.getClass()==this.new_value.get){
//					
//				}
				if(this.old_value.toString().equals(this.new_value.toString())){
					return false;
				}else{
					return true;
				}
			}else{
				return false;
			}
			
		}
	}
	@Override
	public String toString() {
		if(old_value==null){
			return "null";
		}
		return old_value.toString();
	}
}

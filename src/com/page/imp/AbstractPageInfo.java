package com.page.imp;

import com.page.PageInfo;

public abstract class AbstractPageInfo implements PageInfo{

	private int pageSize = 100;
	private int begin;
	private int end = pageSize;
	private int countPage;
	@Override
	public int getPageSize() {
		return pageSize;
	}
	
	public boolean nextPage(){
		if(end <countPage){
			begin = begin + pageSize;
			end = pageSize + begin;
		}
		return end <countPage;
	}
	public boolean prePage(){
		if(begin > 0)
		begin = begin - pageSize;
		end = pageSize - begin;
		return begin >0;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	

}

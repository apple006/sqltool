package com.view.movedata.execut;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class ExpMsg implements  Serializable{
	private int max=-2;
	private String startDate;
	private String endDate;
	private String showVaue;
	private AtomicLong value;
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public ExpMsg( int max){
		this.max = max;
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		startDate = df.format(new Date());
		value = new AtomicLong();
	}
	
	public ExpMsg(){
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		startDate = df.format(new Date());
		value = new AtomicLong();
	}
	public int getMax(){
		return max;
		
	}
	
	public void setMax(int max){
		this.max = max;
	}


	public String getShowVaue() {
		return showVaue;
	}
	public void setShowVaue(String showVaue) {
		this.showVaue = showVaue;
	}
	public AtomicLong getValue() {
		return value;
	}
	public long setValue() {
		return value.getAndIncrement();
	}
	public long setValue(long l) {
		return value.addAndGet(l);
	}
	public void setInitValue(long l) {
		 value.set(l);
	}
}